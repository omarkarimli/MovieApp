package com.omarkarimli.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.movieapp.databinding.ItemGenreBinding
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.utils.goneItem
import com.omarkarimli.movieapp.utils.visibleItem

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

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
            textViewName.text = instance.name

            if (instance.isSelected) {
                root.isEnabled = false
                divider.visibleItem()
            } else {
                root.isEnabled = true
                divider.goneItem()
            }

            root.setOnClickListener { onItemClick(instance) }
        }
    }

    fun updateList(newList: List<GenreModel>) {
        originalList.clear()
        originalList.addAll(newList)
        notifyDataSetChanged()
    }
}
