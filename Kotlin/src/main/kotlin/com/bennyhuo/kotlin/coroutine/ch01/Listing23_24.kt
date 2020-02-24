package com.bennyhuo.kotlin.coroutine.ch01

import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun bitmapSuspendable(url: String): Bitmap =
    suspendCoroutine<Bitmap> { continuation ->
        thread {
            try {
                continuation.resume(download(url))
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }

suspend fun main() {
    val bitmap = bitmapSuspendable("https://www.bennyhuo.com/assets/avatar.jpg")
    show(bitmap)
}