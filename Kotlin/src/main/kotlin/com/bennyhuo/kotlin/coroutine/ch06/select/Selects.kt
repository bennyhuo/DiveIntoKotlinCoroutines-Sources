package com.bennyhuo.kotlin.coroutine.ch06.select

import com.bennyhuo.kotlin.coroutine.common.api.User
import com.bennyhuo.kotlin.coroutine.common.api.githubApi
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import java.io.File

val localDir = File("localCache").also { it.mkdirs() }

val gson = Gson()

fun CoroutineScope.getUserFromApi(login: String) = async(Dispatchers.IO){
    githubApi.getUserSuspend(login)
}

fun CoroutineScope.getUserFromLocal(login:String) = async(Dispatchers.IO){
    File(localDir, login).takeIf { it.exists() }?.readText()?.let { gson.fromJson(it, User::class.java) }
}

fun cacheUser(login: String, user: User){
    File(localDir, login).writeText(gson.toJson(user))
}

data class Response<T>(val value: T, val isLocal: Boolean)

suspend fun main() {
    val login = "bennyhuo"
    GlobalScope.launch {
        val localDeferred = getUserFromLocal(login)
        val remoteDeferred = getUserFromApi(login)

        val userResponse = select<Response<User?>> {
            localDeferred.onAwait { Response(it, true) }
            remoteDeferred.onAwait { Response(it, false) }
        }

        userResponse.value?.let { println(it) }
        userResponse.isLocal.takeIf { it }?.let {
            val userFromApi = remoteDeferred.await()
            cacheUser(login, userFromApi)
            println(userFromApi)
        }
    }.join()
}