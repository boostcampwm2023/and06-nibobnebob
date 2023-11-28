package com.avengers.nibobnebob.presentation.ui.main.global.restaurantadd

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@SuppressLint("SetTextI18n")
@BindingAdapter("textLength", "textLimit")
fun bindTextLength(tv: TextView, text: String, limit: Int) {
    tv.text = "(${text.length}/$limit)"
}

@BindingAdapter("parkingSpaceViewState")
fun bindParkingSpaceViewState(v: View, state: Boolean) {
    if (state) {
        v.visibility = View.VISIBLE
    } else {
        v.visibility = View.INVISIBLE
    }
}

@BindingAdapter("trafficViewState")
fun bindTrafficViewState(v: View, state: Boolean) {
    if (state) v.visibility = View.INVISIBLE
    else v.visibility = View.VISIBLE
}

@BindingAdapter("commentHelperMessage")
fun bindCommentHelperMessage(tv: TextView, state: CommentState) {
    when (state) {
        is CommentState.Over -> {
            tv.text = state.msg
            tv.setTextColor(Color.RED)
        }

        is CommentState.Lack -> {
            tv.text = state.msg
            tv.setTextColor(Color.RED)
        }

        is CommentState.Success -> {
            tv.text = state.msg
            tv.setTextColor(Color.BLACK)
        }

        else -> tv.visibility = View.INVISIBLE
    }
}