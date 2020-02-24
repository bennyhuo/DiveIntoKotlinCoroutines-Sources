package com.bennyhuo.kotlin.coroutine.ch06.exceptionhandler

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class GlobalCoroutineExceptionHandler: CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        println("Global Coroutine exception: $exception")
    }
}

suspend fun main() {
    GlobalScope.launch {
        throw ArithmeticException("Hey!")
    }.join()
}