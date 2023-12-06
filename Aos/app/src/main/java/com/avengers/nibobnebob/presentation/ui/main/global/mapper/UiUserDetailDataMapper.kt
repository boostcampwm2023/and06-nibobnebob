package com.avengers.nibobnebob.presentation.ui.main.global.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.avengers.nibobnebob.domain.model.UserDetailData
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiUserDetailData
import com.avengers.nibobnebob.presentation.ui.toAgeString


@RequiresApi(Build.VERSION_CODES.O)
internal fun UserDetailData.toUiUserDetailData() = UiUserDetailData(
    nickName = nickName,
    region = region,
    age = birthdate.toAgeString(),
    isFollow = isFollow
)