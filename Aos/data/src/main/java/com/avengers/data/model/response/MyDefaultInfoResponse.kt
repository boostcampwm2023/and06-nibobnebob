package com.avengers.data.model.response

import com.avengers.data.model.base.BaseDataModel
import com.avengers.data.model.mapper.DomainMapper
import com.avengers.data.model.response.UserEditInfo.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.MyDefaultInfoData
import com.avengers.nibobnebob.domain.model.UserEditInfoData
import com.google.gson.annotations.SerializedName

data class MyDefaultInfoResponse(
    @SerializedName("userInfo") val userInfo: com.avengers.data.model.response.UserEditInfo
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<com.avengers.data.model.response.MyDefaultInfoResponse, MyDefaultInfoData> {
        override fun com.avengers.data.model.response.MyDefaultInfoResponse.toDomainModel(): MyDefaultInfoData = MyDefaultInfoData(
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
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<com.avengers.data.model.response.UserEditInfo, UserEditInfoData> {
        override fun com.avengers.data.model.response.UserEditInfo.toDomainModel(): UserEditInfoData = UserEditInfoData(
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
