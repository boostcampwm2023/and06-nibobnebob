package com.avengers.data.model.response

import com.avengers.data.model.base.BaseDataModel
import com.avengers.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.RecommendFollowListData
import com.google.gson.annotations.SerializedName

data class RecommendFollowListResponse(
    @SerializedName("user_nickName")
    val nickName: String,
    @SerializedName("user_region")
    val region: String,
    @SerializedName("isFollow")
    val isFollow: Boolean,
    @SerializedName("user_profileImage")
    val profileImage: String
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<RecommendFollowListResponse, RecommendFollowListData> {
        override fun RecommendFollowListResponse.toDomainModel(): RecommendFollowListData =
            RecommendFollowListData(
                nickName = nickName,
                region = region,
                isFollow = isFollow,
                profileImage = profileImage
            )
    }
}
