package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.avengers.nibobnebob.data.model.response.MyInfoResponse
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyPageInfoData
import com.avengers.nibobnebob.presentation.ui.toAgeString

@RequiresApi(Build.VERSION_CODES.O)
internal fun MyInfoResponse.toUiMyPageInfoData() = UiMyPageInfoData(
    nickName = userInfo.nickName,
    age = userInfo.birthdate.toAgeString(),
    location = userInfo.region,
    gender = if (userInfo.isMale) "남" else "여",
    profileImage = userInfo.profileImage
)