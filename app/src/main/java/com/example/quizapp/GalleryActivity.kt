package com.example.quizapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        // Loading the array from internal storage
        val loadedArray = ArrayStorage.loadArray(this)

        // Displaying the loaded array of Photo objects
        displayPhotos(loadedArray)
    }

    // Function to display the loaded array of Photo objects
    private fun displayPhotos(photoArray: Array<Photo>?) {
        if (photoArray != null) {
            val photosLayout: GridLayout = findViewById(R.id.photosLayout)

            photosLayout.rowCount = (photoArray.size + 2) / 3

            for (photo in photoArray) {
                val imageButton = ImageButton(this)
                val imageButtonSize = resources.displayMetrics.widthPixels / 3 // Assuming 3 images per row
                val layoutParams = GridLayout.LayoutParams().apply {
                    width = imageButtonSize
                    height = imageButtonSize
                }
                imageButton.layoutParams = layoutParams

                // Set image resource dynamically based on photo file name
                val resourceId = resources.getIdentifier(photo.fileName, "drawable", packageName)
                imageButton.setImageResource(resourceId)
                imageButton.scaleType = ImageView.ScaleType.FIT_CENTER
                imageButton.contentDescription = photo.description
                photosLayout.addView(imageButton)
            }

            // Add ImageButton for "plus"
            val plusImageButton = ImageButton(this)
            val imageButtonSize = (resources.displayMetrics.widthPixels) / 3 // Considering 3 images per row and subtracting padding
            val layoutParams = GridLayout.LayoutParams().apply {
                width = imageButtonSize
                height = imageButtonSize
            }
            plusImageButton.layoutParams = layoutParams

            // Set image resource for "plus" button
            plusImageButton.setBackgroundColor(Color.TRANSPARENT)
            val plusResourceId = resources.getIdentifier("plus", "drawable", packageName)
            plusImageButton.setImageResource(plusResourceId)
            plusImageButton.scaleType = ImageView.ScaleType.FIT_CENTER
            plusImageButton.contentDescription = "Add Photo"
            photosLayout.addView(plusImageButton)
        }
    }
}
