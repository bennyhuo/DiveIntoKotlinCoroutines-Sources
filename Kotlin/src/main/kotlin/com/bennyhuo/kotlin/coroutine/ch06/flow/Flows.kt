package com.bennyhuo.kotlin.coroutine.ch06.flow

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors

fun sequences() {
    val ints = sequence {
        (1..3).forEach {
            yield(it)
            //delay(1000)
        }
    }

    for (i in ints) {
        println(i)
    }

    for (i in ints) {
        println(i)
    }

}

fun createFlow() = flow<Int> {
    (1..3).forEach {
        emit(it)
        delay(100)
    }
}.onEach { println(it) }

suspend fun flows() {
    val intFlow = flow<Int> {
        (1..3).forEach {
            emit(it)
            delay(100)
        }
    }

    val dispatcher =
        Executors.newSingleThreadExecutor {
            Thread(
                it,
                "MyThread"
            ).also { it.isDaemon = true }
        }.asCoroutineDispatcher()

    GlobalScope.launch(dispatcher) {
        intFlow.flowOn(Dispatchers.IO)
            .collect { println(it) }
    }.join()

    intFlow.onEach { }.launchIn(GlobalScope)
}


suspend fun exception() {
    flow {
        emit(1)
        throw ArithmeticException("Div 0")
    }.catch { t: Throwable ->
            println("caught error: $t")
            emit(10)
        }.onCompletion { t: Throwable? ->
            println("finally.")
        }.flowOn(Dispatchers.IO)
        .collect { println(it) }


//    flow { // bad!!!
//        try {
//            emit(1)
//            throw ArithmeticException("Div 0")
//        } catch (t: Throwable){
//            println("caught error: $t")
//        } finally {
//            println("finally.")
//        }
//    }
}

suspend fun terminalOperator() {
    GlobalScope.launch {
        val intFlow = flowOf(1, 2, 3, 4).onEach { println(it) }

        val sum = intFlow
            .reduce { accumulator, value ->
                accumulator + value
            }

        println(sum)

        intFlow.onEach {
            println(it)
        }.collect()
    }.join()
}

fun fromCollections() {
    listOf(1, 2, 3, 4).asFlow()
    setOf(1, 2, 3, 4).asFlow()
    flowOf(1, 2, 3, 4)
}

suspend fun fromChannel() {
    val channel = Channel<Int>()
    channel.consumeAsFlow()

//    flow { // bad!!
//        emit(1)
//        withContext(Dispatchers.IO){
//            emit(2)
//        }
//    }

    channelFlow {
        // good
        send(1)
        withContext(Dispatchers.IO) {
            send(2)
        }
    }
}

suspend fun cancelFlow() {
    val job = GlobalScope.launch {
        val intFlow = flow {
            (1..3).forEach {
                delay(1000)
                emit(it)
            }
        }

        intFlow.collect { println(it) }
    }

    delay(2500)
    job.cancelAndJoin()
}

suspend fun backPressure() {
    flow {
        List(100) {
            emit(it)
        }
    }
        //.buffer()
//        .conflate()
        .collectLatest { value ->
            println("Collecting $value")
            delay(100) // Emulate work
            println("$value collected")
        }
}

fun rxjava() {
    val observable = Observable.create<Int> {
            (1..3).forEach { e ->
                it.onNext(e)
            }
            it.onComplete()
        }.onErrorReturn { t ->
            println(t)
            10
        }

        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.from(Executors.newSingleThreadExecutor {
            Thread(
                it,
                "MyThread-Rx"
            )
        }))


    observable.subscribe {
        println(it)
    }

    observable.subscribe {
        println(it)
    }

    Thread.sleep(10000)
}

suspend fun transformFlow() {
    flow {
        List(5) { emit(it) }
    }.map {
        flow { List(it) { emit(it) } }
    }.flattenMerge()
        .collect { println(it) }
}

suspend fun main() {
//    sequences()
//    flows()
//    exception()
//    cancelFlow()
//    rxjava()
    backPressure()
//    transformFlow()
}