package com.bennyhuo.kotlin.coroutine.ch04.lua

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface SymCoroutineScope<T> {
    suspend fun <P> transfer(symCoroutine: SymCoroutine<P>, value: P): T
}

class SymCoroutine<T>(
    override val context: CoroutineContext = EmptyCoroutineContext,
    private val block: suspend SymCoroutineScope<T>.(T) -> Unit
) : Continuation<T> {

    companion object {
        lateinit var main: SymCoroutine<Any?>

        suspend fun main(block: suspend SymCoroutineScope<Any?>.() -> Unit) {
            SymCoroutine<Any?> { block() }.also { main = it }.start(Unit)
        }

        fun <T> create(
            context: CoroutineContext = EmptyCoroutineContext,
            block: suspend SymCoroutineScope<T>.(T) -> Unit
        ): SymCoroutine<T> {
            return SymCoroutine(context, block)
        }
    }

    class Parameter<T>(val coroutine: SymCoroutine<T>, val value: T)

    val isMain: Boolean
        get() = this == main

    private val body: SymCoroutineScope<T> = object : SymCoroutineScope<T> {
        private tailrec suspend fun <P> transferInner(symCoroutine: SymCoroutine<P>, value: Any?): T{
            if(this@SymCoroutine.isMain){
                return if(symCoroutine.isMain){
                    value as T
                } else {
                    val parameter = symCoroutine.coroutine.resume(value as P)
                    transferInner(parameter.coroutine, parameter.value)
                }
            } else {
                coroutine.run {
                    return yield(Parameter(symCoroutine, value as P))
                }
            }
        }

        override suspend fun <P> transfer(symCoroutine: SymCoroutine<P>, value: P): T {
            return transferInner(symCoroutine, value)
        }
    }

    private val coroutine = Coroutine<T, Parameter<*>>(context) {
        Parameter(this@SymCoroutine, suspend {
            block(body, it)
            if(this@SymCoroutine.isMain) Unit else throw IllegalStateException("SymCoroutine cannot be dead.")
        }() as T)
    }

    override fun resumeWith(result: Result<T>) {
        throw IllegalStateException("SymCoroutine cannot be dead!")
    }

    suspend fun start(value: T){
        coroutine.resume(value)
    }
}

object SymCoroutines {
    val coroutine0: SymCoroutine<Int> = SymCoroutine.create<Int> { param: Int ->
        println("coroutine-0 $param")
        var result = transfer(coroutine2, 0)
        println("coroutine-0 1 $result")
        result = transfer(SymCoroutine.main, Unit)
        println("coroutine-0 1 $result")
    }

    val coroutine1: SymCoroutine<Int> = SymCoroutine.create { param: Int ->
        println("coroutine-1 $param")
        val result = transfer(coroutine0, 1)
        println("coroutine-1 1 $result")
    }

    val coroutine2: SymCoroutine<Int> = SymCoroutine.create { param: Int ->
        println("coroutine-2 $param")
        var result = transfer(coroutine1, 2)
        println("coroutine-2 1 $result")
        result = transfer(coroutine0, 2)
        println("coroutine-2 2 $result")
    }
}

suspend fun main() {

    SymCoroutine.main {
        println("main 0")
        val result = transfer(SymCoroutines.coroutine2, 3)
        println("main end $result")
    }

}
