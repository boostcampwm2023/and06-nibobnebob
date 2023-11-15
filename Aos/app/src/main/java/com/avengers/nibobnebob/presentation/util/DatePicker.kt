package com.avengers.nibobnebob.presentation.util


import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun showCalendarDatePicker(
    fragmentManager: FragmentManager,
    onSelectDateListener: (String) -> Unit
) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("생일을 고르세요")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()

    datePicker.addOnPositiveButtonClickListener {
        onSelectDateListener(it.toDateString())
    }

    datePicker.show(fragmentManager, "")
}

private fun Long.toDateString(): String {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = Date(this)
    return dateFormat.format(date)
}