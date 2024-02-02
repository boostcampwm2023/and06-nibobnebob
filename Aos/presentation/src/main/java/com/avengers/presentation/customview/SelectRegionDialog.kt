package com.avengers.nibobnebob.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.avengers.presentation.R
import com.avengers.presentation.databinding.DialogSelectRegionBinding
import com.google.android.material.chip.Chip

class SelectRegionDialog(
    context: Context,
    private val curFilterList: List<String>,
    private val onFilterChangeListener: (List<String>) -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogSelectRegionBinding
    private val filterList = context.resources.getStringArray(R.array.location_list)
    private var newFilterList = curFilterList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSelectRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        filterList.forEach {
            val chip = layoutInflater.inflate(R.layout.layout_filter_chip, cgRegion, false) as Chip
            chip.id = View.generateViewId()
            chip.text = it
            chip.chipStrokeWidth = 0F
            chip.setChipBackgroundColorResource(R.color.selector_filter_background)
            cgRegion.addView(chip)
            if (it in curFilterList) cgRegion.check(chip.id)
        }

        cgRegion.setOnCheckedStateChangeListener { _, checkedIds ->
            newFilterList = checkedIds.map {
                findViewById<Chip>(it).text.toString()
            }
        }

        btnConfirm.setOnClickListener {
            onFilterChangeListener(newFilterList)
            dismiss()
        }
    }

}