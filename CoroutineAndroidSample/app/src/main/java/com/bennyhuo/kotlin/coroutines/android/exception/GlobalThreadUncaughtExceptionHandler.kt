package com.bennyhuo.kotlin.coroutines.android.exception

import timber.log.Timber
import java.lang.Thread.UncaughtExceptionHandler

//For threads.
class GlobalThreadUncaughtExceptionHandler : UncaughtExceptionHandler {

    companion object {
        fun setUp() {
            Thread.setDefaultUncaughtExceptionHandler(GlobalThreadUncaughtExceptionHandler())
        }
    }

    //Don't use lazy here.
    private val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        Timber.d(e, "Uncaugth exception in thread: ${t.name}")
        defaultUncaughtExceptionHandler?.uncaughtException(t, e)
    }
}