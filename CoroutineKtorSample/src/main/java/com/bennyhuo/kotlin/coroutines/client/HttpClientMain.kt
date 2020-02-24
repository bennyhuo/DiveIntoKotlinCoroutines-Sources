package com.bennyhuo.kotlin.coroutines.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get


data class GitUser(val login: String, val avatar_url: String, val location: String)

suspend fun main(){
    val client = HttpClient(OkHttp){
        install(JsonFeature)
    }

    // Get the content of an URL.
    val user = client.get<GitUser>("https://api.github.com/users/bennyhuo")

    println(user)

    client.close()
}