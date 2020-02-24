package com.bennyhuo.kotlin.coroutines.server.upload

import com.bennyhuo.kotlin.coroutines.server.Context
import com.bennyhuo.kotlin.coroutines.server.utils.md5
import io.ktor.application.call
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlinx.serialization.Serializable
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class UploadResponse(val url: String, val md5: String)

val uploadDir = File("static").also { it.mkdirs() }

fun Route.upload(){
    post("upload") {
        val multipart = call.receiveMultipart()
        val uploadedFiles = mutableListOf<UploadResponse>()
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    val file = File(uploadDir, "upload-${System.currentTimeMillis()}-${part.originalFileName}")
                    part.streamProvider().use { input ->
                        file.outputStream().buffered().use { output ->
                            input.copyTo(output)
                        }
                    }
                    uploadedFiles += UploadResponse(Context.urlOf(file), file.md5())
                }
            }
            part.dispose()
        }
        call.respond(uploadedFiles)
    }
}