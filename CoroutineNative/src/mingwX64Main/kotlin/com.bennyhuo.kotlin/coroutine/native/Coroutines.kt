package com.bennyhuo.kotlin.coroutine.native

import kotlinx.coroutines.*
import kotlin.native.concurrent.isFrozen

suspend fun coroutineSample() {
    GlobalScope.launch(Dispatchers.Default) {
        log(1)
        val job = launch(newSingleThreadContext("MyDispatcher")) {
            log(2)
            delay(1000)
            log(3)
        }
        log(4)
        job.join()
        log(5)

        val result = withContext(newSingleThreadContext("MyDispatcher")){
            Counter()
        }
        log(result.isFrozen)
        //InvalidMutabilityException
        //log(++result.num)

    }.join()
}
