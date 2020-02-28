package com.bennyhuo.kotlin.coroutine.ch03

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext

fun main() {
    val ref = ::notSuspend
    ref.call()
    val result = ref.call(object: Continuation<Int> {
        override val context = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            println("resumeWith: ${result.getOrNull()}")
        }
    })

    println(result)
}