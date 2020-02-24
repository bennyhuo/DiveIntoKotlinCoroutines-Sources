package com.bennyhuo.kotlin.coroutine.ch01

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

fun main() {
    Observable.just("https://www.bennyhuo.com/assets/avatar.jpg")
        .map { download(it) }
        .subscribeOn(Schedulers.io())
        .subscribe({ bitmap ->
            show(bitmap)
        }, { throwable ->
            showError(throwable)
        })

    Thread.sleep(10000)
}