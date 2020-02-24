package com.bennyhuo.kotlin.coroutine.common.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

val githubApi by lazy {
    val retrofit = retrofit2.Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(Interceptor {
                it.proceed(it.request()).apply {
                    println("request: ${code()}")
                }
            }).build())
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    retrofit.create(
        GitHubApi::class.java)
}

interface GitHubApi {
    @GET("users/{login}")
    fun getUserCallback(@Path("login") login: String): Call<User>

    @GET("users/{login}")
    suspend fun getUserSuspend(@Path("login") login: String): User
}

data class User(val id: String, val name: String, val url: String)