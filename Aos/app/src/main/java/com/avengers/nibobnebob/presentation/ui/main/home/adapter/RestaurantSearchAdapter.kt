package com.avengers.nibobnebob.presentation.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.databinding.ItemHomeSearchBinding
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData

class RestaurantSearchAdapter(private val onClickSearchItem: (Int) -> Unit) :
    ListAdapter<UiRestaurantData, HomeSearchViewHolder>(HomeSearchDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSearchViewHolder {
        return HomeSearchViewHolder(
            ItemHomeSearchBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeSearchViewHolder, position: Int) {
        holder.bind(getItem(position), position, onClickSearchItem)
    }


}

class HomeSearchViewHolder(private val binding: ItemHomeSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UiRestaurantData, position: Int, onClickSearchItem: (Int) -> Unit) {
        with(binding) {
            tvResultOne.text = item.name
            tvAddress.text = item.address
            root.setOnClickListener {
                onClickSearchItem(position)
            }
        }
    }
}

class HomeSearchDiffUtil : DiffUtil.ItemCallback<UiRestaurantData>() {
    override fun areItemsTheSame(
        oldItem: UiRestaurantData,
        newItem: UiRestaurantData
    ): Boolean = (oldItem.id == newItem.id)

    override fun areContentsTheSame(
        oldItem: UiRestaurantData,
        newItem: UiRestaurantData
    ): Boolean = (oldItem == newItem)
}