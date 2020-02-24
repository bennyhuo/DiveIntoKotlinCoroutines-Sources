package com.bennyhuo.kotlin.coroutine.ch06.concurrent

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.util.concurrent.atomic.AtomicInteger

suspend fun main() {
    atomicCounter()
//    atomicFuCounter()
    mutexCounter()
    semaphoreCounter()
    pureFunctionCounter()
}

suspend fun plainCounter() {
    var count = 0

    List(1000) {
        GlobalScope.launch {
            count++
        }
    }.joinAll()

    println(count)
}

suspend fun atomicCounter() {
    val count = AtomicInteger(0)

    List(1000) {
        GlobalScope.launch {
            count.incrementAndGet()
        }
    }.joinAll()

    println(count.get())
}

//val count = atomic(0)
//
//suspend fun atomicFuCounter() {
//
//    List(1000) {
//        GlobalScope.launch {
//            count.incrementAndGet()
//        }
//    }.joinAll()
//
//    println(count.value)
//}

suspend fun mutexCounter() {
    var count = 0
    val mutext = Mutex()
    List(1000) {
        GlobalScope.launch {
            mutext.withLock {
                count++
            }
        }
    }.joinAll()

    println(count)
}

suspend fun semaphoreCounter(){
    var count = 0
    val semaphore = Semaphore(1)
    List(1000) {
        GlobalScope.launch {
            semaphore.withPermit {
                count++
            }
        }
    }.joinAll()

    println(count)
}

suspend fun pureFunctionCounter(){
    val count = 0

    val result = count + List(1000) {
        GlobalScope.async { 1 }
    }.map {
        it.await()
    }.sum()

    println(result)
}
