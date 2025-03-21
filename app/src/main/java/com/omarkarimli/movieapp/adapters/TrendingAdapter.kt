package com.omarkarimli.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.movieapp.databinding.ItemTrendingBinding
import com.omarkarimli.movieapp.domain.models.Article
import com.omarkarimli.movieapp.utils.getTimeAgo
import com.omarkarimli.movieapp.utils.loadFromUrlToImage

class TrendingAdapter : RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    lateinit var onMoreClick: (context: Context, anchoredView: View, article: Article) -> Unit
    lateinit var onItemClick: (article: Article) -> Unit

    private var originalList = arrayListOf<Article>()

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
            imageViewArticle.loadFromUrlToImage(instance.urlToImage)
            textViewSourceName.text = instance.source?.name
            textViewNewsTitle.text = instance.title
            textViewNewsAuthor.text = instance.author
            textViewPublishedAt.text = getTimeAgo(instance.publishedAt!!)

            buttonMore.setOnClickListener { onMoreClick(it.context, it, instance) }
            root.setOnClickListener { onItemClick(instance) }
        }
    }

    fun updateList(newList: List<Article>) {
        originalList.clear()
        originalList.addAll(newList)
        notifyDataSetChanged()
    }
}
