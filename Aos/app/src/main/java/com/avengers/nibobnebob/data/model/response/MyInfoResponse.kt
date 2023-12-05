package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.data.model.response.UserInfo.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.MyInfoData
import com.avengers.nibobnebob.domain.model.UserInfoData
import com.google.gson.annotations.SerializedName


data class MyInfoResponse(
    @SerializedName("userInfo") val userInfo: UserInfo
): BaseDataModel {
    companion object : DomainMapper<MyInfoResponse, MyInfoData> {
        override fun MyInfoResponse.toDomainModel(): MyInfoData  = MyInfoData(
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
) : BaseDataModel {
    companion object : DomainMapper<UserInfo, UserInfoData> {
        override fun UserInfo.toDomainModel(): UserInfoData = UserInfoData(
            nickName = nickName,
            birthdate = birthdate,
            region = region,
            isMale = isMale,
            profileImage = profileImage
        )
    }
}

