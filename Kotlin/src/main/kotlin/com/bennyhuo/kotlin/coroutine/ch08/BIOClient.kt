package com.bennyhuo.kotlin.coroutine.ch08

import java.net.InetSocketAddress
import java.net.Socket

fun main() {
    val serverAddress = InetSocketAddress(SERVER_HOST, SERVER_PORT)
    Socket().apply {
        connect(serverAddress)
        println("Connecting to $SERVER_HOST:$SERVER_PORT ...")
        val values = listOf("Java")//, "Kotlin", "Lua", "Dart", "JavaScript")
        val byteArray = ByteArray(128)
        values.forEach { value ->
            getOutputStream().write(value.toByteArray())
            val length = getInputStream().read(byteArray)
            println("Receiving: ${String(byteArray, 0, length)}")
            Thread.sleep(2000)
        }
    }
}
