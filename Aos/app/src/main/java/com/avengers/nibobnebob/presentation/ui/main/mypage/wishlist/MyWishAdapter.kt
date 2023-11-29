package com.avengers.nibobnebob.presentation.ui.main.mypage.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.ItemWishListBinding
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyWishData

class MyWishAdapter(
    private val toggleWish: (Int) -> Unit,
    private val showDetail: (Int) -> Unit,
    private val addItem: (Int) -> Unit
) : ListAdapter<UiMyWishData, MyWishViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UiMyWishData>() {
            override fun areItemsTheSame(
                oldItem: UiMyWishData,
                newItem: UiMyWishData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiMyWishData,
                newItem: UiMyWishData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWishViewHolder {
        return MyWishViewHolder(
            ItemWishListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyWishViewHolder, position: Int) {
        holder.bind(getItem(position), toggleWish, showDetail, addItem)
    }

}

class MyWishViewHolder(
    private val binding: ItemWishListBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: UiMyWishData,
        toggleWish: (Int) -> Unit,
        showDetail: (Int) -> Unit,
        addItem: (Int) -> Unit
    ) {
        binding.item = item
        binding.ivMore.setOnClickListener { listMenu(item, showDetail, addItem) }
        binding.ivStar.setOnClickListener { toggleWish(item.id) }
        binding.executePendingBindings()

    }

    private fun listMenu(
        item: UiMyWishData,
        showDetail: (Int) -> Unit,
        addItem: (Int) -> Unit
    ) {
        val menu = PopupMenu(binding.root.context, binding.ivMore)
        menu.menuInflater.inflate(R.menu.my_page_list_menu, menu.menu)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_detail -> showDetail(item.id)
                R.id.menu_delete -> addItem(item.id)
            }
            true
        }
        menu.show()
    }
}