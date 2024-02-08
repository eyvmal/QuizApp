package com.example.quizapp

import android.content.Context
import java.io.*

object ArrayStorage {
    private const val FILE_NAME = "photo_array.dat"

    // Function to save an array of Photo objects to internal storage
    fun saveArray(context: Context, array: Array<Photo>) {
        ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)).use { stream ->
            stream.writeObject(array)
        }
    }

    // Function to load an array of Photo objects from internal storage
    fun loadArray(context: Context): Array<Photo>? {
        return try {
            ObjectInputStream(context.openFileInput(FILE_NAME)).use { stream ->
                stream.readObject() as Array<Photo>?
            }
        } catch (e: FileNotFoundException) {
            // If photo_array.dat doesn't exist. Create this default array.
            return arrayOf(
                Photo("mcdonalds", "McDonald's"),
                Photo("nike", "Nike"),
                Photo("pepsi", "Pepsi")
            )
        }
    }
}
