package com.avengers.nibobnebob.presentation.ui.global.model

data class UiReviewData(
    val reviewId : Int,
    val profileImage : String,
    val nickName : String,
    val visited : String,
    val parkingConvenience : Int,
    val taste : Int,
    val service : Int,
    val cleanliness : Int,
    val review : String,
    val thumbsUpCnt : Int,
    val thumbsDownCnt : Int,
    val isThumbsUp : Boolean,
    val isThumbsDown : Boolean,
    val onThumbsUpClick : (UiReviewData) -> Unit,
    val onThumbsDownClick : (UiReviewData) -> Unit,
    val onReviewClick : (Int) -> Unit
)