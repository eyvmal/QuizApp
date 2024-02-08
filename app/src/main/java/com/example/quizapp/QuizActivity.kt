package com.example.quizapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.io.File
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {

    // Variables used in multiple functions
    private lateinit var photoArray: Array<Photo>
    private var currentIndex: Int = 0
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Load the array from ArrayStorage and randomize it
        photoArray = ArrayStorage.loadArray(this)?.let {
            it.shuffle()
            it
        } ?: emptyArray()

        // Display the first image in the randomized array,
        // randomize the options, and set the score to a initial 0
        displayImage(currentIndex)
        generateOptions()
        updateScoreTextView()

        setupButtons()
    }

    // Setup buttons with clickListeners
    private fun setupButtons() {
        val option1Button = findViewById<Button>(R.id.option1)
        val option2Button = findViewById<Button>(R.id.option2)
        val option3Button = findViewById<Button>(R.id.option3)

        option1Button.setOnClickListener {
            checkAnswer(option1Button.text.toString())
        }
        option2Button.setOnClickListener {
            checkAnswer(option2Button.text.toString())
        }
        option3Button.setOnClickListener {
            checkAnswer(option3Button.text.toString())
        }
    }

    // Compare the textValue in this button to the answer
    private fun checkAnswer(answer: String) {
        val correctAnswer = photoArray[currentIndex].description

        // Add green/red buttons, maybe?!

        // Add score and update it if the answer is correct
        if (answer == correctAnswer) {
            score++
            updateScoreTextView()
        }

        // Move to the next question
        currentIndex++
        if (currentIndex < photoArray.size) {
            displayImage(currentIndex)
            generateOptions()
        } else {
            // Handle end of game logic.
            // For now, it just finishes the Activity
            finish()
        }
    }

    // Loads the image from cache/drawable into the imageView
    private fun displayImage(index: Int) {
        if (index in photoArray.indices) {
            val imageView = findViewById<ImageView>(R.id.imageView)
            val photo = photoArray[index]

            // Try loading the image as a cache resource
            val cacheImageFile = File(cacheDir, photo.fileName)
            if (cacheImageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(cacheImageFile.absolutePath)
                imageView.setImageBitmap(bitmap)
            } else {
                // Try loading the image as a drawable resource
                val resourceId = resources.getIdentifier(photo.fileName, "drawable", packageName)
                if (resourceId != 0) {
                    imageView.setImageResource(resourceId)
                } else {
                    Log.e("Image Loading", "Failed to load image: ${photo.fileName}")
                }
            }
        }
    }

    // Generates the two other random options, and randomizes it.
    private fun generateOptions() {
        val options = mutableListOf<String>()

        // Add the correct answer
        options.add(photoArray[currentIndex].description)

        // Add two random descriptions
        val random = Random
        while (options.size < 3) {
            val randomIndex = random.nextInt(photoArray.size)
            val randomDesc = photoArray[randomIndex].description
            if (!options.contains(randomDesc)) {
                options.add(randomDesc)
            }
        }

        // Shuffle the options
        options.shuffle()
        setOptionsToButtons(options)
    }

    // Applies the options to the different buttons
    private fun setOptionsToButtons(options: List<String>) {
        val option1Button = findViewById<Button>(R.id.option1)
        val option2Button = findViewById<Button>(R.id.option2)
        val option3Button = findViewById<Button>(R.id.option3)

        // Set options to buttons
        option1Button.text = options[0]
        option2Button.text = options[1]
        option3Button.text = options[2]
    }

    // Updates the score textView
    private fun updateScoreTextView() {
        val scoreTextView = findViewById<TextView>(R.id.textView2)
        scoreTextView.text = getString(R.string.score, score)
    }
}
