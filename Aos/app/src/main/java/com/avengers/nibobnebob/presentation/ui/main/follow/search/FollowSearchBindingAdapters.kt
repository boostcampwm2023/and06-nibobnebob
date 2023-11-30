package com.avengers.nibobnebob.presentation.ui.main.follow.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.databinding.BindingAdapter
import com.avengers.nibobnebob.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


@BindingAdapter("curFilterChips")
fun bindCurFilterChips(cg: ChipGroup, curFilter: List<String>){
    cg.removeAllViews()

    curFilter.forEach {
        val chip = Chip(cg.context)
        chip.setChipBackgroundColorResource(R.color.nn_primary_light4)
        chip.chipStrokeWidth = 0F
        chip.text = it
        cg.addView(chip)
    }
}