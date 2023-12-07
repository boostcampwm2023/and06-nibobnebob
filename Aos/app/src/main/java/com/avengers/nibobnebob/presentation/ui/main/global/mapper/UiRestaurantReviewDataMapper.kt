package com.avengers.nibobnebob.presentation.ui.main.global.mapper

import com.avengers.nibobnebob.domain.model.ReviewsData
import com.avengers.nibobnebob.presentation.ui.main.global.model.UiReviewData

internal fun ReviewsData.toUiRestaurantReviewDataInfo(
    onThumbsUpClick: (Int) -> Unit,
    onThumbsDownClick: (Int) -> Unit,
) = UiReviewData(
    reviewId = id,
    createdAt = createdAt,
    overallExperience = overallExperience,
    isCarVisit = isCarVisit,
    parkingArea = parkingArea,
    cleanliness = restroomCleanliness,
    service = service,
    taste = taste,
    reviewer = reviewer,
    transportationAccessibility = transportationAccessibility,
    onThumbsUpClick = onThumbsUpClick,
    onThumbsDownClick = onThumbsDownClick,
)