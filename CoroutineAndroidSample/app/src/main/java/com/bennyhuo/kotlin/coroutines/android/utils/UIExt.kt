package com.bennyhuo.kotlin.coroutines.android.utils

import android.widget.Toast
import com.bennyhuo.kotlin.coroutines.android.appContext

fun toast(message: Any?){
    Toast.makeText(appContext, message.toString(), Toast.LENGTH_SHORT).show()
}