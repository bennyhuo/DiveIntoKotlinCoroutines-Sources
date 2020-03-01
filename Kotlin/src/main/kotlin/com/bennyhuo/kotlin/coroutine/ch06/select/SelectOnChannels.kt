package com.bennyhuo.kotlin.coroutine.ch06.select

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlin.random.Random

suspend fun main() {
//    selectOnReceive()
//    selectOnSend()
    flowOnReceive()
}

suspend fun selectOnReceive() {
    val channels = List(10) { Channel<Int>() }

    GlobalScope.launch {
        delay(100)
        channels[Random.nextInt(10)].send(200)

        channels.forEach {
            it.close()
        }
    }

    val result = select<Int?> {
        channels.forEach { channel ->
            channel.onReceive { it }
            // OR
            channel.onReceiveOrNull { it }
        }
    }

    channels.map { it.consumeAsFlow() }.merge()
        .first()

    println(result)
}

suspend fun flowOnReceive() {
    val channels = List(10) { Channel<Int>() }

    GlobalScope.launch {
        delay(100)
        channels[Random.nextInt(10)].send(200)

        channels.forEach {
            it.close()
        }
    }

    val result = channels.map {
            it.consumeAsFlow()
        }
        .merge()
        .first()

    println(result)
}

suspend fun selectOnSend() {
    val channels = List(10) {
        GlobalScope.actor<Int> {
            consumeEach {
                println("receive $it")
            }
        }
    }

    List(100) { element ->
        select<Unit> {
            channels.forEach { channel ->
                channel.onSend(element) { sentChannel -> println("sent on $sentChannel") }
            }
        }
    }
}