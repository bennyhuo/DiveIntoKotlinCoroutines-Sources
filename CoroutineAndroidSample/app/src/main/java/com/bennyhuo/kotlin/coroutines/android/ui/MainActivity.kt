package com.bennyhuo.kotlin.coroutines.android.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import coil.ImageLoader
import coil.api.load
import coil.decode.SvgDecoder
import com.bennyhuo.kotlin.coroutines.android.R
import com.bennyhuo.kotlin.coroutines.android.api.GitUser
import com.bennyhuo.kotlin.coroutines.android.db.User
import com.bennyhuo.kotlin.coroutines.android.download.DownloadStatus.*
import com.bennyhuo.kotlin.coroutines.android.legacy.ImageAsyncTaskWithCallback
import com.bennyhuo.kotlin.coroutines.android.utils.alert
import com.bennyhuo.kotlin.coroutines.android.utils.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import splitties.alertdialog.appcompat.alertDialog
import splitties.alertdialog.appcompat.coroutines.showAndAwait
import splitties.alertdialog.appcompat.message


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private val listAdapter by lazy {
        ArrayAdapter<User>(this, android.R.layout.simple_list_item_1)
    }

    private val imageLoader by lazy {
        ImageLoader(this) {
            componentRegistry {
                add(SvgDecoder(this@MainActivity))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDialogButton.setOnClickListener {
            lifecycleScope.launch {
                val myChoice = alert("Warning!", "Do you want this?")
                toast("My choice is: $myChoice")

                val result = alertDialog {
                    message = "Dialog from Splitties"
                }.showAndAwait(
                    okValue = 1,
                    cancelValue = 0,
                    dismissValue = -1
                )
                toast("Result from splitties dialog: $result")
            }
        }

        mainViewModel.downloadStatusLiveData.observe(this) { downloadStatus ->

            when (downloadStatus) {
                null, None -> {
                    downloadButton.text = "Download"
                    downloadButton.setOnClickListener {
                        mainViewModel.download(
                            "https://kotlinlang.org/docs/kotlin-docs.pdf",
                            "Kotlin-Docs.pdf"
                        )
                    }
                }
                is Progress -> {
                    downloadButton.isEnabled = false
                    downloadButton.text = "Downloading (${downloadStatus.value})"
                }
                is Error -> {
                    toast(downloadStatus.throwable)
                    downloadButton.text = "Download Error"
                    downloadButton.isEnabled = true
                    downloadButton.setOnClickListener {
                        mainViewModel.download(
                            "https://kotlinlang.org/docs/kotlin-docs.pdf",
                            "Kotlin-Docs.pdf"
                        )
                    }
                }
                is Done -> {
                    toast("Done: ${downloadStatus.file.name}")
                    downloadButton.isEnabled = true
                    downloadButton.text = "Open File"
                    downloadButton.setOnClickListener {
                        Intent(Intent.ACTION_VIEW).also {
                            it.setDataAndType(
                                FileProvider.getUriForFile(
                                    this,
                                    "${packageName}.provider",
                                    downloadStatus.file
                                ), "application/pdf"
                            )
                            it.flags =
                                Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }.also(::startActivity)
                    }
                }
            }
        }

        userListView.adapter = listAdapter
        mainViewModel.usersLiveData.observe(this) { users ->
            listAdapter.clear()
            users?.let {
                userListView.visibility = View.VISIBLE
                listAdapter.addAll(it)
            } ?: run {
                userListView.visibility = View.GONE
            }
        }

        loadUsersFromDb.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.loadUsers()
            }
        }

        loadImageButton.setOnClickListener {
            lifecycleScope.launch {
                //                kotlinLogoView.load("https://resources.jetbrains.com/storage/img-old/kotlin_blog_logo.png").await()
                logoView.load(
                    "https://upload.wikimedia.org/wikipedia/commons/7/74/Kotlin-logo.svg",
                    imageLoader
                ).await()
            }
        }

        loadImage2Button.setOnClickListener {
            ImageAsyncTaskWithCallback(onComplete = { bitmap ->
                logoView.setImageBitmap(bitmap)
            }).execute("https://upload.wikimedia.org/wikipedia/commons/3/3e/Android_logo_2019.png")
        }

        mainViewModel.gitUserLiveData.observe(this) { gitUser: GitUser? ->
            gitUserTextView.text = gitUser.toString()
        }
        loadGitUser.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.loadGitUser()
            }
        }
    }
}
