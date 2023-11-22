package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.Base
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageInfoData

internal fun Base.toUiMyPageInfoData() = UiMyPageInfoData(
    nickName = data.userInfo.nickName,
    age = data.userInfo.birthdate,
    location = data.userInfo.region,
    gender = if(data.userInfo.isMale) "남" else "여"
)