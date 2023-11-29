package com.avengers.nibobnebob.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.avengers.nibobnebob.R
import com.avengers.nibobnebob.databinding.DialogSelectRegionBinding
import com.google.android.material.chip.Chip

class SelectRegionDialog(
    context: Context,
    private val curFilterList: List<String>,
    private val onFilterChangeListener: (List<String>) -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogSelectRegionBinding
    private val filterList = context.resources.getStringArray(R.array.location_list)
    private var newFilterList = curFilterList.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSelectRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterList.forEach {
            val chip = layoutInflater.inflate(R.layout.layout_filter_chip, cgRegion, false) as Chip
            chip.id = View.generateViewId()
            chip.text = it
            cgRegion.addView(chip)
            if (it in curFilterList) cgRegion.check(chip.id)
        }

        cgRegion.setOnCheckedStateChangeListener { _, checkedIds ->
            checkedIds.forEach {
                val checkedFilter = findViewById<Chip>(it).text.toString()
                if( checkedFilter !in newFilterList) newFilterList.add(checkedFilter)
            }
        }

        btnConfirm.setOnClickListener {
            onFilterChangeListener(newFilterList)
            dismiss()
        }
    }

}