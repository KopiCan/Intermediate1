package com.dicoding.intermediate1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.dicoding.intermediate1.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var gallery: ActivityResultLauncher<Intent>
    private var capture: ImageCapture? = null
    private var selector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openGallery()
        binding.let {
            it.imgShutter.setOnClickListener {
                takePhoto()
            }
            it.imgSwitchCamera.setOnClickListener {
                selector =
                    if (selector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                    else CameraSelector.DEFAULT_BACK_CAMERA
                runCamera()
            }
            it.imgGallery.setOnClickListener {
                runGallery()
            }
            it.imgBack.setOnClickListener {
                onBackPressed()
            }
        }
        runCamera()
    }

    private fun runGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val picker = Intent.createChooser(intent, "Choose a Picture")
        gallery.launch(picker)
    }

    private fun takePhoto() {
        val capture = capture ?: return
        val imgFile = Helper.createFile(application)
        val option = ImageCapture.OutputFileOptions.Builder(imgFile).build()
        capture.takePicture(option, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {

                override fun onError(exc: ImageCaptureException) {
                    Helper.showInteraction(this@CameraActivity, "${getString(R.string.capture_failed)} : ${exc.message}")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent(this@CameraActivity, AddStoryActivity::class.java)
                    intent.putExtra(AddStoryActivity.EXTRA_PHOTO, imgFile)
                    intent.putExtra(AddStoryActivity.EXTRA_CAMERA, selector== CameraSelector.DEFAULT_BACK_CAMERA)
                    this@CameraActivity.finish()
                    startActivity(intent)
                }
            }
        )
    }

    private fun openGallery() {
        gallery = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.i("TEST_GALERY", "Galeri berhasil dipilih")
                val file: Uri = result.data?.data as Uri
                val temporaryFile = Helper.getToFile(file, this@CameraActivity)
                val intent = Intent(this@CameraActivity, AddStoryActivity::class.java)
                intent.putExtra(AddStoryActivity.EXTRA_PHOTO, temporaryFile)
                intent.putExtra(AddStoryActivity.EXTRA_CAMERA, selector == CameraSelector.DEFAULT_BACK_CAMERA)
                this@CameraActivity.finish()
                startActivity(intent)
            }
        }
    }

    private fun runCamera() {
        val cameraProvider = ProcessCameraProvider.getInstance(this)
        cameraProvider.addListener({
            val cameraTemp: ProcessCameraProvider = cameraProvider.get()
            val imgAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(480, 720))
                .build()
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding.pvViewFinder.surfaceProvider) }
            capture = ImageCapture.Builder().setTargetResolution(Size(480, 720)).build()
            try {
                cameraTemp.unbindAll()
                cameraTemp.bindToLifecycle(this, selector, preview, capture, imgAnalysis)
            } catch (e: Exception) {
                Helper.showInteraction(this, "Camera Failed to Open : ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }
}