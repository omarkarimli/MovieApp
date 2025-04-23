package com.omarkarimli.movieapp.utils.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.omarkarimli.movieapp.domain.models.OnBoardingModel

class OnBoardingDiffCallback : DiffUtil.ItemCallback<OnBoardingModel>() {
    override fun areItemsTheSame(oldItem: OnBoardingModel, newItem: OnBoardingModel): Boolean {
        return oldItem.title == newItem.title // or use unique ID if exists
    }

    override fun areContentsTheSame(oldItem: OnBoardingModel, newItem: OnBoardingModel): Boolean {
        return oldItem == newItem
    }
}