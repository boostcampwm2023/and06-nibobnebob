package com.avengers.nibobnebob.presentation.ui

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.avengers.presentation.NavGraphDirections


internal fun NavController.toRestaurantDetail(restaurantId: Int) {
    val action = NavGraphDirections.globalToRestaurantDetailFragment(restaurantId)
    navigate(action)
}

internal fun NavController.toAddRestaurant(restaurantName: String, restaurantId: Int) {
    val action = NavGraphDirections.globalToAddMyRestaurantFragment(restaurantName, restaurantId)
    navigate(action)
}

internal fun NavController.toHome(restaurantId: Int? = 0) {
    val action = if (restaurantId != null) {
        NavGraphDirections.globalToHomeFragment(restaurantId)
    } else {
        NavGraphDirections.globalToHomeFragment()
    }
    navigate(action)
}

internal fun NavController.toMyPage() {
    val action = NavGraphDirections.globalToMyPageFragment()
    navigate(action)
}

internal fun NavController.toUserDetail(nickName: String) {
    val action = NavGraphDirections.globalToUserDetailFragment(nickName)
    navigate(action)
}

internal fun customBack(activity : FragmentActivity, nav : NavController){
    activity.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            nav.navigateUp()
        }
    })
}