package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import java.io.File

class GalleryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var photoArray: Array<Photo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        // Assign the loaded photo array to the local variable
        photoArray = ArrayStorage.loadArray(this)
        displayPhotos()

        // Configure the buttons
        val addPhotoButton = findViewById<Button>(R.id.button)
        addPhotoButton.setOnClickListener {
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

    // Function to handle the deletion of a photo
    private fun handleDeletePhoto(clickedPosition: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this@GalleryActivity)
        alertDialogBuilder.apply {
            setTitle("Delete Photo")
            setMessage("Are you sure you want to delete this photo?")
            setPositiveButton("Yes") { _, _ ->
                // Proceed to delete photo
                photoArray?.let { array ->
                    // Delete image from cache
                    val cacheImageFile = File(cacheDir, array[clickedPosition].fileName)
                    if (cacheImageFile.exists()) {
                        cacheImageFile.delete()
                    }
                    // Remove photo from array
                    array.toMutableList().apply {
                        removeAt(clickedPosition)
                    }.also { updatedList ->
                        photoArray = updatedList.toTypedArray()
                        ArrayStorage.saveArray(this@GalleryActivity, updatedList.toTypedArray())
                        // Update the adapter's dataset instead of creating a new adapter
                        (recyclerView.adapter as? PhotoAdapter)?.updateDataSet(updatedList.toTypedArray())
                    }
                }
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }
        alertDialogBuilder.create().show()
    }

    // Function to display the photos in gallery
    private fun displayPhotos() {
        // Set up the recyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.apply {
            // Using a grid
            layoutManager = GridLayoutManager(this@GalleryActivity, 3)
            adapter = photoArray?.let { array ->
                // Create onDeleteClickListener
                val onDeleteClickListener: (Int) -> Unit = { clickedPosition ->
                    // Handle delete action
                    handleDeletePhoto(clickedPosition)
                }
                // Create a new instance of PhotoAdapter with the current array and onDeleteClickListener
                PhotoAdapter(array, onDeleteClickListener)
            }
        }
    }


    // Function to sort photos. True -> A-Z, False -> Z-A
    private fun sortPhotosByDescription(ascending: Boolean) {
        photoArray?.let { array ->
            val sortedArray = array.copyOf()
            sortedArray.sortBy { photo -> photo.description }
            if (!ascending) {
                sortedArray.reverse()
            }
            ArrayStorage.saveArray(this, sortedArray)
            // Update the array
            photoArray = sortedArray
            displayPhotos()
        }
    }
}
