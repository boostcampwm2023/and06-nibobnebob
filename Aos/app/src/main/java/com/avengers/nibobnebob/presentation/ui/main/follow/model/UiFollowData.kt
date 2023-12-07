package com.avengers.nibobnebob.presentation.ui.main.follow.model

data class UiFollowData(
    val nickName: String = "",
    val region: String = "",
    val isFollowing: Boolean = true,
    val profileImage: String = "",
    val onFollowBtnClickListener: (String) -> Unit,
    val onUnFollowBtnClickListener: (String) -> Unit,
    val onRootClickListener: (String) -> Unit
)
