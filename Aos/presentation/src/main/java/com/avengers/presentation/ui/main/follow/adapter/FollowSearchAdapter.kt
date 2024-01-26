package com.avengers.nibobnebob.presentation.ui.main.follow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowSearchData
import com.avengers.presentation.databinding.ItemFollowSearchBinding

class FollowSearchAdapter() : ListAdapter<UiFollowSearchData,FollowSearchViewHolder>(diffCallback) {

    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<UiFollowSearchData>(){
            override fun areItemsTheSame(oldItem: UiFollowSearchData, newItem: UiFollowSearchData): Boolean {
                return oldItem.nickName == newItem.nickName
            }

            override fun areContentsTheSame(oldItem: UiFollowSearchData, newItem: UiFollowSearchData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: FollowSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowSearchViewHolder {
        return FollowSearchViewHolder(
            ItemFollowSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }
}

class FollowSearchViewHolder(private val binding: ItemFollowSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiFollowSearchData) {
        binding.item = item
    }
}