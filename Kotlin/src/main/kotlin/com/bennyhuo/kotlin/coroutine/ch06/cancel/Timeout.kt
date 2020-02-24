package com.bennyhuo.kotlin.coroutine.ch06.cancel

import com.bennyhuo.kotlin.coroutine.common.api.User
import com.bennyhuo.kotlin.coroutine.common.api.githubApi
import kotlinx.coroutines.*

suspend fun main() {
    dummyTimeout()
//    smartTimeout()
}

suspend fun dummyTimeout(){
    GlobalScope.launch {
        val userDeferred = async {
            getUserSuspend()
        }

        val timeoutJob = launch {
            delay(5000)
            userDeferred.cancel()
        }

        val user = userDeferred.await()
        timeoutJob.cancel()
        println(user)
    }.join()
    println("End.")
}

suspend fun smartTimeout(){
    GlobalScope.launch {
        val user = withTimeout(5000) {
            getUserSuspend()
        }
        println(user)
    }.join()
    println("End.")
}

suspend fun getUserSuspend(): User {
    return githubApi.getUserSuspend("bennyhuo")
}