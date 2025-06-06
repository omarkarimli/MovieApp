package com.omarkarimli.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.movieapp.databinding.ItemOnboardingBinding
import com.omarkarimli.movieapp.domain.models.OnBoardingModel
import com.omarkarimli.movieapp.utils.diffUtils.OnBoardingDiffCallback

class OnBoardingAdapter : ListAdapter<OnBoardingModel, OnBoardingAdapter.OnBoardingViewHolder>(OnBoardingDiffCallback()) {

    private var originalList = arrayListOf<OnBoardingModel>()

    inner class OnBoardingViewHolder(val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val layout = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OnBoardingViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        val instance = originalList[position]

        holder.binding.apply {
            textViewTitle.text = instance.title
            textViewDescription.text = instance.description
            imageView.setImageResource(instance.imageRes)
        }
    }

    fun updateList(newList: List<OnBoardingModel>) {
        originalList.clear()
        originalList.addAll(newList)
        submitList(newList)
    }
}
