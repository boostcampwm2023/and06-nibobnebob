package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import com.avengers.nibobnebob.data.model.response.MyPageEditInfoResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageEditInfoData
import com.avengers.nibobnebob.presentation.util.LoginType

fun MyPageEditInfoResponse.toUiMyPageEditInfoData() = UiMyPageEditInfoData(
    nickName = data.userInfo.nickName,
    email = data.userInfo.email,
    provider = if(data.userInfo.provider == LoginType.NAVER_LOGIN) "네이버 소셜 로그인" else "",
    birth = data.userInfo.birthdate,
    location = data.userInfo.region,
    gender = if(data.userInfo.isMale) "남" else "여"
)