package com.omarkarimli.movieapp.utils

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.omarkarimli.movieapp.R
import com.squareup.picasso.Picasso

fun View.visibleItem() {
    this.visibility = View.VISIBLE
}

fun View.goneItem() {
    this.visibility = View.GONE
}

fun ImageView.loadFromUrlToImage(urlToImage: String?) {
    Picasso.get()
        .load(Constants.POSTER_URL_PREFIX + urlToImage)
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.error_image)
        .into(this)
}
