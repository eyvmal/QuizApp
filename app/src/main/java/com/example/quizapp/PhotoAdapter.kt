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

    // Create a new ViewHolder and inflates the layout defined in item_photo.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoArray[position]
        val cacheImageFile = File(holder.itemView.context.cacheDir, photo.fileName)

        if (cacheImageFile.exists()) {  // If the photo is stored in cache
            val bitmap = BitmapFactory.decodeFile(cacheImageFile.absolutePath)
            holder.imageButton.setImageBitmap(bitmap)
        } else {  // If not, it has to be stored in @drawable
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

    // Define ViewHolder for each photo item
    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
        private val buttonText: TextView = itemView.findViewById(R.id.buttonText)

        fun bind(photo: Photo) {
            buttonText.text = photo.description

            // Set dimensions of photos allowing for 3 each row
            val widthPixels = itemView.resources.displayMetrics.widthPixels
            val imageSize = widthPixels / 3
            imageButton.layoutParams.width = imageSize
            imageButton.layoutParams.height = imageSize

            // Set click listener for deletion
            imageButton.setOnClickListener {
                onDeleteClickListener(bindingAdapterPosition)
            }
        }
    }

    // Function to update the dataset
    fun updateDataSet(newPhotoArray: Array<Photo>) {
        photoArray = newPhotoArray.copyOf()
        notifyDataSetChanged()
    }
}
