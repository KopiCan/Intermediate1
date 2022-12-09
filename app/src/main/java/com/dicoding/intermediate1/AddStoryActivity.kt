package com.dicoding.intermediate1

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate1.databinding.AcvitvityAddStoryBinding
import java.io.File
import java.lang.StringBuilder

class AddStoryActivity: AppCompatActivity() {

    private var token: String? = null
    private var homeViewModel: HomeViewModel? = null
    private lateinit var binding: AcvitvityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AcvitvityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel = ViewModelProvider(this, ViewModelFactory(this)
        )[HomeViewModel::class.java]

        val preferences = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelSettingFactory(preferences))[ViewModelSetting::class.java]
        settingViewModel.getProfilePreferences(Constanst.ProfilePreferences.ProfileToken.name)
            .observe(this) { userToken ->
                token = StringBuilder("Bearer ").append(token).toString()
            }


        val temporaryFile = intent?.getSerializableExtra(EXTRA_PHOTO) as File
        val cameraState = intent?.getBooleanExtra(EXTRA_CAMERA, true) as Boolean
        val rotation = Helper.rotation(BitmapFactory.decodeFile(temporaryFile.path), cameraState)
        binding.imgNew.setImageBitmap(rotation)
        binding.btnAddStory.setOnClickListener {
            if (binding.descriptionInput.text.isNotEmpty()) {
                uploadImage(temporaryFile, binding.descriptionInput.text.toString())
            } else {
                Helper.showInteraction(
                    this,
                    getString(R.string.description_mandatory)
                )
            }
        }
        homeViewModel?.let { state ->
            state.storyLoadState.observe(this) {
                if (it) {
                    val dialog = Helper.interactionBuilder(this, getString(R.string.story_succes))
                    val btnOk = dialog.findViewById<Button>(R.id.btn_interaction)
                    btnOk.setOnClickListener { finish() }
                    dialog.show()
                }
            }
            state.loader.observe(this) {
                binding.loadingAnim.root.visibility = it
            }
            state.error.observe(this) {
                if (it.isNotEmpty()) {
                    Helper.showInteraction(this, it)
                }
            }
        }
    }

    private fun uploadImage(temporaryFile: File, description: String){
        if (token != null) {
            homeViewModel?.newStory(token!!, temporaryFile, description)
        } else {
            Helper.showInteraction(
                this,
                getString(R.string.error_token_401)
            )
        }
    }

    companion object {
        const val EXTRA_PHOTO = "PHOTO_RESULT"
        const val EXTRA_CAMERA = "CAMERA_MODE"
    }

}
