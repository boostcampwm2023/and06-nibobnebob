package com.avengers.nibobnebob.presentation.ui.main.global.userdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiUserDetailRestaurantData
import com.avengers.nibobnebob.presentation.util.DefaultDiffUtil
import com.avengers.presentation.databinding.ItemUserDetailRestaurantBinding

class UserDetailRestaurantAdapter(
    private val restaurantClick: (Int) -> Unit
) :
    ListAdapter<UiUserDetailRestaurantData, UserDetailRestaurantViewHolder>(DefaultDiffUtil<UiUserDetailRestaurantData>()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserDetailRestaurantViewHolder {
        return UserDetailRestaurantViewHolder(
            ItemUserDetailRestaurantBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserDetailRestaurantViewHolder, position: Int) {
        holder.bind(getItem(position), restaurantClick)
    }

}

class UserDetailRestaurantViewHolder(private val binding: ItemUserDetailRestaurantBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: UiUserDetailRestaurantData,
        restaurantClick: (Int) -> Unit
    ) {
        binding.item = item
        binding.root.setOnClickListener { restaurantClick(item.id) }
    }
}