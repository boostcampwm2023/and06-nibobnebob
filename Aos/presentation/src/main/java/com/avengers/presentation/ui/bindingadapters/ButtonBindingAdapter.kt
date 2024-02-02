package com.avengers.nibobnebob.presentation.ui.bindingadapters

import android.graphics.Color
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter

import com.avengers.nibobnebob.presentation.ui.main.mypage.edit.EditProfileUiState
import com.avengers.presentation.R


@BindingAdapter("setBtnEnable")
fun bindDoneButtonEnable(btn: AppCompatButton, state: EditProfileUiState?) = with(btn) {
    state ?: return

    val allValid = state.nickName.isValid && state.location.isValid && state.birth.isValid
    val isChanged =
        state.nickName.isChanged || state.location.isChanged || state.birth.isChanged || state.profileImage.isChanged || state.isMale.isChanged

    isEnabled = allValid && isChanged
}

@BindingAdapter("addRestaurantState")
fun bindAddRestaurantStatus(btn: AppCompatButton, isMy: Boolean) = with(btn) {
    setBackgroundResource(if (isMy) R.drawable.rect_dark6fill_nostroke_20radius else R.drawable.rect_primary4fill_nostroke_20radius)
    setTextColor(if (isMy) Color.BLACK else Color.WHITE)
    text =
        context.getString(if (isMy) R.string.delete_my_restaurant else R.string.add_my_list_review)
}

@BindingAdapter("followState")
fun bindFollowState(btn: AppCompatButton, isFollowing: Boolean) = with(btn) {
    setBackgroundResource(if (isFollowing) R.drawable.rect_dark6fill_nostroke_20radius else R.drawable.rect_primary4fill_nostroke_20radius)
    setTextColor(if (isFollowing) Color.BLACK else Color.WHITE)
    text = context.getString(if (isFollowing) R.string.unfollow_label else R.string.follow_label)
}