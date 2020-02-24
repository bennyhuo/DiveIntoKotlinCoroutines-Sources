package com.bennyhuo.kotlin.coroutine.ch01

import com.bennyhuo.kotlin.coroutine.common.api.httpClient
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.concurrent.thread

fun main() {
    // This will make the async call.
    callAsync {
        println("End 0")
        // This will return immediately from cache.
        callAsync {
            println("End 1")
        }
    }

}

fun callAsync(callback: (Bitmap) -> Unit) {
    val bitmap = asyncBitmap(
        "https://www.bennyhuo.com/assets/avatar.jpg"
    ) { bitmap ->
        println("Async: $bitmap")
        callback(bitmap)
    }
    println("Main $bitmap")
    if (bitmap != null) {
        callback(bitmap)
    }
}

typealias Bitmap = ByteArray

object Cache {
    private val map = WeakHashMap<String, Bitmap>()

    fun get(key: String): Bitmap? {
        return map[key]
    }

    fun put(key: String, bitmap: Bitmap) {
        map[key] = bitmap
    }
}

fun download(url: String): Bitmap {
    return getAsStream(url).use { inputStream ->
        val bos = ByteArrayOutputStream()
        inputStream.copyTo(bos)
        bos.toByteArray()
    }
}

fun getAsStream(url: String): InputStream =
    httpClient.newCall(
        Request.Builder().get().url(url).build()
    ).execute().body()?.byteStream()
        ?: throw IOException("No body")

fun asyncBitmap(
    url: String,
    callback: (Bitmap) -> Unit
): Bitmap? {
    return when (val bitmap = Cache.get(url)) {
        null -> {
            thread {
                download(url)
                    .also { Cache.put(url, it) }
                    .also(callback)
            }
            null
        }
        else -> bitmap
    }
}