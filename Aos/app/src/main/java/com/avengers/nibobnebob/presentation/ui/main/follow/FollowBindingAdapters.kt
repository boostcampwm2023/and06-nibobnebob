package com.avengers.nibobnebob.presentation.ui.main.follow

import android.graphics.Color
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R


@BindingAdapter("followState")
fun bindFollowState(btn: AppCompatButton, isFollowing: Boolean){
    if( isFollowing ){
        btn.setBackgroundResource(R.drawable.rect_dark6fill_nostroke_20radius)
        btn.setTextColor(Color.BLACK)
    } else {
        btn.setBackgroundResource(R.drawable.rect_primary4fill_nostroke_20radius)
        btn.setTextColor(Color.WHITE)
    }
}