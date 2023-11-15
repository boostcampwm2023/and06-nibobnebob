package com.avengers.nibobnebob.presentation.ui.main.mypage.edit_profile

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.presentation.ui.intro.signup.bindTextLayoutColor
import com.avengers.nibobnebob.presentation.ui.main.mypage.Validation
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text


@BindingAdapter("set_enabled")
fun TextView.setButtonEnabled(uiState: EditProfileUiState?) {
    uiState ?: return
    uiState.run {
        if(nickName.isValid && location.isValid && birth.isValid) {
            setTextColor(resources.getColor(R.color.nn_dark1,null))
        } else {
            setTextColor(resources.getColor(R.color.nn_dark5,null))
        }
    }
}


@BindingAdapter("set_nick_helper_text")
fun TextView.setNickHelperText(state: InputState?) {
    state ?: return

    if((state.helperText == Validation.VALID_NICK) && state.isValid){
        text = resources.getString(R.string.helper_valid_nick)
    } else if((state.helperText == Validation.INVALID_NICK) && !state.isValid){
        text = resources.getString(R.string.helper_invalid_nick)
        setTextColor(resources.getColor(R.color.nn_rainbow_red, null))
    } else {
        text = ""
    }
}

@BindingAdapter("set_nick_input_layout")
fun TextInputLayout.setNickInputLayoutColor(state: InputState?){
    state ?: return

    boxStrokeColor = if((state.helperText == Validation.VALID_NICK) && state.isValid){
        resources.getColor(R.color.nn_dark1, null)
    } else if((state.helperText == Validation.INVALID_NICK) && !state.isValid){
        resources.getColor(R.color.nn_rainbow_red, null)
    } else {
        resources.getColor(R.color.nn_dark1, null)
    }
}

@BindingAdapter("set_date_input_style")
fun TextInputLayout.setDateHelperText(state: InputState?){
    state ?: return

    val invalidCondition = (state.helperText == Validation.INVALID_DATE) && !state.isValid

    if(invalidCondition){
        helperText = resources.getString(R.string.helper_invalid_date)
        setHelperTextTextAppearance(R.style.ErrorMsgRegular)
        boxStrokeColor = resources.getColor(R.color.nn_rainbow_red, null)
    } else {
        helperText = ""
        boxStrokeColor = resources.getColor(R.color.nn_dark1, null)

    }

}

@BindingAdapter("set_enabled")
fun TextView.setDoneButtonEnable(state : EditProfileUiState?) {
    state ?: return
    if(state.nickName.isValid && state.location.isValid && state.birth.isValid) {
        setTextColor(resources.getColor(R.color.nn_dark1, null))
    } else {
        setTextColor(resources.getColor(R.color.nn_dark5, null))
    }
}

