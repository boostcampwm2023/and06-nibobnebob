package com.avengers.nibobnebob.presentation.ui.bindingadapters

import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.presentation.ui.intro.signup.InputState
import com.avengers.nibobnebob.presentation.ui.main.mypage.Validation
import com.avengers.nibobnebob.presentation.ui.main.mypage.edit.EditInputState
import com.google.android.material.textfield.TextInputLayout


// signup
@BindingAdapter("textLayoutColor")
fun bindTextLayoutColor(til: TextInputLayout, inputState: InputState) = with(til) {
    when (inputState) {
        is InputState.Success -> boxStrokeColor = resources.getColor(R.color.nn_primary6, null)
        is InputState.Error -> boxStrokeColor = resources.getColor(R.color.nn_rainbow_red, null)
        else -> til.boxStrokeColor = resources.getColor(R.color.nn_primary6, null)
    }
}

@BindingAdapter("nickLayoutColor")
fun bindNickInputLayoutColor(til: TextInputLayout, state: EditInputState?) = with(til) {
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

@BindingAdapter("dateLayoutStyle")
fun bindDateHelperText(til: TextInputLayout, state: EditInputState?) = with(til) {
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