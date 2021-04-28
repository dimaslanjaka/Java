package android

import android.content.Context
import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.snatik.storage.Storage
import java.io.File
import java.lang.reflect.Type
import java.net.CookieHandler
import java.net.CookiePolicy
import java.net.HttpCookie
import java.net.URI
import com.dimaslanjaka.library.helper.CookieHandling as LibraryCookieHandling
import java.net.CookieManager as JavaNetCookieManager

@Suppress("UsePropertyAccessSyntax")
class CookieHandling(
    context: Context,
    webview: android.webkit.WebView
) {
    private var storage = Storage(context)
    private var externalStorage = File(
        storage.externalStorageDirectory, "Facebot/cookies"
    ).apply {
        android.File.resolveDir(this)
    }

    private var coreCookieManager: WebkitCookieManagerProxy
    private var librarych: LibraryCookieHandling
    private var androidch: android.webkit.CookieManager
    var manager: JavaNetCookieManager
    var fileCookie = File(externalStorage, "default.json")

    init {
        android.webkit.CookieManager.getInstance().setAcceptCookie(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.CookieManager.getInstance().acceptThirdPartyCookies(webview)
        }

        coreCookieManager = WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(coreCookieManager)
        manager = coreCookieManager.toJavaNetCookieManager()
        androidch = coreCookieManager.toAndroidWebkitCookieManager()

        librarych = LibraryCookieHandling(fileCookie, coreCookieManager)
        webview.webViewClient = CookieHandlingWebviewClient()

        loadCookie()

        instance = this
    }

    fun saveCookie(uri: URI, cookies: String?) {
        cookies?.let { c ->
            val splitcomma = c.split("; ")
            splitcomma.map { cookie ->
                val spliteq = cookie.split("=")
                manager.cookieStore.add(uri, httpCookieCreate(uri, spliteq[0], spliteq[1]))
            }
        }
        saveCookie()
    }

    private fun httpCookieCreate(uri: URI, cname: String, cvalue: String): HttpCookie {
        val httpCookie = HttpCookie(cname, cvalue)
        httpCookie.domain = uri.host
        httpCookie.path = uri.path
        //println(uri.scheme)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            httpCookie.isHttpOnly = uri.scheme!! == "http"
        }
        httpCookie.secure = uri.scheme!! == "https"

        return httpCookie
    }

    fun saveCookie() {
        librarych.saveCookie()
    }

    fun loadCookie() {
        librarych.loadCookie()
    }

    fun changeFileCookie(fileCookie: File) {
        librarych.changeFileCookie(fileCookie)
    }

    companion object {
        @JvmStatic
        lateinit var instance: CookieHandling
    }

    @Suppress("unused")
    private inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, genericType<T>())

    @Suppress("unused")
    private inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type

    @Suppress("unused")
    private fun gson(): Gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
}