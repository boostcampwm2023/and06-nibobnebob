package com.avengers.nibobnebob.data.model.request

data class AddRestaurantRequest(
    val isCarVisit: Boolean,
    val transportationAccessibility: Int?,
    val parkingArea: Int?,
    val taste: Int,
    val service: Int,
    val restroomCleanliness: Int,
    val overallExperience: String
)
