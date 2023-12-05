package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class MyInfoData(
    val userInfo: UserInfoData
): BaseDomainModel

data class UserInfoData(
   val nickName: String,
   val birthdate: String,
   val region: String,
   val isMale: Boolean,
   val profileImage: String
): BaseDomainModel