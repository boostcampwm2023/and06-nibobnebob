package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageInfoData

internal fun MyInfoResponse.toUiMyPageInfoData() = UiMyPageInfoData(
    nickName = userInfo.nickName,
    age = userInfo.birthdate,
    location = userInfo.region,
    gender = if(userInfo.isMale) "남" else "여"
)