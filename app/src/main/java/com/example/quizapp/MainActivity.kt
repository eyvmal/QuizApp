package com.example.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find and store a reference to the buttons
        val quizButton = findViewById<Button>(R.id.button)
        val galleryButton = findViewById<Button>(R.id.button2)

        // Add listeners to the buttons
        quizButton.setOnClickListener {
            // Create an intent to navigate to the QuizActivity
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
        galleryButton.setOnClickListener {
            // Create an intent to navigate to the GalleryActivity
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
    }
}