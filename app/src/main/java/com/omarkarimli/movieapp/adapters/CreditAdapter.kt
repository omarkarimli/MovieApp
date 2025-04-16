package com.omarkarimli.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarkarimli.movieapp.databinding.ItemCreditBinding
import com.omarkarimli.movieapp.domain.models.Cast
import com.omarkarimli.movieapp.utils.loadFromUrlToImage

class CreditAdapter: RecyclerView.Adapter<CreditAdapter.CreditViewHolder>() {
    private val originalList = mutableListOf<Cast?>()

    inner class CreditViewHolder(val binding: ItemCreditBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        val binding = ItemCreditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditViewHolder(binding)
    }

    override fun getItemCount(): Int = originalList.size

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        val item = originalList[position]
        if (item != null) {
            holder.binding.apply {
                textViewName.text = item.name
                textViewCharacter.text = item.character
                imageView.loadFromUrlToImage(item.profilePath)
            }
        }
    }

    fun updateList(newList: List<Cast>) {
        originalList.clear()
        originalList.addAll(newList)
        notifyDataSetChanged()
    }
}