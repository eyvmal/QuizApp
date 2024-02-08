package com.example.quizapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import android.graphics.BitmapFactory

class PhotoAdapter(
    private var photoArray: Array<Photo>,
    private val onDeleteClickListener: (Int) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoArray[position]
        val cacheImageFile = File(holder.itemView.context.cacheDir, photo.fileName)

        if (cacheImageFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(cacheImageFile.absolutePath)
            holder.imageButton.setImageBitmap(bitmap)
        } else {
            val resourceId = holder.itemView.context.resources.getIdentifier(photo.fileName, "drawable", holder.itemView.context.packageName)
            if (resourceId != 0) {
                holder.imageButton.setImageResource(resourceId)
            }
        }
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return photoArray.size
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
        val buttonText: TextView = itemView.findViewById(R.id.buttonText)

        fun bind(photo: Photo) {
            // Set text
            buttonText.text = photo.description

            // Set square dimensions
            val widthPixels = itemView.resources.displayMetrics.widthPixels
            val imageSize = widthPixels / 3 // Assuming 3 images per row
            imageButton.layoutParams.width = imageSize
            imageButton.layoutParams.height = imageSize

            // Set click listener
            imageButton.setOnClickListener {
                onDeleteClickListener(adapterPosition)
            }
        }
    }

    // Function to update the dataset
    fun updateDataSet(newPhotoArray: Array<Photo>) {
        photoArray = newPhotoArray.copyOf()
        notifyDataSetChanged()
    }
}
