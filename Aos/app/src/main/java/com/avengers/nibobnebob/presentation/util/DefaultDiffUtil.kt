package com.avengers.nibobnebob.presentation.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class DefaultDiffUtil<T> : DiffUtil.ItemCallback<T>(){
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
        return oldItem == newItem
    }
}