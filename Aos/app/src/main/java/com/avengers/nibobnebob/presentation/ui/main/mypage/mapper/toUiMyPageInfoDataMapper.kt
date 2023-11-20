package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.MyPageInfoResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageInfoData

internal fun MyPageInfoResponse.toUiMyPageInfoData() = UiMyPageInfoData(
    nickName = data.userInfo.nickName,
    age = data.userInfo.birthdate,
    location = data.userInfo.region,
    gender = if(data.userInfo.isMale) "남" else "여"
)