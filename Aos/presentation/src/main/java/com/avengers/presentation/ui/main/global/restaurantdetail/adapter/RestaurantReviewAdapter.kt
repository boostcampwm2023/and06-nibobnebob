package com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiReviewData
import com.avengers.presentation.databinding.ItemReviewListBinding

class RestaurantReviewAdapter : ListAdapter<UiReviewData, ReviewFilterViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UiReviewData>() {
            override fun areItemsTheSame(oldItem: UiReviewData, newItem: UiReviewData): Boolean {
                return oldItem.reviewId == newItem.reviewId
            }

            override fun areContentsTheSame(oldItem: UiReviewData, newItem: UiReviewData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewFilterViewHolder {
        return ReviewFilterViewHolder(
            ItemReviewListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewFilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ReviewFilterViewHolder(
    private val binding: ItemReviewListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiReviewData) {
        binding.item = item
    }
}