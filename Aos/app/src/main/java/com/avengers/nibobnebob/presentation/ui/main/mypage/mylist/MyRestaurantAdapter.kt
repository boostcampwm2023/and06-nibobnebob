package com.avengers.nibobnebob.presentation.ui.main.mypage.mylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.databinding.ItemMyListBinding
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageListData

class MyRestaurantAdapter(
    private val onClickMenu: (Int) -> Unit
) : ListAdapter<UiMyPageListData, MyRestaurantViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UiMyPageListData>() {
            override fun areItemsTheSame(
                oldItem: UiMyPageListData,
                newItem: UiMyPageListData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiMyPageListData,
                newItem: UiMyPageListData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRestaurantViewHolder {
        return MyRestaurantViewHolder(
            ItemMyListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyRestaurantViewHolder, position: Int) {
        holder.bind(getItem(position), onClickMenu)
    }

}

class MyRestaurantViewHolder(
    private val binding: ItemMyListBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UiMyPageListData, onClickMenu: (Int) -> Unit) {
        binding.item = item
//        binding.tvTitle.text = item.name
//        binding.tvAddress.text = item.address
        binding.executePendingBindings()
        binding.root.setOnClickListener {
            onClickMenu(item.id)
        }
    }
}