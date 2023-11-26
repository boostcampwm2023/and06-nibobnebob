package com.avengers.nibobnebob.presentation.ui.global.restaurantadd

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@SuppressLint("SetTextI18n")
@BindingAdapter("textLength", "textLimit")
fun bindTextLength(view: TextView, text: String, limit: Int) {
    view.text = "(${text.length}/$limit)"
}

@BindingAdapter("parkingSpaceViewState")
fun bindParkingSpaceViewState(view: View, state: Boolean){
    if(state){
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.INVISIBLE
    }
}

@BindingAdapter("trafficViewState")
fun bindTrafficViewState(view: View, state: Boolean){
    if(state){
        view.visibility = View.INVISIBLE
    } else {
        view.visibility = View.VISIBLE
    }
}