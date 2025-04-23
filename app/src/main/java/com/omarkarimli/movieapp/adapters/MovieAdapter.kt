package com.omarkarimli.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.movieapp.databinding.ItemMovieBinding
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.utils.diffUtils.MovieDiffCallback
import com.omarkarimli.movieapp.utils.loadFromUrlToImage

class MovieAdapter : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    lateinit var onMoreClick: (context: Context, anchoredView: View, movie: Movie) -> Unit
    lateinit var onItemClick: (movie: Movie) -> Unit

    private var originalList = arrayListOf<Movie>()

    inner class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layout = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return originalList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
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
        submitList(newList)
    }
}
