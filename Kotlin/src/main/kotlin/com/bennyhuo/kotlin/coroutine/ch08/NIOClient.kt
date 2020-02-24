package com.bennyhuo.kotlin.coroutine.ch08

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

fun main() {
    val serverAddress = InetSocketAddress(SERVER_HOST, SERVER_PORT)
    SocketChannel.open(serverAddress).use { serverChannel ->
        println("Connected to $SERVER_HOST:$SERVER_PORT ...")
        val values = listOf("Java")//, "Kotlin", "Lua", "Dart", "JavaScript")

        values.forEach { value ->
            serverChannel.write(ByteBuffer.wrap(value.toByteArray()))
            println("sending: $value")
            val buffer = ByteBuffer.allocate(128)
            val length = serverChannel.read(buffer)
            buffer.flip()
            println("receiving: ${CHARSET.decode(buffer)}")
            Thread.sleep(2000)
        }
    }
}
