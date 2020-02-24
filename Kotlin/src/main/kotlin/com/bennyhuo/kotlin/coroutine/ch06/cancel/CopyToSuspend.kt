package com.bennyhuo.kotlin.coroutine.ch06.cancel

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.yield
import java.io.InputStream
import java.io.OutputStream
import kotlin.coroutines.coroutineContext


@UseExperimental(InternalCoroutinesApi::class)
suspend fun InputStream.copyToSuspend(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)

    val job = coroutineContext[Job]

    while (bytes >= 0) {
        out.write(buffer, 0, bytes)
        bytesCopied += bytes

        job?.let { it.takeIf { it.isActive } ?: throw job.getCancellationException() }

        bytes = read(buffer)
    }
    return bytesCopied
}


@UseExperimental(InternalCoroutinesApi::class)
suspend fun InputStream.copyToSuspend2(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        yield()
        bytes = read(buffer)
    }
    return bytesCopied
}


