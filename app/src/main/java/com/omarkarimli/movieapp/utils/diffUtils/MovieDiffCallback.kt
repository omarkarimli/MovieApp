package com.omarkarimli.movieapp.utils.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.omarkarimli.movieapp.domain.models.Movie

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id // Compare unique ID
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem // Compare contents
    }
}
