package com.bennyhuo.kotlin.coroutine.ch01

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

fun asyncBitmapCancellable(
    url: String, onSuccess: (Bitmap) -> Unit,
    onError: (Throwable) -> Unit
) = thread {
    try {
        downloadCancellable(url).also(onSuccess)
    } catch (e: Exception) {
        onError(e)
    }
}

fun downloadCancellable(url: String): Bitmap {
    return getAsStream(url).use { inputStream ->
        val bos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        while (true) {
            val length = inputStream.read(buffer, 0, 1024)
            if (length > 0) {
                bos.write(buffer, 0, length)
            } else {
                break
            }
            if (Thread.interrupted())
                throw InterruptedException("Task is cancelled.")
        }
        bos.toByteArray()
    }
}

fun main() {
    val url = "https://www.bennyhuo.com/assets/avatar.jpg"
    checkUrl(url)
    val thread = asyncBitmapCancellable(
        url,
        onSuccess = ::show,
        onError = ::showError
    )
    delay(100) { thread.interrupt() }
}