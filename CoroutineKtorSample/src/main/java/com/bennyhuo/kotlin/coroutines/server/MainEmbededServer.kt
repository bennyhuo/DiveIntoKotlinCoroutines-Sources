package com.bennyhuo.kotlin.coroutines.server

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.serialization
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val age: Int)

suspend fun main(){
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation){
            serialization()
        }
        routing {
            get("/Hello") {
                call.respond("Hello Ktor!!")
            }
            get("/user"){
                call.respond(User("bennyhuo", 30))
            }
        }
    }.start(true)
}