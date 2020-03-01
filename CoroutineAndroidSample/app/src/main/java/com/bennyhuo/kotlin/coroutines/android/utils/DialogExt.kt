package com.bennyhuo.kotlin.coroutines.android.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun Context.alert(title: String, message: String): Boolean =
    suspendCancellableCoroutine { continuation ->

        AlertDialog.Builder(this)
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
                continuation.resume(false)
            }.setPositiveButton("Yes") { dialog, which ->
                dialog.dismiss()
                continuation.resume(true)
            }.setTitle(title)
            .setMessage(message)
            .setOnCancelListener {
                continuation.resume(false)
            }.create()
            .also { dialog ->
                continuation.invokeOnCancellation {
                    dialog.dismiss()
                }
            }.show()
    }