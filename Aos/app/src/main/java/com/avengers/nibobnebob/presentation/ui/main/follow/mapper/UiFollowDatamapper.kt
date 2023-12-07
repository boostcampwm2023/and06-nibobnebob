package com.avengers.nibobnebob.presentation.ui.main.follow.mapper

import com.avengers.nibobnebob.domain.model.FollowListData
import com.avengers.nibobnebob.domain.model.RecommendFollowListData
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowData


internal fun FollowListData.toUiFollowData(
    onFollowBtnClickListener: (String) -> Unit,
    onUnFollowBtnClickListener: (String) -> Unit,
    onRootClickListener: (String) -> Unit
) = UiFollowData(
    nickName = nickName,
    region = region,
    isFollowing = isFollow,
    profileImage = profileImage,
    onFollowBtnClickListener = onFollowBtnClickListener,
    onUnFollowBtnClickListener = onUnFollowBtnClickListener,
    onRootClickListener = onRootClickListener
)

internal fun RecommendFollowListData.toUiFollowData(
    onFollowBtnClickListener: (String) -> Unit,
    onUnFollowBtnClickListener: (String) -> Unit,
    onRootClickListener: (String) -> Unit
) = UiFollowData(
    nickName = nickName,
    region = region,
    isFollowing = isFollow,
    profileImage = profileImage,
    onFollowBtnClickListener = onFollowBtnClickListener,
    onUnFollowBtnClickListener = onUnFollowBtnClickListener,
    onRootClickListener = onRootClickListener
)

