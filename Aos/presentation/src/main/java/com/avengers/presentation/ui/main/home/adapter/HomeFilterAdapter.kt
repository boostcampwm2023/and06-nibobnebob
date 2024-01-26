package com.avengers.nibobnebob.presentation.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiFilterData
import com.avengers.presentation.databinding.ItemHomeFilterBinding

class HomeFilterAdapter: ListAdapter<UiFilterData, HomeFilterViewHolder>(diffCallback) {

    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<UiFilterData>(){
            override fun areItemsTheSame(oldItem: UiFilterData, newItem: UiFilterData): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: UiFilterData, newItem: UiFilterData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFilterViewHolder {
        return HomeFilterViewHolder(
            ItemHomeFilterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeFilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HomeFilterViewHolder(
    private val binding: ItemHomeFilterBinding
): RecyclerView.ViewHolder(binding.root){

    fun bind(item: UiFilterData){
        binding.item = item
        binding.tvFilter.text = item.name
    }
}

