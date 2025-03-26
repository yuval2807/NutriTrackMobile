package com.example.nutriTrack

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream

fun Bitmap.toFile(context: Context, name: String): File {
    val file = File(context.cacheDir, "image_$name.jpg")
    FileOutputStream(file).use { stream ->
        compress(Bitmap.CompressFormat.JPEG, 100, stream)
    }

    return file
}


fun loadImageIntoImageView(imageView: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Picasso.get()
            .load(imageUrl) // Load from Firebase Storage URL
            .placeholder(R.drawable.image_placeholder) // Optional placeholder
            .into(imageView) // Set into ImageView
    } else {
        imageView.setImageResource(R.drawable.image_placeholder) // Default image
    }
}