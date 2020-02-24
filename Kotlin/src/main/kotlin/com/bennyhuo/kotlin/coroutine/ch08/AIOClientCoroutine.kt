package com.bennyhuo.kotlin.coroutine.ch08

import kotlinx.coroutines.suspendCancellableCoroutine
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun main() {
    val serverAddress = InetSocketAddress(SERVER_HOST, SERVER_PORT)
    AsynchronousSocketChannel.open().use { serverChannel ->
        runCatching {
            serverChannel.connectAsync(serverAddress)
            println("Connected to $SERVER_HOST:$SERVER_PORT ...")
            val value = "Java"
            serverChannel.write(ByteBuffer.wrap(value.toByteArray()))
            println("sending: $value")
            val buffer = ByteBuffer.allocate(128)
            serverChannel.readAsync(buffer)
            buffer.flip()
            println("receiving: ${CHARSET.decode(buffer)}")
        }.onFailure {
            println("Error: $it")
        }
    }
}

suspend fun AsynchronousSocketChannel.connectAsync(remote: SocketAddress) = suspendCancellableCoroutine<Unit> { continuation ->
    connect(remote, continuation, object: CompletionHandler<Void, Continuation<Unit>>{
        override fun completed(result: Void?, attachment: Continuation<Unit>?) {
            continuation.resume(Unit)
        }

        override fun failed(exc: Throwable, attachment: Continuation<Unit>?) {
            continuation.resumeWithException(exc)
        }
    })
    continuation.invokeOnCancellation { close() }
}

suspend fun AsynchronousSocketChannel.readAsync(buffer: ByteBuffer) = suspendCancellableCoroutine<Int> { continuation ->
    read(buffer, continuation, object: CompletionHandler<Int, Continuation<Int>>{
        override fun completed(result: Int, attachment: Continuation<Int>?) {
            continuation.resume(result)
        }

        override fun failed(exc: Throwable, attachment: Continuation<Int>?) {
            continuation.resumeWithException(exc)
        }
    })
    continuation.invokeOnCancellation { close() }
}

