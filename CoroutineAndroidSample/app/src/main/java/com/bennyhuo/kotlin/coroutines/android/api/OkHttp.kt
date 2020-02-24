package com.bennyhuo.kotlin.coroutines.android.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import timber.log.Timber

val okHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor{
    message -> Timber.d(message)
}.setLevel(Level.BASIC)).build()