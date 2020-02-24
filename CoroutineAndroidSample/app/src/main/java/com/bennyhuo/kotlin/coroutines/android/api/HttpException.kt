package com.bennyhuo.kotlin.coroutines.android.api

import okhttp3.Response
import java.io.IOException

class HttpException(val response: Response) : IOException(response.toString())