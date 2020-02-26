package com.bennyhuo.kotlin.coroutine.ch01

import java.util.concurrent.CompletableFuture

fun bitmapCompletableFuture(url: String): CompletableFuture<Bitmap> =
    CompletableFuture.supplyAsync {
        download(url)
    }

fun callCompletableFuture() {
    urls.map {
        bitmapCompletableFuture(it)
    }.let { futureList ->
        CompletableFuture.allOf(*futureList.toTypedArray())
            .thenApply {
                futureList.map { it.get() }
            }
    }.thenAccept { bitmaps ->
        println(bitmaps.size)
    }.join()
}

fun <T> List<CompletableFuture<T>>.allOf(): CompletableFuture<List<T>> {
    return CompletableFuture.allOf(*this.toTypedArray())
        .thenApply {
            this.map { it.get() }
        }
}

fun callSimplifiedCompletableFuture() {
    urls.map {
            bitmapCompletableFuture(it)
        }.allOf()
        .thenAccept { bitmaps ->
            println(bitmaps.size)
        }.join()
}

fun main() {
    callSimplifiedCompletableFuture()
}