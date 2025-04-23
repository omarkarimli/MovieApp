package com.omarkarimli.movieapp.utils.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.omarkarimli.movieapp.domain.models.GenreModel

class GenreDiffCallback : DiffUtil.ItemCallback<GenreModel>() {
    override fun areItemsTheSame(oldItem: GenreModel, newItem: GenreModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GenreModel, newItem: GenreModel): Boolean {
        return oldItem == newItem
    }
}