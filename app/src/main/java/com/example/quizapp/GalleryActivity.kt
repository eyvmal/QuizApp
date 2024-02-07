package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.widget.Button
import kotlin.math.ceil

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val quizButton = findViewById<Button>(R.id.button)

        quizButton.setOnClickListener {
            // Create an intent to navigate to the AddPhotoActivity
            val intent = Intent(this, AddPhotoActivity::class.java)
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

            for (photo in photoArray) {
                val imageButton = ImageButton(this)
                val imageButtonSize =
                    resources.displayMetrics.widthPixels / 3 // Assuming 3 images per row
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
        }
    }
}
