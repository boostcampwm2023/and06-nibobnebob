package com.avengers.nibobnebob.presentation.ui

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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

internal fun String.toMultiPart(context: Context, fileName: String): MultipartBody.Part {
    val uri = this.toUri()
    val file = File(getRealPathFromUri(uri, context) ?: "")
    val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(fileName, file.name, requestFile)
}


// 절대경로 변환
private fun getRealPathFromUri(uri: Uri, context: Context): String? {
    var filePath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.let {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            filePath = it.getString(columnIndex)
        }
        it.close()
    }
    return filePath
}