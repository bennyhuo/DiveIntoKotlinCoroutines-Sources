package com.bennyhuo.kotlin.coroutine.ch01

import kotlin.concurrent.thread

fun main() {
    val callback = {
        println("D")
    }

    val task = {
        println("C")
        callback()
    }

    println("A")
    thread(block = task)
    println("B")
}