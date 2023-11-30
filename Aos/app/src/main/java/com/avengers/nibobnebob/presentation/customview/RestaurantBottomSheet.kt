package com.avengers.nibobnebob.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.BottomSheetRestaurantBinding
import com.avengers.nibobnebob.presentation.ui.main.home.model.UiRestaurantData
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch


class RestaurantBottomSheet(
    context: Context,
    private val data: UiRestaurantData,
    private val onClickAddWishRestaurant: suspend (Int, Boolean) -> Boolean,
    private val onClickAddMyRestaurant: (String, Int) -> Unit,
    private val onClickGoReview: (Int) -> Unit
) : BottomSheetDialog(context) {

    private var binding: BottomSheetRestaurantBinding
    private var isWishState = data.isInWishList

    init {
        binding = BottomSheetRestaurantBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        binding.item = data
        setBottomSheetListener()
    }

    private fun setBottomSheetListener() {
        binding.btnAddMyRestaurant.setOnClickListener {
            onClickAddMyRestaurant(data.name, data.id)
            dismiss()
        }

        binding.btnAddWishRestaurant.setOnClickListener {
            lifecycleScope.launch {
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

        }

        binding.btnGoReview.setOnClickListener {
            onClickGoReview(data.id)
            dismiss()
        }
    }

}
