package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class LoginData(
    val accessToken: String?,
    val refreshToken: String?
) : BaseDomainModel
