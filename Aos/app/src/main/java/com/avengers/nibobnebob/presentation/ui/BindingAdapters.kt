package com.avengers.nibobnebob.presentation.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("list")
fun <T> bindList(recyclerView: RecyclerView, list: List<T>) {
    val adapter = recyclerView.adapter as ListAdapter<T, RecyclerView.ViewHolder>
    adapter.submitList(list)
}