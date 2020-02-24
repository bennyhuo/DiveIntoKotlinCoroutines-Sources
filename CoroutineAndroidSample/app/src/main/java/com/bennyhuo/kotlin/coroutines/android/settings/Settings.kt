package com.bennyhuo.kotlin.coroutines.android.settings

import com.bennyhuo.kotlin.coroutines.android.utils.pref

object Settings {

    var firstTimeLaunch by pref(true)

}