package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.LoginData
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?
) : BaseDataModel {
    companion object : DomainMapper<LoginResponse, LoginData> {
        override fun LoginResponse.toDomainModel(): LoginData = LoginData(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
