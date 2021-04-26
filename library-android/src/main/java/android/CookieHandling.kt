package android

import android.annotation.SuppressLint
import android.content.Context
import com.dimaslanjaka.library.helper.CookieHandling
import com.dimaslanjaka.library.helper.CookieHandlingInterface
import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

@Suppress("UsePropertyAccessSyntax")
class CookieHandling(webview: android.webkit.WebView, cookieFile: File, androidWebkitCm: WebkitCookieManagerProxy) :
    CookieHandlingInterface {
    var handler: CookieHandling
    override var manager: CookieManager = androidWebkitCm
    override var fileCookie = cookieFile
    var androidCm = androidWebkitCm

    init {
        androidCm.getAndroidWebkitInstance().setAcceptCookie(true)
        androidCm.acceptThirdPartyCookies(webview)
        androidCm.setAcceptCookie(true)
        val coreCookieManager = WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(coreCookieManager)
        handler = CookieHandling(cookieFile, coreCookieManager)
    }

    override fun saveCookie() {
        handler.saveCookie()
    }

    override fun loadCookie() {
        handler.loadCookie()
    }

    override fun changeFileCookie(fileCookie: File) {
        handler.changeFileCookie(fileCookie)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}