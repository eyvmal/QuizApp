package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import kotlin.math.ceil
import java.io.File
import android.graphics.BitmapFactory
import android.app.AlertDialog

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val quizButton = findViewById<Button>(R.id.button)

        quizButton.setOnClickListener {
            // Create an intent to navigate to the AddPhotoActivity
            val intent = Intent(this, AddPhotoActivity::class.java)
            finish()
            startActivity(intent)
        }

        val loadedArray = ArrayStorage.loadArray(this)
        displayPhotos(loadedArray)
    }

    // Function to display the loaded array of Photo objects
    private fun displayPhotos(photoArray: Array<Photo>?) {
        if (photoArray != null) {
            val photosLayout: GridLayout = findViewById(R.id.photosLayout)

            photosLayout.rowCount = ceil(photoArray.size.toDouble() / 3).toInt()

            for (i in photoArray.indices) {
                val photo = photoArray[i]
                val imageButton = ImageButton(this)
                val imageButtonSize = resources.displayMetrics.widthPixels / 3 // Assuming 3 images per row
                val layoutParams = GridLayout.LayoutParams().apply {
                    width = imageButtonSize
                    height = imageButtonSize
                }
                imageButton.layoutParams = layoutParams

                // Try loading the image as a cache resource
                val cacheImageFile = File(cacheDir, photo.fileName)
                if (cacheImageFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(cacheImageFile.absolutePath)
                    imageButton.setImageBitmap(bitmap)
                } else {
                    // Try loading the image as a drawable resource
                    val resourceId = resources.getIdentifier(photo.fileName, "drawable", packageName)
                    if (resourceId != 0) {
                        imageButton.setImageResource(resourceId)
                    } else {
                        Log.e("Image Loading", "Failed to load image: ${photo.fileName}")
                        continue
                    }
                }

                imageButton.scaleType = ImageView.ScaleType.FIT_CENTER
                imageButton.contentDescription = photo.description

                // Set click listener to display name with Toast
                imageButton.setOnClickListener {
                    Toast.makeText(this, photo.description, Toast.LENGTH_SHORT).show()
                }

                // Set long click listener to delete the photo
                imageButton.setOnLongClickListener {
                    deletePhoto(photoArray, i, cacheImageFile, photo.description)
                    true // Return true to consume the long click event
                }

                photosLayout.addView(imageButton)
            }
        }
    }

    private fun deletePhoto(photoArray: Array<Photo>, index: Int, imageFile: File, fileDesc: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete $fileDesc")
        alertDialogBuilder.setMessage("Are you sure you want to delete $fileDesc?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // Delete the image file from cache
            if (imageFile.exists()) {
                imageFile.delete()
            }

            // Remove the photo from the array
            val updatedArray = photoArray.toMutableList()
            updatedArray.removeAt(index)

            // Save the updated array
            ArrayStorage.saveArray(this, updatedArray.toTypedArray())

            // Restart the activity to refresh
            val intent = Intent(this, GalleryActivity::class.java)
            finish()
            startActivity(intent)
        }

        alertDialogBuilder.setNegativeButton("No") { _, _ -> }

        alertDialogBuilder.create().show()
    }
}
