package com.bennyhuo.kotlin.coroutine.js

import Axios
import AxiosResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.*
import kotlinx.io.core.use
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class User(val login: String, val avatar_url: String, val location: String)

suspend fun simpleCoroutine() {
    val job = GlobalScope.launch {
        println("1")
        delay(1000)
        println("2")
    }
    job.join()
}

fun promisifiedApi() {
    Axios.get<User, AxiosResponse<User>>("https://api.github.com/users/bennyhuo")
        .then {
            console.log(it.status)
            console.log(it.data)
            console.log(it.data.avatar_url)
        }.catch {
            console.error(it)
        }
}

suspend fun awaitPromise() {
    val response = Axios.get<User, AxiosResponse<User>>("https://api.github.com/users/bennyhuo").await()
    console.log(response.status)
    console.log(response.data)
}

suspend fun promiseAsDeferred() {
    val responseDeferred = Axios.get<User, AxiosResponse<User>>("https://api.github.com/users/bennyhuo").asDeferred()
    val response = responseDeferred.await()
    console.log(response.status)
    console.log(response.data)
}

suspend fun promiseBuilder() {
    GlobalScope.async { 1 }
        .asPromise()
        .then { }
        .catch { }

    GlobalScope.promise { 1 }
        .then {}
        .catch { }
}

suspend fun ktorSample() {
    val client = HttpClient {
        install(JsonFeature){
            serializer = KotlinxSerializer(Json.nonstrict)
        }
    }
    val user = client.get<User>("https://api.github.com/users/bennyhuo")
    console.log(user)
    client.close()
}