package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageEditInfoData

fun MyDefaultInfoResponse.toUiMyPageEditInfoData() = UiMyPageEditInfoData(
    nickName = userInfo.nickName,
    email = userInfo.email,
    provider = userInfo.provider,
    birth = userInfo.birthdate,
    location = userInfo.region,
    gender = userInfo.isMale,
    profileImage = userInfo.profileImage

)