package com.avengers.nibobnebob.presentation.util

import android.content.Context
import android.view.LayoutInflater
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.BottomSheetRestaurantBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


internal fun restaurantSheet(
    context: Context,
    restaurantId: Int,
    isWish: Boolean,
    onClickAddWishRestaurant: (Int, Boolean) -> Boolean,
    onClickAddMyRestaurant: (Int) -> Unit,
    onClickGoReview: (Int) -> Unit
): BottomSheetDialog {
    val dialog = BottomSheetDialog(context)
    val binding = BottomSheetRestaurantBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)

    var isWishState = isWish

    binding.btnAddMyRestaurant.setOnClickListener {
        onClickAddMyRestaurant(restaurantId)
        dialog.dismiss()
    }

    binding.btnAddWishRestaurant.setOnClickListener {
        val result = onClickAddWishRestaurant(restaurantId, isWish)

        if (result) {
            isWishState = !isWishState

            if (isWishState) {
                binding.btnAddWishRestaurant.setBackgroundResource(R.drawable.ic_star_full)
            } else {
                binding.btnAddWishRestaurant.setBackgroundResource(R.drawable.ic_star_border)
            }
        }
    }

    binding.btnGoReview.setOnClickListener {
        onClickGoReview(restaurantId)
        dialog.dismiss()
    }

    return dialog
}