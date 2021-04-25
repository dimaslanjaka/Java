package android

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import com.dimaslanjaka.library.helper.CookieHandling
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

class CookieHandling {
    lateinit var handler: CookieHandling
    lateinit var androidManager: android.webkit.CookieManager
    lateinit var webview: android.webkit.WebView

    constructor(file: File) {
        handler = CookieHandling(file)
    }

    constructor(fileCookie: File, manager: CookieManager) {
        handler = CookieHandling(fileCookie, manager)
    }

    constructor(fileCookie: File, androidManager: android.webkit.CookieManager) {
        this.androidManager = androidManager

        // unrelated, just make sure cookies are generally allowed
        android.webkit.CookieManager.getInstance().setAcceptCookie(true)

        // magic starts here
        val coreCookieManager = WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(coreCookieManager)

        handler = CookieHandling(fileCookie, coreCookieManager)
    }

    fun androidWebkitSetup() {
        val androidManager = android.webkit.CookieManager.getInstance()
        androidManager.setAcceptCookie(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            androidManager.acceptThirdPartyCookies(webview)
        }
    }

    fun changeHandler(handler: CookieHandling) {
        this.handler = handler
    }

    fun saveCookie() {
        handler.saveCookie()
    }

    fun loadCookie() {
        handler.loadCookie()
    }

    fun changeCookieFile(file: File) {
        handler.changeCookieFile(file)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}