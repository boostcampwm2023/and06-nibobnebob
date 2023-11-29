package com.avengers.nibobnebob.presentation.ui.main.global.mapper

import com.avengers.nibobnebob.data.model.response.Reviews
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiReviewData

internal fun Reviews.toUiRestaurantReviewDataInfo(
    onThumbsUpClick: (Int) -> Unit,
    onThumbsDownClick: (Int) -> Unit,
    onReviewClick: (Int) -> Unit
) = UiReviewData(
    reviewId = id,
    overallExperience = overallExperience,
    parkingArea = parkingArea,
    cleanliness = restroomCleanliness,
    service = service,
    taste = taste,
    reviewer = reviewer,
    transportationAccessibility = transportationAccessibility,
    onThumbsUpClick = onThumbsUpClick,
    onThumbsDownClick = onThumbsDownClick,
    onReviewClick = onReviewClick
)