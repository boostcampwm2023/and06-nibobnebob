package com.avengers.nibobnebob.data.model.response

data class FollowSearchResponse(
    val userInfo: List<FollowSearchItem>
)

data class FollowSearchItem(
    val nickName: String,
    val region: String,
    val isFollow: Boolean
)
