package com.bennyhuo.kotlin.coroutine.native

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


fun sayHello(): String = "Hello, Kotlin/Native from ${platform()}!"

suspend fun commonMain() {
    println(sayHello())

    GlobalScope.launch {
        println("1")
        delay(1000)
        println("2")
        ktorSample()
    }.join()
}

@Serializable
data class User(val login: String, val avatar_url: String, val location: String)

suspend fun ktorSample() {
    val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json.nonstrict)
        }
    }
    val user = client.get<User>("https://api.github.com/users/bennyhuo")
    println(user)
    client.close()
}