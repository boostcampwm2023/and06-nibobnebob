package com.avengers.data.model.response

import com.avengers.data.model.base.BaseDataModel
import com.avengers.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.LoginData
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?
) : com.avengers.data.model.base.BaseDataModel {
    companion object :
        com.avengers.data.model.mapper.DomainMapper<com.avengers.data.model.response.LoginResponse, LoginData> {
        override fun com.avengers.data.model.response.LoginResponse.toDomainModel(): LoginData = LoginData(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
