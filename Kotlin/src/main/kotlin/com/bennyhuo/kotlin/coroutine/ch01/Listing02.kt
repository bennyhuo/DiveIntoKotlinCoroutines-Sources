package com.bennyhuo.kotlin.coroutine.ch01

import kotlin.concurrent.thread

fun main() {
    val task = {
        println("C")
    }

    println("A")
    thread(block = task)
    println("B")
}