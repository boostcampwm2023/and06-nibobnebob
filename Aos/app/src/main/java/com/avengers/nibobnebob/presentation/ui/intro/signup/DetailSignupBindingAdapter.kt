package com.avengers.nibobnebob.presentation.ui.intro.signup

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("helperMessage")
fun bindHelpMessage(view: TextView, inputState: InputState) {
    when (inputState) {
        is InputState.Success -> {
            view.text = inputState.msg
            view.setTextColor(ContextCompat.getColor(view.context, R.color.nn_primary6))
        }

        is InputState.Error -> {
            view.text = inputState.msg
            view.setTextColor(ContextCompat.getColor(view.context, R.color.nn_rainbow_red))
        }

        else -> {
            view.text = ""
        }
    }
}

@BindingAdapter("textLayoutColor")
fun bindTextLayoutColor(view: TextInputLayout, inputState: InputState) {
    when (inputState) {
        is InputState.Success -> view.boxStrokeColor =
            ContextCompat.getColor(view.context, R.color.nn_primary6)

        is InputState.Error -> view.boxStrokeColor =
            ContextCompat.getColor(view.context, R.color.nn_rainbow_red)

        else -> view.boxStrokeColor = ContextCompat.getColor(view.context, R.color.nn_primary6)
    }
}