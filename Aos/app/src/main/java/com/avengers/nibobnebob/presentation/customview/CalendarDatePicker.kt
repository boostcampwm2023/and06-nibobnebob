package com.avengers.nibobnebob.presentation.customview


import androidx.fragment.app.FragmentManager
import com.avengers.nibobnebob.presentation.ui.toDateString
import com.google.android.material.datepicker.MaterialDatePicker

class CalendarDatePicker(
    private val onSelectDateListener: (String) -> Unit
) {
    private val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("생일을 고르세요")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    init{
        datePicker.addOnPositiveButtonClickListener {
            onSelectDateListener(it.toDateString())
        }
    }

    fun show(fragmentManager: FragmentManager){
        datePicker.show(fragmentManager,"")
    }
}


