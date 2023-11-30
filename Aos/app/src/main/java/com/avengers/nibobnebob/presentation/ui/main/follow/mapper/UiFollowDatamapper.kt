package com.avengers.nibobnebob.presentation.ui.main.follow.mapper

import com.avengers.nibobnebob.data.model.response.FollowListResponse
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowData


internal fun FollowListResponse.toUiFollowData(
    onFollowBtnClickListener: (String) -> Unit,
    onUnFollowBtnClickListener: (String) -> Unit,
    onRootClickListener: (String) -> Unit
) = UiFollowData(
    nickName = nickName,
    region = region,
    isFollowing = isFollow,
    onFollowBtnClickListener = onFollowBtnClickListener,
    onUnFollowBtnClickListener = onUnFollowBtnClickListener,
    onRootClickListener = onRootClickListener
)