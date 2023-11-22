package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.BaseEdit
import com.avengers.nibobnebob.data.model.response.MyDefaultInfoResponse
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageEditInfoData

fun BaseEdit.toUiMyPageEditInfoData() = UiMyPageEditInfoData(
    nickName = data.userInfo.nickName,
    email = data.userInfo.email,
    provider = data.userInfo.provider,
    birth = data.userInfo.birthdate,
    location = data.userInfo.region,
    gender = data.userInfo.isMale
)