package com.bennyhuo.kotlin

import com.bennyhuo.kotlin.coroutine.native.commonMain
import com.bennyhuo.kotlin.coroutine.native.coroutineSample
import com.bennyhuo.kotlin.coroutine.native.log
import com.bennyhuo.kotlin.coroutine.native.workersSample
import hello.hello
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
//    workersSample()
//    hello()
//    commonMain()
    log("start")
    coroutineSample()
    log("end")
}