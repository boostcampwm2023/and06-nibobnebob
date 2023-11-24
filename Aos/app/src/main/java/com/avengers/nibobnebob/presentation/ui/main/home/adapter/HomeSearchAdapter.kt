package com.avengers.nibobnebob.presentation.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.databinding.ItemHomeSearchBinding
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiSearchResultData

class HomeSearchAdapter :
    ListAdapter<UiSearchResultData, HomeSearchViewHolder>(HomeSearchDiffUtil()) {
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
        holder.bind(getItem(position))
    }

}

class HomeSearchViewHolder(private val binding: ItemHomeSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UiSearchResultData) {
        with(binding) {
            tvResultOne.text = item.name
            tvAddress.text = item.address
        }
    }
}

class HomeSearchDiffUtil : DiffUtil.ItemCallback<UiSearchResultData>() {
    override fun areItemsTheSame(
        oldItem: UiSearchResultData,
        newItem: UiSearchResultData
    ): Boolean = (oldItem.id == newItem.id)

    override fun areContentsTheSame(
        oldItem: UiSearchResultData,
        newItem: UiSearchResultData
    ): Boolean = (oldItem == newItem)
}