package com.bennyhuo.kotlin.coroutine.ch03

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun notSuspend() = suspendCoroutine<Int> { continuation ->
    continuation.resume(100)
}