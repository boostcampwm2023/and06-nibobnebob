package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class RecommendFollowListData(
    val nickName: String,
    val region: String,
    val isFollow: Boolean,
    val profileImage: String
): BaseDomainModel
