package com.avengers.nibobnebob.presentation.ui.main.follow.search

import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


@BindingAdapter("curFilterChips")
fun bindCurFilterChips(cg: ChipGroup, curFilter: List<String>){
    cg.removeAllViews()

    curFilter.forEach {
        val chip = Chip(cg.context)
        chip.text = it
        cg.addView(chip)
    }
}