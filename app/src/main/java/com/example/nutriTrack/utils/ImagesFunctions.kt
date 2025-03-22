package com.example.nutriTrack.utils

import android.widget.ImageView
import com.example.nutriTrack.R
import com.squareup.picasso.Picasso

fun loadImageIntoImageView(imageView: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Picasso.get()
            .load(imageUrl) // Load from Firebase Storage URL
            .placeholder(R.drawable.ic_profile) // Optional placeholder
            .into(imageView) // Set into ImageView
    } else {
        imageView.setImageResource(R.drawable.ic_profile) // Default image
    }
}