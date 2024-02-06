package com.example.quizapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        // Test data: Creating an array of Photo objects
        val photoArray = arrayOf(
            Photo("mcdonalds", "McDonalds"),
            Photo("nike", "Nike"),
            Photo("pepsi", "Pepsi")
        )

        // Saving the array to internal storage
        ArrayStorage.saveArray(this, photoArray)

        // Loading the array from internal storage
        val loadedArray = ArrayStorage.loadArray(this)

        // Displaying the loaded array of Photo objects
        displayPhotos(loadedArray)
    }

    // Function to display the loaded array of Photo objects
    private fun displayPhotos(photoArray: Array<Photo>?) {
        if (photoArray != null) {
            val photosLayout: LinearLayout = findViewById(R.id.photosLayout)

            for (photo in photoArray) {
                val imageButton = ImageButton(this)
                imageButton.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                // Set image resource dynamically based on photo file name
                val resourceId = resources.getIdentifier(photo.fileName, "drawable", packageName)
                imageButton.setImageResource(resourceId)
                imageButton.contentDescription = photo.description
                photosLayout.addView(imageButton)
            }
        }
    }
}
