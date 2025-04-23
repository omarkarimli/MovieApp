package com.omarkarimli.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.movieapp.databinding.ItemGenreBinding
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.utils.diffUtils.GenreDiffCallback
import com.omarkarimli.movieapp.utils.getThemeColor

class GenreAdapter : ListAdapter<GenreModel, GenreAdapter.GenreViewHolder>(GenreDiffCallback()) {

    lateinit var onItemClick: (GenreModel) -> Unit

    private var originalList = arrayListOf<GenreModel>()

    inner class GenreViewHolder(val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val layout = ItemGenreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GenreViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val instance = originalList[position]

        holder.binding.apply {
            val context = root.context

            root.text = instance.name

            if (instance.isSelected) {
                root.isEnabled = false

                root.setBackgroundColor(context.getThemeColor(com.google.android.material.R.attr.colorPrimary))
                root.setTextColor(context.getThemeColor(com.google.android.material.R.attr.colorOnPrimary))
            } else {
                root.isEnabled = true

                root.setBackgroundColor(context.getThemeColor(com.google.android.material.R.attr.colorSurface))
                root.setTextColor(context.getThemeColor(com.google.android.material.R.attr.colorOnSurface))
            }

            root.setOnClickListener { onItemClick(instance) }
        }
    }

    fun updateList(newList: List<GenreModel>) {
        originalList.clear()
        originalList.addAll(newList)
        submitList(newList)
    }
}
