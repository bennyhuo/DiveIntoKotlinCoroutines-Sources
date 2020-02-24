package com.bennyhuo.kotlin.coroutines.android.exception

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

//For coroutines.
class GlobalCoroutineExceptionHandler: CoroutineExceptionHandler {
    override val key = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Timber.e(exception, "Unhandled Coroutine Exception with ${context[Job]}")
    }
}