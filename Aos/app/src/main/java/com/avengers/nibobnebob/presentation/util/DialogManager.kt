package com.avengers.nibobnebob.presentation.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface

internal fun dialogManager(
    context : Context,
    title : String,
    message : String,
    onClickConfirm : Unit,
    onClickDismiss: Unit
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("ok"
        ) { p0, p1 -> onClickConfirm }
        .setNegativeButton("no"
        ) { p0, p1 -> onClickDismiss }
        .create()
        .show()
}