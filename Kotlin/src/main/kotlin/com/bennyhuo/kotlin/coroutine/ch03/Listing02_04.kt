package com.bennyhuo.kotlin.coroutine.ch03

import com.bennyhuo.kotlin.coroutine.ch01.delayExecutor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

fun <R, T> launchCoroutine(receiver: R, block: suspend R.() -> T) {
    block.startCoroutine(receiver, object : Continuation<T> {
        override fun resumeWith(result: Result<T>) {
            println("Coroutine End: $result")
        }

        override val context = EmptyCoroutineContext
    })
}

suspend fun delay(delay: Long) = suspendCoroutine<Unit> {
    delayExecutor.schedule({
        it.resume(Unit)
    }, delay, TimeUnit.MILLISECONDS)
}

class ProducerScope<T> {
    suspend fun produce(value: T) {
        println("produce $value")
    }
}

fun callLaunchCoroutine(){
    launchCoroutine(ProducerScope<Int>()) {
        println("In Coroutine.")
        produce(1024)
        delay(1000)
        produce(2048)
    }
}

@RestrictsSuspension
class RestrictProducerScope<T> {
    suspend fun produce(value: T){
        println("produce $value")
    }
}

fun callLaunchCoroutineRestricted(){
    launchCoroutine(RestrictProducerScope<Int>()) {
        println("In Coroutine.")
        produce(1024)
        // delay(1000)
        produce(2048)
    }
}


fun main() {
    callLaunchCoroutine()
}