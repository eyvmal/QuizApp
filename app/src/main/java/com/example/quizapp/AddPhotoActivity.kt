package com.example.quizapp

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import android.graphics.BitmapFactory

class AddPhotoActivity : AppCompatActivity() {

    private lateinit var selectedImageFile: File

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val inputStream: InputStream? = data?.data?.let { contentResolver.openInputStream(it) }
            inputStream?.let {
                saveImageToTempLocation(it)
                setImageFromFile(selectedImageFile)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addphoto)

        val imageButton: ImageButton = findViewById(R.id.imageButton)
        val saveButton: Button = findViewById(R.id.button3)
        val editText: EditText = findViewById(R.id.editTextText)

        imageButton.setOnClickListener {
            openFileChooser()
        }

        saveButton.setOnClickListener {
            val text = editText.text.toString()
            if (text.isNotEmpty() && imageButton.drawable != getDrawable(R.drawable.plus)) {
                val imageName = text.replace("\\s+".toRegex(), "_") // Replace spaces with underscores

                val newImageFile = File(cacheDir, "$imageName.jpg") // Ensure to add the file extension
                selectedImageFile.copyTo(newImageFile) // Copy the contents to the new file

                val photo = Photo("$imageName.jpg", text)
                val loadedArray = ArrayStorage.loadArray(this)?.toMutableList() ?: mutableListOf()
                loadedArray.add(photo)
                ArrayStorage.saveArray(this, loadedArray.toTypedArray())
                Toast.makeText(this, "Successful!", Toast.LENGTH_SHORT).show()

                // Finish the activity and go back to GalleryActivity
                val intent = Intent(this, GalleryActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                // Show an error message stating what's wrong
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        selectImageLauncher.launch(intent)
    }

    private fun saveImageToTempLocation(inputStream: InputStream) {
        selectedImageFile = File(cacheDir, "temp_image.jpg")
        FileOutputStream(selectedImageFile).use { output ->
            inputStream.copyTo(output)
        }
    }

    private fun setImageFromFile(file: File) {
        val imageButton: ImageButton = findViewById(R.id.imageButton)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        imageButton.setImageBitmap(bitmap)
    }
}