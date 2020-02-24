package com.bennyhuo.kotlin.coroutine.ch01

import okhttp3.HttpUrl
import java.net.MalformedURLException
import kotlin.concurrent.thread


@Throws(MalformedURLException::class)
fun checkUrl(url: String){
    HttpUrl.parse(url) ?: throw MalformedURLException("Illegal url: $url")
}

fun asyncBitmap(
    url: String, onSuccess: (Bitmap) -> Unit,
    onError: (Throwable) -> Unit
) {
    thread {
        try {
            download(url).also(onSuccess)
        } catch (e: Exception) {
            onError(e)
        }
    }
}

fun show(bitmap: Bitmap) {
    println("Got bitmap, size: ${bitmap.size}")
}

fun showError(throwable: Throwable) {
    println("Error: $throwable")
}

fun callAsync(){
    val url = "https://www.bennyhuo.com/assets/avatar.jpg"
    checkUrl(url)
    asyncBitmap(url, onSuccess = ::show, onError = ::showError)
}

fun callAsync2(){
    try {
        val url = "https://www.bennyhuo.com/assets/avatar.jpg"
        checkUrl(url)
        asyncBitmap(url, onSuccess = ::show, onError = ::showError)
    } catch (e: Exception) {
        showError(e)
    }
}

fun main() {
    callAsync()
}