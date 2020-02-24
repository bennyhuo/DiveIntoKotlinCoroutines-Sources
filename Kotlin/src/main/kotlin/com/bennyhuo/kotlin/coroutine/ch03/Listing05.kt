package com.bennyhuo.kotlin.coroutine.ch03

suspend fun suspendMain(){

}

//真正的程序入口
fun main() {
    runSuspend {
        suspendMain()
    }
}