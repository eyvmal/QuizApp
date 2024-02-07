package com.example.quizapp

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import android.os.Bundle
import android.widget.ImageButton

class AddPhotoActivity : AppCompatActivity() {

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val inputStream: InputStream? = data?.data?.let { contentResolver.openInputStream(it) }
            // Handle the input stream (e.g., save it to drawable folder)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addphoto)

        val imageButton: ImageButton = findViewById(R.id.imageButton)
        imageButton.setOnClickListener {
            openFileChooser()
        }
    }

    private fun openFileChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        selectImageLauncher.launch(intent)
    }
}