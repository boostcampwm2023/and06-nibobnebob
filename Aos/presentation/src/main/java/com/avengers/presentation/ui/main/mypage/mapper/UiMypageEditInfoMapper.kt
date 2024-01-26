package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.domain.model.MyDefaultInfoData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageEditInfoData

fun MyDefaultInfoData.toUiMyPageEditInfoData() = UiMyPageEditInfoData(
    nickName = userInfo.nickName,
    email = userInfo.email,
    provider = userInfo.provider,
    birth = userInfo.birthdate,
    location = userInfo.region,
    gender = userInfo.isMale,
    profileImage = userInfo.profileImage
)