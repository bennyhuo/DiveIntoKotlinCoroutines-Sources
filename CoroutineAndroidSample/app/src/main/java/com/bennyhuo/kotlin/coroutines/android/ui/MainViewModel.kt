package com.bennyhuo.kotlin.coroutines.android.ui

import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.concurrent.futures.DirectExecutor
import androidx.concurrent.futures.await
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bennyhuo.kotlin.coroutines.android.api.GitUser
import com.bennyhuo.kotlin.coroutines.android.api.gitHubServiceApi
import com.bennyhuo.kotlin.coroutines.android.db.Db
import com.bennyhuo.kotlin.coroutines.android.db.User
import com.bennyhuo.kotlin.coroutines.android.download.DownloadManager
import com.bennyhuo.kotlin.coroutines.android.download.DownloadManagerRx
import com.bennyhuo.kotlin.coroutines.android.download.DownloadStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

class MainViewModel : ViewModel() {

    val downloadStatusLiveData = MutableLiveData<DownloadStatus>(DownloadStatus.None)

    val usersLiveData = MutableLiveData<List<User>>()

    val gitUserLiveData = MutableLiveData<GitUser>()

    private val useCoroutine = true

    fun download(url: String, fileName: String) {
        if (useCoroutine) {
            viewModelScope.launch {
                downloadSuspend(url, fileName)
            }
        } else {
            downloadRx(url, fileName)
        }
    }

    private suspend fun downloadSuspend(url: String, fileName: String) {
        Timber.d("use coroutine.")
        DownloadManager.download(url, fileName)
            .flowOn(Dispatchers.IO)
            .collect {
                downloadStatusLiveData.value = it
            }
    }

    private fun downloadRx(url: String, fileName: String) {
        Timber.d("use RxJava")
        DownloadManagerRx.download(url, fileName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                downloadStatusLiveData.value = it
            }
    }

    private fun downloadRx2Coroutine(url: String, fileName: String) {
        DownloadManagerRx.download(url, fileName)
            .subscribeOn(Schedulers.io())
            .asFlow()
            //.flowOn(Dispatchers.IO)
            .onEach {
                Timber.d("onEach: $it")
            }
            .launchIn(viewModelScope)
    }

    suspend fun loadUsers() {
        usersLiveData.value = Db.userDao.listUsers()
    }

    suspend fun loadGitUser() {
        gitUserLiveData.value = CallbackToFutureAdapter.getFuture<GitUser> { completer ->
            val call = gitHubServiceApi.getUserCallback("bennyhuo")
            completer.addCancellationListener(Runnable { call.cancel() }, DirectExecutor.INSTANCE)
            call.enqueue(object : Callback<GitUser> {
                override fun onFailure(call: Call<GitUser>, t: Throwable) {
                    completer.setException(t)
                }

                override fun onResponse(call: Call<GitUser>, response: Response<GitUser>) {
                    if (!response.isSuccessful) {
                        completer.setException(HttpException(response))
                    } else {
                        response.body()?.let(completer::set)
                            ?: completer.setException(NullPointerException())
                    }
                }
            })
        }.await()
    }
}