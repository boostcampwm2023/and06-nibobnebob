package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.MyPageEditInfoResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageEditInfoData
import com.avengers.nibobnebob.presentation.util.LocationArray
import com.avengers.nibobnebob.presentation.util.LoginType

fun MyPageEditInfoResponse.toUiMyPageEditInfoData() = UiMyPageEditInfoData(
    nickName = data.userInfo.nickName,
    email = data.userInfo.email,
    provider = data.userInfo.provider,
    birth = data.userInfo.birthdate,
    location = data.userInfo.region,
    gender = data.userInfo.isMale
)