package com.bennyhuo.kotlin.coroutine.ch03

import kotlin.coroutines.*

class LogInterceptor : ContinuationInterceptor {
    override val key = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>)
            = LogContinuation(continuation)
}

class LogContinuation<T>(private val continuation: Continuation<T>)
    : Continuation<T> by continuation {
    override fun resumeWith(result: Result<T>) {
        println("before resumeWith: $result")
        continuation.resumeWith(result)
        println("after resumeWith.")
    }
}

fun main() {
    suspend {
        suspendFunc02("Hello", "Kotlin")
        suspendFunc02("Hello", "Coroutine")
    }.startCoroutine(object : Continuation<Int> {
        override val context = LogInterceptor()

        override fun resumeWith(result: Result<Int>) {
            result.getOrThrow()
        }
    })
}