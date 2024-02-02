package com.avengers.data.model.response

import com.avengers.data.model.base.BaseDataModel
import com.avengers.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.FollowListData

data class FollowListResponse(
    val nickName: String,
    val region: String,
    val isFollow: Boolean,
    val profileImage: String
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<com.avengers.data.model.response.FollowListResponse, FollowListData> {
        override fun com.avengers.data.model.response.FollowListResponse.toDomainModel(): FollowListData = FollowListData(
            nickName = nickName,
            region = region,
            isFollow = isFollow,
            profileImage = profileImage
        )
    }
}
