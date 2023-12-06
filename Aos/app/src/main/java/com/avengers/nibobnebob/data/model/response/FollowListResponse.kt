package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.FollowListData

data class FollowListResponse(
    val nickName: String,
    val region: String,
    val isFollow: Boolean
) : BaseDataModel {
    companion object : DomainMapper<FollowListResponse, FollowListData> {
        override fun FollowListResponse.toDomainModel(): FollowListData = FollowListData(
            nickName = nickName,
            region = region,
            isFollow = isFollow
        )
    }
}
