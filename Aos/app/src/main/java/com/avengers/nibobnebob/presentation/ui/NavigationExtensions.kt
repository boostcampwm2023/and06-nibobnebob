package com.avengers.nibobnebob.presentation.ui

import androidx.navigation.NavController
import com.avengers.nibobnebob.NavGraphDirections


internal fun NavController.toRestaurantDetail(restaurantId: Int) {
    val action = NavGraphDirections.globalToRestaurantDetailFragment(restaurantId)
    navigate(action)
}

internal fun NavController.toAddRestaurant(restaurantName: String, restaurantId: Int) {
    val action = NavGraphDirections.globalToAddMyRestaurantFragment(restaurantName, restaurantId)
    navigate(action)
}