package com.avengers.nibobnebob.presentation.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avengers.nibobnebob.R

@BindingAdapter("list")
fun <T> bindList(recyclerView: RecyclerView, list: List<T>) {
    val adapter = recyclerView.adapter as ListAdapter<T, RecyclerView.ViewHolder>
    adapter.submitList(list)
}

@BindingAdapter("star")
fun setWishStar(iv : ImageView, isWish : Boolean){
    iv.setBackgroundResource(
        if(isWish) R.drawable.ic_star_full else R.drawable.ic_star_border
    )
}