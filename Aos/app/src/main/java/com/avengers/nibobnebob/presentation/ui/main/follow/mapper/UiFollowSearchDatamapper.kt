package com.avengers.nibobnebob.presentation.ui.main.follow.mapper

import com.avengers.nibobnebob.domain.model.FollowListData
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowSearchData

internal fun FollowListData.toUiFollowSearchData(
    onRootClickListener: (String) -> Unit
) = UiFollowSearchData(
    nickName = nickName,
    region = region,
    onRootClickListener = onRootClickListener
)