package com.avengers.nibobnebob.presentation.ui.main.global.model

data class UiReviewData(
    val reviewId : Int,
    val reviewer : String,
    val createdAt : String = "2023.11.29 테스트",
    val isCarVisit : Boolean,
    val transportationAccessibility : Int = 0,
    val parkingArea : Int = 0,
    val taste : Int,
    val service : Int,
    val cleanliness : Int,
    val overallExperience : String,
    val thumbsUpCnt : Int = 0,
    val thumbsDownCnt : Int = 0,
    val isThumbsUp : Boolean = false,
    val isThumbsDown : Boolean = false,
//    val profileImage : String?,
    val onThumbsUpClick : (Int) -> Unit,
    val onThumbsDownClick : (Int) -> Unit,
    val onReviewClick : (Int) -> Unit
)