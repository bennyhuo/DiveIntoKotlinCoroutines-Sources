package com.bennyhuo.kotlin.coroutine.ch01

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

val EMPTY_BITMAP = ByteArray(0)

fun syncBitmap(url: String) = download(url)

val urls = arrayOf(
    "https://www.bennyhuo.com/assets/avatar.jpg",
    "https://www.bennyhuo.com/assets/avatar.jpg",
    "https://www.bennyhuo.com/assets/avatar.jpg"
)

fun loopOnSyncCalls() {
    val bitmaps = urls.map { syncBitmap(it) }
}

fun loopOnAsyncCalls() {
    val countDownLatch = CountDownLatch(urls.size)
    val map = urls.map { it to EMPTY_BITMAP }
        .toMap(ConcurrentHashMap<String, Bitmap>())
    urls.map { url ->
        asyncBitmap(url, onSuccess = {
            map[url] = it
            countDownLatch.countDown()
        }, onError = {
            showError(it)
            countDownLatch.countDown()
        })
    }
    countDownLatch.await()
    val bitmaps = map.values
}

fun main() {
    syncBitmap("https://www.bennyhuo.com/assets/avatar.jpg")
    measureTimeMillis {
        loopOnAsyncCalls()
    }.let(::println)

    measureTimeMillis {
        loopOnSyncCalls()
    }.let(::println)
}