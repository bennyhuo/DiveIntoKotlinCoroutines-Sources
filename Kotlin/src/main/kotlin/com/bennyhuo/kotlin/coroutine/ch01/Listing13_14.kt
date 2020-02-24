package com.bennyhuo.kotlin.coroutine.ch01

import java.util.concurrent.Callable
import java.util.concurrent.Future

fun bitmapFuture(url: String): Future<Bitmap> {
    return ioExecutor.submit(Callable {
        download(url)
    })
}

fun main() {
    val bitmaps = urls.map {
        bitmapFuture(it)
    }.map {
        it.get()
    }
}