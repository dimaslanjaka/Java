package com.dimaslanjaka.browser

import android.CookieHandling
import android.File.Companion.resolveDir
import android.WebkitCookieManagerProxy
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import android.webkit.*
import com.google.gson.GsonBuilder
import com.snatik.storage.Storage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.CookieHandler

class AndroidCookieHandling(context: Context, web: WebView) {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    var manager: WebkitCookieManagerProxy = WebkitCookieManagerProxy()
    private var storage = Storage(context)
    private var externalStorage = File(storage.externalStorageDirectory, "Facebot/cookies").apply {
        resolveDir(this)
    }
    var fileCookie = File(externalStorage, "default.json")
    var cookieHandler = CookieHandling(fileCookie, manager)

    init {
        CookieHandler.setDefault(manager)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(web, true)
        }
        web.webViewClient = wClient()
        loadCookie()
    }

    fun loadCookie() {
        cookieHandler.loadCookie()
    }

    fun saveCookies() {
        cookieHandler.saveCookie()
    }

    fun changeFileCookie(newCookieLocation: File) {
        cookieHandler.changeCookieFile(newCookieLocation)
    }

    @Throws(IOException::class)
    fun writeFile(fileName: String?, data: String) {
        val outFile = File(Environment.getExternalStorageDirectory(), fileName)
        val out = FileOutputStream(outFile, false)
        val contents = data.toByteArray()
        out.write(contents)
        out.flush()
        out.close()
    }

    inner class wClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d("ERROR", request.url.toString())
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            CookieSyncManager.getInstance().sync()
            val cookies = CookieManager.getInstance().getCookie(url)
            //if (cookies != null) Log.d("COOKIE", cookies)
            saveCookies()
        }
    }

    companion object {
        val isSDCARDAvailable: Boolean
            get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}