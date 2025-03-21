package com.omarkarimli.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.movieapp.databinding.ItemTrendingBinding
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.utils.loadFromUrlToImage

class TrendingAdapter : RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    lateinit var onMoreClick: (context: Context, anchoredView: View, movie: Movie) -> Unit
    lateinit var onItemClick: (movie: Movie) -> Unit

    private var originalList = arrayListOf<Movie>()

    inner class TrendingViewHolder(val binding: ItemTrendingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val layout = ItemTrendingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TrendingViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val instance = originalList[position]

        holder.binding.apply {
            imageViewMovie.loadFromUrlToImage(instance.posterPath)
            textViewTitle.text = instance.title

            buttonMore.setOnClickListener { onMoreClick(it.context, it, instance) }

            root.setOnClickListener { onItemClick(instance) }
        }
    }

    fun updateList(newList: List<Movie>) {
        originalList.clear()
        originalList.addAll(newList)
        notifyDataSetChanged()
    }
}
