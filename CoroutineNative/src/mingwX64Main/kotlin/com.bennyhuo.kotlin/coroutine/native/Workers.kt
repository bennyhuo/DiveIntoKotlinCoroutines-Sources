package com.bennyhuo.kotlin.coroutine.native

import platform.posix.pthread_self
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker

class Counter {
    var num = 0
}

class Refer(val any: Any)

fun workersSample() {

    val counter = Counter()

    val refer = Refer(counter)

    counter.num = 100
    //val frozenCounter = counter.freeze()


    val a = 100

    val worker = Worker.start(name = "myWorker")

    log(1)

    val future = worker.execute(TransferMode.SAFE, {
        log(3)
        counter
    }) { counter ->
        log(4)
        log("counter: ${counter.num}")
        counter
    }

    log(2)

    future.consume { result ->
        log(5)
        log("consume result: $result")
        log("result num: ${result.num}")
    }

    log(6)
}

fun log(vararg msg: Any?) {
    println("[Thread-${pthread_self()}] ${msg.joinToString(" ")}")
}