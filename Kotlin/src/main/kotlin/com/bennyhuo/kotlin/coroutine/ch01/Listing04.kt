package com.bennyhuo.kotlin.coroutine.ch01

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


fun main() {
    loopMain {
        runOnIOThread {
            println("A")
            delay(1000) {
                println("B")
                runOnMainThread {
                    println("C")
                }
            }
        }
    }
}

fun loopMain(block: () -> Unit) {
    Looper.prepareMainLooper()
    runOnIOThread(block)
    Looper.loop()
}

val ioExecutor =
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
val delayExecutor = Executors.newScheduledThreadPool(1)
val mainExecutor by lazy { Handler(Looper.getMainLooper()) }

fun runOnIOThread(block: () -> Unit) {
    ioExecutor.execute(block)
}

fun runOnMainThread(block: () -> Unit) {
    mainExecutor.post(block)
}

fun delay(ms: Long, block: () -> Unit) {
    delayExecutor.schedule(block, ms, TimeUnit.MILLISECONDS)
}