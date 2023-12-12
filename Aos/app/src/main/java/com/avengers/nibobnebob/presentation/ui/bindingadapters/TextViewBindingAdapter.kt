package com.avengers.nibobnebob.presentation.ui.bindingadapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.presentation.ui.intro.signup.InputState
import com.avengers.nibobnebob.presentation.ui.main.global.restaurantadd.CommentState
import com.avengers.nibobnebob.presentation.ui.main.mypage.Validation
import com.avengers.nibobnebob.presentation.ui.main.mypage.edit.EditInputState
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_BEST
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_NEW
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_OLD
import com.avengers.nibobnebob.presentation.util.Constants.FILTER_WORST
import com.avengers.nibobnebob.presentation.util.LoginType
import com.google.android.material.textfield.TextInputLayout

// signup
@BindingAdapter("helperMessage")
fun bindHelpMessage(tv: TextView, inputState: InputState) = with(tv) {
    when (inputState) {
        is InputState.Success -> {
            text = inputState.msg
            setTextColor(ContextCompat.getColor(context, R.color.nn_primary6))
        }

        is InputState.Error -> {
            text = inputState.msg
            setTextColor(ContextCompat.getColor(context, R.color.nn_rainbow_red))
        }

        else -> {
            text = ""
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("textLength", "textLimit")
fun bindTextLength(tv: TextView, text: String, limit: Int) {
    tv.text = "(${text.length}/$limit)"
}

@BindingAdapter("commentHelperMessage")
fun bindCommentHelperMessage(tv: TextView, state: CommentState) = with(tv) {
    when (state) {
        is CommentState.Over -> {
            text = state.msg
            setTextColor(Color.RED)
        }

        is CommentState.Lack -> {
            text = state.msg
            setTextColor(Color.RED)
        }

        is CommentState.Success -> {
            text = state.msg
            setTextColor(Color.BLACK)
        }

        else -> visibility = View.INVISIBLE
    }
}

@BindingAdapter("setNickHelperText")
fun bindNickHelperText(tv: TextView, state: EditInputState?) = with(tv) {
    state ?: return

    val validNick = (state.helperText == Validation.VALID_NICK) && state.isValid
    val invalidNick = (state.helperText == Validation.INVALID_NICK) && !state.isValid

    if (validNick) {
        text = resources.getString(R.string.helper_valid_nick)
        setTextColor(resources.getColor(R.color.nn_dark1, null))
    } else if (invalidNick) {
        text = resources.getString(R.string.helper_invalid_nick)
        setTextColor(resources.getColor(R.color.nn_rainbow_red, null))
    } else {
        text = ""
    }
}

@BindingAdapter("loginType")
fun bindLoginType(tv: TextView, type: String?) = with(tv) {
    type ?: return
    text = if (type == LoginType.NAVER_LOGIN) "네이버 소셜로그인" else "일반 로그인"
}

@BindingAdapter("filterType")
fun bindFilterType(tv: TextView, type: String) = with(tv) {
    text = when (type) {
        FILTER_NEW -> "최신순"
        FILTER_OLD -> "오래된순"
        FILTER_BEST -> "추천순"
        FILTER_WORST -> "비추천순"
        else -> ""
    }
}

@BindingAdapter("adjustText")
fun bindLongText(tv: TextView, value: String) {
    tv.text = if (value.length > 27) "${value.substring(0, 27)}.." else value
}
