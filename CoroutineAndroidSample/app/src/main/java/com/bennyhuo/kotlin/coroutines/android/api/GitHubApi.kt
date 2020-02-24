package com.bennyhuo.kotlin.coroutines.android.api

import com.bennyhuo.kotlin.coroutines.android.db.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import timber.log.Timber

data class GitUser(val id: String, val name: String, val url: String)

val gitHubServiceApi by lazy {
    val retrofit = retrofit2.Retrofit.Builder()
        .client(OkHttpClient.Builder().addInterceptor(Interceptor {
            it.proceed(it.request()).apply {
                Timber.d("request: ${code()}")
            }
        }).build())
        .baseUrl("https://api.github.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    retrofit.create(GitHubServiceApi::class.java)
}

interface GitHubServiceApi {

    @GET("users/{login}")
    fun getUserCallback(@Path("login") login: String): Call<GitUser>

    @GET("users/{login}")
    suspend fun getUserSuspend(@Path("login") login: String): GitUser

}