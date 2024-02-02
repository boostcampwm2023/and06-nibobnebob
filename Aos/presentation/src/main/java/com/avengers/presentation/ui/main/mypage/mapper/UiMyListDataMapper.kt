package com.avengers.nibobnebob.presentation.ui.main.mypage.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.avengers.nibobnebob.domain.model.MyRestaurantItemData
import com.avengers.nibobnebob.presentation.ui.main.mypage.model.UiMyListData
import com.avengers.nibobnebob.presentation.ui.toCreateDateString

@RequiresApi(Build.VERSION_CODES.O)
fun MyRestaurantItemData.toUiMyListData(): UiMyListData = UiMyListData(
    id = id,
    name = name,
    address = address,
    date = createdAt?.toCreateDateString()
)