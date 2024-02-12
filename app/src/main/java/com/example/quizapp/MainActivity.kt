package com.example.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find and store a reference to the buttons
        val quizButton = findViewById<Button>(R.id.button)
        val galleryButton = findViewById<Button>(R.id.button2)
        val resetButton = findViewById<Button>(R.id.button4)

        // Add listeners to the buttons
        quizButton.setOnClickListener {
            // Start the QuizActivity only if there are 3 or more items in the gallery
            // If value is null, its handled as 0
            if ((ArrayStorage.loadArray(this)?.size ?: 0) >= 3) {
                val intent = Intent(this, QuizActivity::class.java)
                startActivity(intent)
            } else {
                // If there's not enough photos in the gallery
                Toast.makeText(this, "There must be at least 3 items in the gallery to start the quiz.", Toast.LENGTH_SHORT).show()
            }
        }
        galleryButton.setOnClickListener {
            // Create an intent to navigate to the GalleryActivity
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
        resetButton.setOnClickListener {
            // Resets the gallery to default
            resetGallery()
        }
    }

    private fun resetGallery() {
        val cacheDirectory = cacheDir
        val jpgFiles = cacheDirectory.listFiles { file ->
            file.isFile && file.extension == "jpg"
        }
        // Delete all image files in cache
        jpgFiles?.forEach { file ->
            file.delete()
        }
        // Reset the array to its default, with the 3 default photos
        ArrayStorage.resetArray(this)
        Toast.makeText(this, "Gallery is now reset!", Toast.LENGTH_SHORT).show()
    }

}