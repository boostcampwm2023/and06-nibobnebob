package com.avengers.nibobnebob.presentation.util

import android.content.Context
import android.view.LayoutInflater
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.BottomSheetRestaurantBinding
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantSimpleData
import com.google.android.material.bottomsheet.BottomSheetDialog


internal fun restaurantSheet(
    context: Context,
    data: UiRestaurantSimpleData,
    onClickAddWishRestaurant: (Int, Boolean) -> Boolean,
    onClickAddMyRestaurant: (Int) -> Unit,
    onClickGoReview: (Int) -> Unit
): BottomSheetDialog {
    val dialog = BottomSheetDialog(context)
    val binding = BottomSheetRestaurantBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)

    var isWishState = data.isInWishList

    binding.btnAddMyRestaurant.setOnClickListener {
        onClickAddMyRestaurant(data.id)
        dialog.dismiss()
    }

    binding.btnAddWishRestaurant.setOnClickListener {
        val result = onClickAddWishRestaurant(data.id, data.isInWishList)

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
        onClickGoReview(data.id)
        dialog.dismiss()
    }

    return dialog
}