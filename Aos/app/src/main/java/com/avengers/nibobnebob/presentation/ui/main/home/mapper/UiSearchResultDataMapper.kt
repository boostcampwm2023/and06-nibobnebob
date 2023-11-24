package com.avengers.nibobnebob.presentation.ui.main.home.mapper

import com.avengers.nibobnebob.data.model.response.SearchRestaurantResponse
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiSearchResultData

fun SearchRestaurantResponse.toUiSearchResultData() : UiSearchResultData = UiSearchResultData(
    id = id,
    name = name,
    address = address,
    location = location
)