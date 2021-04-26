package com.dimaslanjaka.browser

import android.CookieHandling
import android.CookieHandlingWebviewClient
import android.File.Companion.resolveDir
import android.WebkitCookieManagerProxy
import android.content.Context
import android.os.Build
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.WebView
import com.snatik.storage.Storage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.CookieHandler

class WebviewHandler(context: Context, web: WebView) {
    var manager = WebkitCookieManagerProxy(context)
    private var storage = Storage(context)
    private var externalStorage = File(storage.externalStorageDirectory, "Facebot/cookies").apply {
        resolveDir(this)
    }
    var fileCookie = File(externalStorage, "default.json")
    var cookieHandler = CookieHandling(web, fileCookie, manager)

    init {
        CookieHandler.setDefault(manager)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(web, true)
        }
        web.webViewClient = CookieHandlingWebviewClient(cookieHandler)
    }

    @Throws(IOException::class)
    fun writeFile(fileName: String, data: String) {
        val outFile = File(fileName)
        val out = FileOutputStream(outFile, false)
        val contents = data.toByteArray()
        out.write(contents)
        out.flush()
        out.close()
    }



    companion object {
        val isSDCARDAvailable: Boolean
            get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}