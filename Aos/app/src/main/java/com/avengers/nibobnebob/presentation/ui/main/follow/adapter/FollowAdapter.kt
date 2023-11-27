package com.avengers.nibobnebob.presentation.ui.main.follow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.databinding.ItemFollowerBinding
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowData
import com.avengers.nibobnebob.presentation.util.DefaultDiffUtil

class FollowAdapter : ListAdapter<UiFollowData, FollowViewHolder>(DefaultDiffUtil<UiFollowData>()) {

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        return FollowViewHolder(
            ItemFollowerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }
}

class FollowViewHolder(private val binding: ItemFollowerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiFollowData) {
        binding.item = item
        binding.btnFollow.setOnClickListener {
            if(item.isFollowing){
                item.onUnFollowBtnClickListener(item.nickName)
            } else {
                item.onFollowBtnClickListener(item.nickName)
            }
        }
    }

}