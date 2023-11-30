package com.avengers.nibobnebob.presentation.ui.main.mypage.mylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.ItemMyListBinding
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyListData

class MyRestaurantAdapter(
    private val showDetail: (Int) -> Unit,
    private val deleteItem: (Int) -> Unit
) : ListAdapter<UiMyListData, MyRestaurantViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UiMyListData>() {
            override fun areItemsTheSame(
                oldItem: UiMyListData,
                newItem: UiMyListData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiMyListData,
                newItem: UiMyListData
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
        holder.bind(getItem(position), showDetail, deleteItem)
    }

}

class MyRestaurantViewHolder(
    private val binding: ItemMyListBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UiMyListData, showDetail: (Int) -> Unit, deleteItem: (Int) -> Unit) {
        binding.item = item
        binding.ivMore.setOnClickListener { listMenu(item, showDetail, deleteItem) }
        binding.executePendingBindings()

    }

    private fun listMenu(
        item: UiMyListData,
        showDetail: (Int) -> Unit,
        deleteItem: (Int) -> Unit
    ) {
        val menu = PopupMenu(binding.root.context, binding.ivMore)
        menu.menuInflater.inflate(R.menu.my_page_list_menu, menu.menu)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_detail -> showDetail(item.id)
                R.id.menu_delete -> deleteItem(item.id)
            }
            true
        }
        menu.show()
    }
}