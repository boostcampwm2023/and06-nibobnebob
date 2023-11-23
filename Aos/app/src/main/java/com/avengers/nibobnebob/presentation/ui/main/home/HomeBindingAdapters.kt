package com.avengers.nibobnebob.presentation.ui.main.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R

@BindingAdapter("filterBackground")
fun bindFilterBackground(tv: TextView, isSelected: Boolean){
    if(isSelected){
        tv.setBackgroundResource(R.drawable.rect_primary6fill_nostroke_30radius)
    } else {
        tv.setBackgroundResource(R.drawable.rect_primary3fill_nostroke_30radius)
    }
}