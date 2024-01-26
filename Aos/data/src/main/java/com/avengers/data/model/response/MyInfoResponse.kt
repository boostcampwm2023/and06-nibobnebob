package com.avengers.data.model.response

import com.avengers.data.model.base.BaseDataModel
import com.avengers.data.model.mapper.DomainMapper
import com.avengers.data.model.response.UserInfo.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.MyInfoData
import com.avengers.nibobnebob.domain.model.UserInfoData
import com.google.gson.annotations.SerializedName


data class MyInfoResponse(
    @SerializedName("userInfo") val userInfo: com.avengers.data.model.response.UserInfo
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<com.avengers.data.model.response.MyInfoResponse, MyInfoData> {
        override fun com.avengers.data.model.response.MyInfoResponse.toDomainModel(): MyInfoData = MyInfoData(
            userInfo = userInfo.toDomainModel()
        )
    }
}

data class UserInfo(
    @SerializedName("nickName") val nickName: String,
    @SerializedName("birthdate") val birthdate: String,
    @SerializedName("region") val region: String,
    @SerializedName("isMale") val isMale: Boolean,
    @SerializedName("profileImage") val profileImage: String
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<com.avengers.data.model.response.UserInfo, UserInfoData> {
        override fun com.avengers.data.model.response.UserInfo.toDomainModel(): UserInfoData = UserInfoData(
            nickName = nickName,
            birthdate = birthdate,
            region = region,
            isMale = isMale,
            profileImage = profileImage
        )
    }

}

