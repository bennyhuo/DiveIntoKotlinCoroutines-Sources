package com.bennyhuo.kotlin.coroutine.js

external fun setTimeout(
    handler: dynamic,
    timeout: Int = definedExternally,
    vararg arguments: dynamic
): Int

external fun clearTimeout(handle: Int = definedExternally)

fun hello() {
    console.log("Hello World")
    setTimeout({
        console.log("Run after 1000ms.")
    }, 1000)

    setTimeout({
        console.log("Not specify delay.")
    })

    setTimeout({
        a: Int, b: Int ->
        console.log(a + b)
    }, 500, 1, 3)
}