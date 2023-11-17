package com.avengers.nibobnebob.presentation.ui.main.mypage.edit_profile

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.presentation.ui.main.mypage.Validation
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("set_nick_helper_text")
fun TextView.setNickHelperText(state: InputState?) {
    state ?: return

    val validNick = (state.helperText == Validation.VALID_NICK) && state.isValid
    val invalidNick = (state.helperText == Validation.INVALID_NICK) && !state.isValid

    if (validNick) {
        text = resources.getString(R.string.helper_valid_nick)
    } else if (invalidNick) {
        text = resources.getString(R.string.helper_invalid_nick)
        setTextColor(resources.getColor(R.color.nn_rainbow_red, null))
    } else {
        text = ""
    }
}


@BindingAdapter("set_nick_input_layout")
fun TextInputLayout.setNickInputLayoutColor(state: InputState?) {
    state ?: return

    val validNick = (state.helperText == Validation.VALID_NICK) && state.isValid
    val invalidNick = (state.helperText == Validation.INVALID_NICK) && !state.isValid

    boxStrokeColor = resources.getColor(
        if (validNick) R.color.nn_dark1
        else if (invalidNick) R.color.nn_rainbow_red
        else R.color.nn_dark1,
        null
    )
}

@BindingAdapter("set_date_input_style")
fun TextInputLayout.setDateHelperText(state: InputState?) {
    state ?: return

    val invalidCondition = (state.helperText == Validation.INVALID_DATE) && !state.isValid

    if (invalidCondition) {
        helperText = resources.getString(R.string.helper_invalid_date)
        setHelperTextTextAppearance(R.style.ErrorMsgRegular)
        boxStrokeColor = resources.getColor(R.color.nn_rainbow_red, null)
    } else {
        helperText = ""
        boxStrokeColor = resources.getColor(R.color.nn_dark1, null)

    }

}

@BindingAdapter("set_enabled")
fun TextView.setDoneButtonEnable(state: EditProfileUiState?) {
    state ?: return

    val allValid = state.nickName.isValid && state.location.isValid && state.birth.isValid
    val isChanged = state.nickName.isChanged || state.location.isChanged || state.birth.isChanged

    setTextColor(
        if (allValid && isChanged) resources.getColor(R.color.nn_dark1, null)
        else resources.getColor(R.color.nn_dark5, null)
    )
}

