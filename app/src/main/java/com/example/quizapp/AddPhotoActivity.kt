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
import androidx.appcompat.content.res.AppCompatResources

class AddPhotoActivity : AppCompatActivity() {

    // Variable to store the user-uploaded picture
    private lateinit var selectedImageFile: File

    // Define an activity result launcher for selecting an image.
    // Check if the activity was successful.
    // If so, save image to cache and set the src of the imageButton to the image.
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

        // Find and store a reference to the widgets
        val imageButton: ImageButton = findViewById(R.id.imageButton)
        val saveButton: Button = findViewById(R.id.button3)
        val editText: EditText = findViewById(R.id.editTextText)

        // Add listeners to the widgets
        imageButton.setOnClickListener {
            openFileChooser()
        }

        saveButton.setOnClickListener {
            // Store user input in a val and remove whitespace
            val text = editText.text.toString().trim()
            // Check that there is both user input and a picture uploaded
            // Check if both user input and a picture are uploaded
            if (text.isNotEmpty() && imageButton.drawable != AppCompatResources.getDrawable(this, R.drawable.plus)) {
                // Check if the text contains only alphanumeric characters and space
                if (text.matches(Regex("[a-zA-Z0-9 ]+"))) {
                    // Replace spaces with underscores and saves the image in cache
                    val imageName = text.replace("\\s+".toRegex(), "_")
                    val newImageFile = File(cacheDir, "$imageName.jpg")
                    selectedImageFile.copyTo(newImageFile)

                    // Create a new Photo object and save it to the PhotoArray
                    val photo = Photo("$imageName.jpg", text)
                    val loadedArray = ArrayStorage.loadArray(this)?.toMutableList() ?: mutableListOf()
                    loadedArray.add(photo)
                    ArrayStorage.saveArray(this, loadedArray.toTypedArray())

                    // Create a toast showing the task was successful
                    Toast.makeText(this, "New image saved to gallery!", Toast.LENGTH_SHORT).show()

                    // Finish the activity and go back to GalleryActivity
                    val intent = Intent(this, GalleryActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // Display an error message if the text contains non-alphanumeric characters
                    Toast.makeText(this, "Text should only contain letters and numbers.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Display an error message if either user input or a picture is missing
                Toast.makeText(this, "Please enter text and select an image.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Lets the user upload an image and launches the selectImageLauncher to handle the upload
    private fun openFileChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        selectImageLauncher.launch(intent)
    }

    // Saves the image temporarily to cache.
    // It's never deleted, but the next upload will overwrite it.
    private fun saveImageToTempLocation(inputStream: InputStream) {
        selectedImageFile = File(cacheDir, "temp_image.jpg")
        FileOutputStream(selectedImageFile).use { output ->
            inputStream.copyTo(output)
        }
    }

    // Set the active src of the imageButton to file parameter
    private fun setImageFromFile(file: File) {
        val imageButton: ImageButton = findViewById(R.id.imageButton)
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        imageButton.setImageBitmap(bitmap)
    }
}