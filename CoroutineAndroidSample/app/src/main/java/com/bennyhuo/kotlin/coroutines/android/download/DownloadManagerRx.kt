package com.bennyhuo.kotlin.coroutines.android.download

import com.bennyhuo.kotlin.coroutines.android.api.HttpException
import com.bennyhuo.kotlin.coroutines.android.api.okHttpClient
import com.bennyhuo.kotlin.coroutines.android.appContext
import com.bennyhuo.kotlin.coroutines.android.utils.copyTo
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import okhttp3.Request
import timber.log.Timber
import java.io.File

object DownloadManagerRx {

    private val downloadDirectory by lazy {
        File(appContext.filesDir, "download").also { it.mkdirs() }
    }

    fun download(url: String, fileName: String): Flowable<DownloadStatus> {
        val file = File(downloadDirectory, fileName)
        return Flowable.create<DownloadStatus>({
            val request = Request.Builder().url(url).get().build()
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                response.body()!!.let { body ->
                    val total = body.contentLength()
                    file.outputStream().use { output ->
                        val input = body.byteStream()
                        var emittedProgress = 0L
                        input.copyTo(output) { bytesCopied ->
                            val progress = bytesCopied * 100 / total
                            if (progress - emittedProgress > 5) {
                                Timber.d("new progress: $progress")
                                Thread.sleep(1000)
                                it.onNext(DownloadStatus.Progress(progress.toInt()))
                                emittedProgress = progress
                            }
                        }
                        input.close()
                    }
                    it.onNext(DownloadStatus.Done(file))
                }
            } else {
                throw HttpException(response)
            }
            it.onComplete()
        }, BackpressureStrategy.LATEST).onErrorReturn {
            file.delete()
            DownloadStatus.Error(it)
        }
    }
}

