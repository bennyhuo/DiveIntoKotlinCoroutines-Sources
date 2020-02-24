package com.bennyhuo.kotlin.coroutines.server.utils

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun File.md5(): String {
    return if (isFile) try {
        val digest = MessageDigest.getInstance("MD5")
        inputStream().use {
            val buffer = ByteArray(1024)
            while (true) {
                val byteCount = it.read(buffer)
                if (byteCount <= 0) break
                digest.update(buffer, 0, byteCount)
            }
        }
        String.format("%032x", BigInteger(1, digest.digest()))
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    } else ""
}

fun ByteArray.md5(): String {
    return try {
        val digest = MessageDigest.getInstance("MD5")
        BigInteger(1, digest.digest(this)).toString(16)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}