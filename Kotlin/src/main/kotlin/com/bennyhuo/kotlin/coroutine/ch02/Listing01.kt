package com.bennyhuo.kotlin.coroutine.ch02

import kotlin.coroutines.suspendCoroutine

suspend fun level_0() {
    println("I'm in level 0!")
    level_1()
}

suspend fun level_1() {
    println("I'm in level 1!")
    suspendNow()
}

suspend fun suspendNow()
        = suspendCoroutine<Unit> {

}