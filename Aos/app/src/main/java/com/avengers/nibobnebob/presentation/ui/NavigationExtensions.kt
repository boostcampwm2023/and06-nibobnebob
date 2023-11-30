package com.avengers.nibobnebob.presentation.ui

import androidx.navigation.NavController
import com.avengers.nibobnebob.NavGraphDirections
import com.avengers.nibobnebob.presentation.ui.main.home.search.RestaurantSearchFragmentDirections


internal fun NavController.toRestaurantDetail(restaurantId: Int) {
    val action = NavGraphDirections.globalToRestaurantDetailFragment(restaurantId)
    navigate(action)
}

internal fun NavController.toAddRestaurant(restaurantName: String, restaurantId: Int) {
    val action = NavGraphDirections.globalToAddMyRestaurantFragment(restaurantName, restaurantId)
    navigate(action)
}

internal fun NavController.toHome(){
    val action = NavGraphDirections.globalToHomeFragment()
    navigate(action)
}

internal fun NavController.toMyPage() {
    val action = NavGraphDirections.globalToMyPageFragment()
    navigate(action)
}