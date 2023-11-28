package com.avengers.nibobnebob.data.model.response

data class SearchRestaurantResponse(
    val id : Int,
    val name : String,
    val location : String,
    val address : String,
    val phoneNumber : String,
    val reviewCnt : Int,
    val category: String,
    val distance : Double
)
