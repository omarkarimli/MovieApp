package com.omarkarimli.movieapp.utils.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.omarkarimli.movieapp.domain.models.Cast

class CastDiffCallback : DiffUtil.ItemCallback<Cast>() {
    override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
        return oldItem == newItem
    }
}