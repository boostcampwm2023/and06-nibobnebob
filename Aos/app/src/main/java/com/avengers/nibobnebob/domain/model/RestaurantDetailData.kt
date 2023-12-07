package com.avengers.nibobnebob.domain.model

import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

data class RestaurantDetailData(
    val isMy: Boolean,
    val isWish: Boolean,
    val address: String,
    val category: String,
    val id: Int,
    val location: LocationData,
    val name: String,
    val phoneNumber: String,
    val reviewCnt: Int,
    val reviews: List<ReviewsData>?
) : BaseDomainModel

data class ReviewSortData(
    val hasNext: Boolean,
    val reviewItems: List<ReviewsData>
) : BaseDomainModel

data class ReviewsData(
    val id: Int,
    val createdAt: String,
    val isCarVisit: Boolean,
    val overallExperience: String,
    val parkingArea: Int,
    val restroomCleanliness: Int,
    val service: Int,
    val taste: Int,
    val transportationAccessibility: Int,
    val reviewer: String,
    val isLike: Boolean?,
    val likeCount: Int,
    val dislikeCount: Int
) : BaseDomainModel

