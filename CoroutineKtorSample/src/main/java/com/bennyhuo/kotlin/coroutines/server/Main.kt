package com.bennyhuo.kotlin.coroutines.server

import com.bennyhuo.kotlin.coroutines.server.upload.upload
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.serialization
import java.io.File


fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) { serialization() }
    install(Routing)

    routing {
        upload()
        static("static") {
            files("static")
        }

        get("/Hello") {
            call.respond("Hello Ktor!!")
        }
    }
}