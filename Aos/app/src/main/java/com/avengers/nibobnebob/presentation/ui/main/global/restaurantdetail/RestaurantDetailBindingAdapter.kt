package com.avengers.nibobnebob.presentation.ui.main.global.restaurantdetail

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R


@BindingAdapter("filterRateBackground")
fun bindFilterBackground(iv: ImageView, rate : Int){
    when(rate){
        0 -> {iv.setBackgroundResource(R.drawable.oval_primary1fill_nostroke)}
        1 -> {iv.setBackgroundResource(R.drawable.oval_primary2fill_nostroke)}
        2 -> {iv.setBackgroundResource(R.drawable.oval_primary3fill_nostroke)}
        3 -> {iv.setBackgroundResource(R.drawable.oval_primary4fill_nostroke)}
        4 -> {iv.setBackgroundResource(R.drawable.oval_primary5fill_nostroke)}
        else -> {iv.setBackgroundResource(R.drawable.oval_primary1fill_nostroke)}
    }
}

@BindingAdapter("thumbsUpStatus")
fun setThumbsUpStatus(iv: ImageView, isThumbsUp:Boolean){
    if(isThumbsUp){
        iv.setImageResource(R.drawable.ic_thumbs_up_fill)
    }else{
        iv.setImageResource(R.drawable.ic_thumbs_up_blank)
    }
}

@BindingAdapter("thumbsDownStatus")
fun setThumbsDownStatus(iv: ImageView, isThumbsDown:Boolean){
    if(isThumbsDown){
        iv.setImageResource(R.drawable.ic_thumbs_down_fill)
    }else{
        iv.setImageResource(R.drawable.ic_thumbs_down_blank)
    }
}

@BindingAdapter("wishStatus")
fun setWishStatus(iv:ImageView, isWish : Boolean){
    if(isWish){
        iv.setImageResource(R.drawable.ic_star_full)
    }else{
        iv.setImageResource(R.drawable.ic_star_border)
    }
}
