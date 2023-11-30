package com.avengers.nibobnebob.presentation.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object TimeUtil {

    fun formatTime(createdAt: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
        try {
            val createdDate = sdf.parse(createdAt)
            val now = Date()

            val timeDiffMillis = now.time - createdDate!!.time

            return when {
                timeDiffMillis < DateUtils.MINUTE_IN_MILLIS -> {
                    "방금 전"
                }
                timeDiffMillis < DateUtils.HOUR_IN_MILLIS -> {
                    "${timeDiffMillis / DateUtils.MINUTE_IN_MILLIS} 분 전"
                }
                timeDiffMillis < DateUtils.DAY_IN_MILLIS -> {
                    "${timeDiffMillis / DateUtils.HOUR_IN_MILLIS} 시간 전"
                }
                else -> {
                    SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(createdDate)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return createdAt
        }
    }
}