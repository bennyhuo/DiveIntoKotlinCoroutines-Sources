package com.bennyhuo.kotlin.coroutine.common.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Api {

}

val httpClient by lazy {
    OkHttpClient.Builder().addInterceptor(Interceptor {
        it.proceed(it.request()).apply {
            println("request: ${code()}")
        }
    }).build()
}

val api by lazy {
    val retrofit = Retrofit.Builder()
        .client(
            httpClient
        )
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(
        Api::class.java)
}