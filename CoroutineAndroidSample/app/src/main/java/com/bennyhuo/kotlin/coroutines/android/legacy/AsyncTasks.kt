package com.bennyhuo.kotlin.coroutines.android.legacy

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.bennyhuo.kotlin.coroutines.android.api.HttpException
import com.bennyhuo.kotlin.coroutines.android.api.okHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class ImageAsyncTask : AsyncTask<String, Int, List<Bitmap>>() {
    override fun doInBackground(vararg params: String): List<Bitmap> {
        return params.mapIndexed { index, url ->
            publishProgress(index * 100 / params.size)
            ImageManager.getBitmapSync(url)
        }.also { publishProgress(100) }
    }

    override fun onPostExecute(result: List<Bitmap>) {
        // 更新结果
    }

    override fun onProgressUpdate(vararg values: Int?) {
        // 更新进度
    }
}

class CallbackAsyncTask<P, R>(
    private val action: (P) -> R,
    private val onProgress: ((Int) -> Unit)? = null,
    private val onComplete: ((R) -> Unit)? = null
) : AsyncTask<P, Int, List<R>>() {

    override fun doInBackground(vararg params: P): List<R> {
        return params.mapIndexed { index, param ->
            publishProgress(index * 100 / params.size)
            action(param)
        }.also { publishProgress(100) }
    }

    override fun onPostExecute(result: List<R>) {
        onComplete?.let(result::forEach)
    }

    override fun onProgressUpdate(vararg values: Int?) {
        values[0]?.let { onProgress?.invoke(it) }
    }
}

class ImageAsyncTaskWithCallback(
    private val onProgress: ((Int) -> Unit)? = null,
    private val onComplete: ((Bitmap) -> Unit)? = null
) : AsyncTask<String, Int, List<Bitmap>>() {

    override fun doInBackground(vararg params: String): List<Bitmap> {
        return params.mapIndexed { index, url ->
            publishProgress(index * 100 / params.size)
            ImageManager.getBitmapSync(url)
        }.also { publishProgress(100) }
    }

    override fun onPostExecute(result: List<Bitmap>) {
        onComplete?.let(result::forEach)
    }

    override fun onProgressUpdate(vararg values: Int?) {
        values[0]?.let { onProgress?.invoke(it) }
    }
}


fun getImage(url: String) {
    val imageAsyncTask = ImageAsyncTask()
    imageAsyncTask.execute(url)
}

object ImageManager {
    fun getBitmapSync(url: String): Bitmap {
        return download(url)
    }

    fun download(url: String): Bitmap {
        return getAsStream(url).use { inputStream ->
            val bos = ByteArrayOutputStream()
            inputStream.copyTo(bos)
            bos.toByteArray()
        }.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }

    fun getAsStream(url: String): InputStream =
        okHttpClient.newCall(
            Request.Builder().get().url(url).build()
        ).execute().body()?.byteStream()
            ?: throw IOException("No body")

}