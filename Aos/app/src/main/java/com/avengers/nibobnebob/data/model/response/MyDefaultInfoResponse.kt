package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.data.model.response.UserEditInfo.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.MyDefaultInfoData
import com.avengers.nibobnebob.domain.model.UserEditInfoData
import com.google.gson.annotations.SerializedName

data class MyDefaultInfoResponse(
    @SerializedName("userInfo") val userInfo: UserEditInfo
) : BaseDataModel {
    companion object : DomainMapper<MyDefaultInfoResponse, MyDefaultInfoData> {
        override fun MyDefaultInfoResponse.toDomainModel(): MyDefaultInfoData = MyDefaultInfoData(
            userInfo = userInfo.toDomainModel()
        )
    }
}

data class UserEditInfo(
    @SerializedName("nickName") val nickName: String,
    @SerializedName("email") val email: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("birthdate") val birthdate: String,
    @SerializedName("region") val region: String,
    @SerializedName("isMale") val isMale: Boolean,
    @SerializedName("password") val password: Int,
    @SerializedName("profileImage") val profileImage: String
) : BaseDataModel {
    companion object : DomainMapper<UserEditInfo, UserEditInfoData> {
        override fun UserEditInfo.toDomainModel(): UserEditInfoData = UserEditInfoData(
            nickName = nickName,
            email = email,
            provider = provider,
            birthdate = birthdate,
            region = region,
            isMale = isMale,
            password = password,
            profileImage = profileImage
        )
    }
}
