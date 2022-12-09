package com.dicoding.intermediate1

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object Helper {


    @SuppressLint("ConstantLocale")
    val recentTime: String = SimpleDateFormat("ddMMyySSSS", Locale.getDefault()).format(System.currentTimeMillis())

    private fun createTemporaryFile(context: Context): File {
        val directory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(recentTime, ".jpg", directory)
    }

    fun getToFile(file: Uri, context: Context): File {
        val resolver: ContentResolver = context.contentResolver
        val input = resolver.openInputStream(file) as InputStream
        val temporaryFile = createTemporaryFile(context)
        val output: OutputStream = FileOutputStream(temporaryFile)
        val byte = ByteArray(1080)
        var temp: Int
        while (input.read(byte).also { temp = it  } > 0) output.write(byte, 0, temp)
        output.close()
        input.close()
        return temporaryFile
    }

    fun createFile(application: Application): File {
        val directory = application.externalMediaDirs.firstOrNull()?.let { File(it, "story").apply { mkdirs() }
        }
        val output = if (directory != null && directory.exists())directory else application.filesDir

        return File(output, "STORY-$recentTime.jpg")
    }

    fun rotation(bitmap: Bitmap, cameraState: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (cameraState) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, -1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }

    fun interactionBuilder(context: Context, information: String): Dialog {
        val interaction = Dialog(context)
        interaction.setCancelable(false)
        interaction.window!!.apply {
            val parameter: WindowManager.LayoutParams = this.attributes
            parameter.width = WindowManager.LayoutParams.MATCH_PARENT
            parameter.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes.windowAnimations = android.R.transition.fade
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        interaction.setContentView(R.layout.interaction_layout)
        val tvInfo = interaction.findViewById<TextView>(R.id.tv_interaction)
        tvInfo.text = information
        return interaction
    }

    fun showInteraction(context: Context, information: String) {
        val interaction = interactionBuilder(context, information)
        val btnInformation = interaction.findViewById<Button>(R.id.btn_interaction)
        btnInformation.setOnClickListener {
            interaction.dismiss()
        }
        interaction.show()
    }

}
