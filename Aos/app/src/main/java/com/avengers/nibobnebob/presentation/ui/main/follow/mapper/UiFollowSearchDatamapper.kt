package com.avengers.nibobnebob.presentation.ui.main.follow.mapper

import com.avengers.nibobnebob.data.model.response.FollowListResponse
import com.avengers.nibobnebob.presentation.ui.main.follow.model.UiFollowSearchData

internal fun FollowListResponse.toUiFollowSearchData(
    onRootClickListener: (String) -> Unit
) = UiFollowSearchData(
    nickName = nickName,
    region = region,
    onRootClickListener = onRootClickListener
)