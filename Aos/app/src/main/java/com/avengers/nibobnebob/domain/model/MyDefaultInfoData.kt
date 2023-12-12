package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class MyDefaultInfoData(
    val userInfo: UserEditInfoData
) : BaseDomainModel

data class UserEditInfoData(
    val nickName: String,
    val email: String,
    val provider: String,
    val birthdate: String,
    val region: String,
    val isMale: Boolean,
    val password: Int,
    val profileImage: String
) : BaseDomainModel