package com.bennyhuo.kotlin.coroutine.ch03

import kotlin.coroutines.*

class CoroutineName(val name: String): AbstractCoroutineContextElement(Key){
    companion object Key: CoroutineContext.Key<CoroutineName>

    override fun toString() = name
}

class CoroutineExceptionHandler(val onErrorAction: (Throwable) -> Unit)
    : AbstractCoroutineContextElement(Key){
    companion object Key: CoroutineContext.Key<CoroutineExceptionHandler>

    fun onError(error: Throwable){
        error.printStackTrace()
        onErrorAction(error)
    }
}

fun main() {
    var list: List<Int> = emptyList()
    list += 0
    list += listOf(1, 2, 3)


    var coroutineContext: CoroutineContext = EmptyCoroutineContext
    coroutineContext += CoroutineName("co-01")
    coroutineContext += CoroutineExceptionHandler {
        println(it)
    }

    coroutineContext += CoroutineName("co-01") + CoroutineExceptionHandler {
        println(it)
    }

    suspend {
        println("In Coroutine [${coroutineContext[CoroutineName]}].")
        throw ArithmeticException()
        100
    }.startCoroutine(object : Continuation<Int> {

        override val context = coroutineContext

        override fun resumeWith(result: Result<Int>) {
            result.onFailure {
                context[CoroutineExceptionHandler]?.onError(it)
            }.onSuccess {
                println("Result $it")
            }
        }
    })
}