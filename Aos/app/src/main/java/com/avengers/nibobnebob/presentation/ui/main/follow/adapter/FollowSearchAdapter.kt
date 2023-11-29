package com.avengers.nibobnebob.presentation.ui.main.follow.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.databinding.ItemFollowSearchBinding
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowSearchData

class FollowSearchAdapter() : RecyclerView.Adapter<FollowSearchViewHolder>() {


    private var data : List<UiFollowSearchData> = emptyList()

    override fun onBindViewHolder(holder: FollowSearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowSearchViewHolder {
        return FollowSearchViewHolder(
            ItemFollowSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list : List<UiFollowSearchData>){
        data = list
        notifyDataSetChanged()
    }
}

class FollowSearchViewHolder(private val binding: ItemFollowSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiFollowSearchData) {
        binding.item = item
    }

}