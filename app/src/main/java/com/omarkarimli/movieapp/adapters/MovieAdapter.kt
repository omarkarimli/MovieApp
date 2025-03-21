package com.omarkarimli.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.movieapp.databinding.ItemMovieBinding
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.utils.loadFromUrlToImage

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ArticleViewHolder>() {

    lateinit var onMoreClick: (context: Context, anchoredView: View, movie: Movie) -> Unit
    lateinit var onItemClick: (movie: Movie) -> Unit

    private var originalList = arrayListOf<Movie>()

    inner class ArticleViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val layout = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ArticleViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
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
