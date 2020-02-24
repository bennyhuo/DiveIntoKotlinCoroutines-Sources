package com.bennyhuo.kotlin.coroutines.server

import java.io.File

object Context {
    val host = "http://localhost:8081"

    val workDir = File(".").absoluteFile

    fun urlOf(file: File): String {
        return "${host}/${file.absoluteFile.relativeTo(workDir).path}"
    }
}