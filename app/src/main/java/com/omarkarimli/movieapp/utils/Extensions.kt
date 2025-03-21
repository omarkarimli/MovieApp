package com.omarkarimli.movieapp.utils

import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import com.omarkarimli.movieapp.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

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

fun String.getFormattedDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    return try {
        val date = dateFormat.parse(this)
        date?.let { formattedDate.format(it) } ?: "Unknown"
    } catch (e: Exception) {
        "Unknown"
    }
}
