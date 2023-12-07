package com.avengers.nibobnebob.presentation.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.databinding.ItemRecommendRestaurantBinding
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRecommendRestaurantData
import com.bumptech.glide.Glide

class HomeRecommendAdapter(
    private val itemClickListener: (Int) -> Unit
) :
    ListAdapter<UiRecommendRestaurantData, RecommendRestaurantViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UiRecommendRestaurantData>() {
            override fun areItemsTheSame(
                oldItem: UiRecommendRestaurantData,
                newItem: UiRecommendRestaurantData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiRecommendRestaurantData,
                newItem: UiRecommendRestaurantData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendRestaurantViewHolder {
        return RecommendRestaurantViewHolder(
            ItemRecommendRestaurantBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecommendRestaurantViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            itemClickListener.invoke(currentItem.id)
        }
    }
}

class RecommendRestaurantViewHolder(
    private val binding: ItemRecommendRestaurantBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiRecommendRestaurantData) {
        binding.item = item
        Glide.with(itemView)
            .load(item.reviewImage)
            .override(200,200)
            .centerCrop()
            .into(binding.ivRestaurantImage)
    }
}