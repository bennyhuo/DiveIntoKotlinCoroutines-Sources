package com.bennyhuo.kotlin.coroutines.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bennyhuo.kotlin.coroutines.android.R
import kotlinx.android.synthetic.main.activity_scoped.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ScopedActivity:  AppCompatActivity(){

    private val mainScope by lazy { MainScope() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoped)

        button.setOnClickListener {
            mainScope.launch {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}