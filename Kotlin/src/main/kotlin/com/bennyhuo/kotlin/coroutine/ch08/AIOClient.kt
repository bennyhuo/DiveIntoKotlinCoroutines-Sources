package com.bennyhuo.kotlin.coroutine.ch08

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler

fun main() {
    val serverAddress = InetSocketAddress(SERVER_HOST, SERVER_PORT)
    AsynchronousSocketChannel.open().let { serverChannel ->
        serverChannel.connect(serverAddress, null,
            object : CompletionHandler<Void, Any?> {
                override fun completed(result: Void?, attachment: Any?) {
                    println("Connected to $SERVER_HOST:$SERVER_PORT ...")
                    val value = "Java"
                    serverChannel.write(ByteBuffer.wrap(value.toByteArray()))
                    println("sending: $value")
                    val buffer = ByteBuffer.allocate(128)
                    serverChannel.read(buffer, null,
                        object : CompletionHandler<Int, Any?> {
                            override fun completed(length: Int, attachment: Any?) {
                                buffer.flip()
                                println("receiving: ${CHARSET.decode(buffer)}")
                                serverChannel.close()
                            }

                            override fun failed(exc: Throwable, attachment: Any?) {
                                println("Read error: $exc")
                            }
                        })
                }

                override fun failed(exc: Throwable, attachment: Any?) {
                    println("Failed to connect to $SERVER_HOST:$SERVER_PORT.")
                }
            })
    }

    Thread.sleep(5000)
}
