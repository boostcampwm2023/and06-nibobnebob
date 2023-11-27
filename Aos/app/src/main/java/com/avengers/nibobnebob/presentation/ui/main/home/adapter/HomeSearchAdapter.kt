package com.avengers.nibobnebob.presentation.ui.main.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.databinding.ItemHomeSearchBinding
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData

class HomeSearchAdapter(
    private val onClickSearchItem: (Int) -> Unit
) : RecyclerView.Adapter<SearchViewHolder>() {

    private var resultList : List<UiRestaurantData> = emptyList()
    private var keyword : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemHomeSearchBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(resultList[position], position, onClickSearchItem, keyword)
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setResultList(list : List<UiRestaurantData>, keyword : String) {
        resultList = list
        this.keyword = keyword
        notifyDataSetChanged()
    }

}

class SearchViewHolder(private val binding: ItemHomeSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UiRestaurantData, position: Int, onClickSearchItem: (Int) -> Unit, keyword : String) {
        with(binding) {
//            val start = item.name.indexOf(keyword)
//            val end = start + keyword.length
//            val colorSpan = ForegroundColorSpan(Color.BLUE)
//
//            val colorChanged = SpannableString(item.name)
//            colorChanged.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            tvResultOne.text = item.name
            tvAddress.text = item.address
            root.setOnClickListener {
                onClickSearchItem(position)
            }
        }
    }
}