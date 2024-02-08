package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class GalleryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var photoArray: Array<Photo>? = null
    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        // Assign the loaded photo array to the class member
        photoArray = ArrayStorage.loadArray(this)
        displayPhotos()

        val addPhotoButton = findViewById<Button>(R.id.button)
        addPhotoButton.setOnClickListener {
            // Create an intent to navigate to the AddPhotoActivity
            val intent = Intent(this, AddPhotoActivity::class.java)
            finish()
            startActivity(intent)
        }

        val sortAZButton = findViewById<Button>(R.id.button6)
        sortAZButton.setOnClickListener {
            sortPhotosByDescription(true)
        }

        val sortZAButton = findViewById<Button>(R.id.button5)
        sortZAButton.setOnClickListener {
            sortPhotosByDescription(false)
        }
    }

    private fun displayPhotos() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@GalleryActivity, 3)
            adapter = photoArray?.let { array ->
                // Define onDeleteClickListener here
                val onDeleteClickListener: (Int) -> Unit = { clickedPosition ->
                    photoArray?.let { array ->
                        array.toMutableList().apply {
                            removeAt(clickedPosition)
                        }.also { updatedList ->
                            photoArray = updatedList.toTypedArray()
                            ArrayStorage.saveArray(this@GalleryActivity, updatedList.toTypedArray())
                            // Update the adapter's dataset instead of creating a new adapter
                            (adapter as? PhotoAdapter)?.updateDataSet(updatedList.toTypedArray())
                        }
                    }
                }
                // Create a new instance of PhotoAdapter with the current array and onDeleteClickListener
                PhotoAdapter(array, onDeleteClickListener)
            }
        }
    }


    private fun sortPhotosByDescription(ascending: Boolean) {
        photoArray?.let { array ->
            val sortedArray = array.copyOf() // Create a copy of the original array
            sortedArray.sortBy { photo -> photo.description }
            if (!ascending) {
                sortedArray.reverse()
            }
            ArrayStorage.saveArray(this, sortedArray)
            photoArray = sortedArray // Update the class member with the sorted array
            displayPhotos()
        }
    }
}
