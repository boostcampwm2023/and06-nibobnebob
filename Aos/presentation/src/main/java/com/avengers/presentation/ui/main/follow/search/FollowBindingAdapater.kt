package com.avengers.nibobnebob.presentation.ui.main.follow.search

import androidx.databinding.BindingAdapter
import com.avengers.presentation.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

interface RemoveChipInterface {
    fun removeChip(data: String)
}

@BindingAdapter("curFilterChips", "chipRemoveListener")
fun bindCurFilterChips(
    cg: ChipGroup,
    curFilter: List<String>,
    removeListener: RemoveChipInterface
) {
    cg.removeAllViews()

    curFilter.forEach { data ->
        val chip = Chip(cg.context)
        chip.setChipBackgroundColorResource(R.color.nn_primary_light4)
        chip.chipStrokeWidth = 0F
        chip.text = data
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            cg.removeView(chip)
            removeListener.removeChip(data)
        }
        cg.addView(chip)
    }
}