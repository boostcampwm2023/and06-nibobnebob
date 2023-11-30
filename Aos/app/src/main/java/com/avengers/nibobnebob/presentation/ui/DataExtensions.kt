package com.avengers.nibobnebob.presentation.ui

import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal fun Long.toDateString(): String {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = Date(this)
    return dateFormat.format(date)
}

@RequiresApi(android.os.Build.VERSION_CODES.O)
internal fun String.toAgeString(): String {
    val birthYear = split("/")
    if (birthYear.size != 3) return this
    val age = java.time.LocalDate.now().year - birthYear[0].toInt()
    return if (age < 10) {
        "10세 이하"
    } else if (age < 20) {
        "10대"
    } else if (age < 30) {
        "20대"
    } else if (age < 40) {
        "30대"
    } else if (age < 50) {
        "40대"
    } else if (age < 60) {
        "50대"
    } else {
        "60세 이상"
    }
}