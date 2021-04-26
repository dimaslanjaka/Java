package android

import android.content.Context
import android.os.Build
import com.dimaslanjaka.library.helper.CookieHandlingInterface
import com.snatik.storage.Storage
import java.io.File
import java.lang.reflect.Type
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import com.dimaslanjaka.library.helper.CookieHandling as LibraryCookieHandling

@Suppress("UsePropertyAccessSyntax")
class CookieHandling(
    context: Context,
    webview: android.webkit.WebView
) :
    CookieHandlingInterface {
    private var storage = Storage(context)
    private var externalStorage = File(
        storage.externalStorageDirectory, "Facebot/cookies"
    ).apply {
        android.File.resolveDir(this)
    }

    private var handler: LibraryCookieHandling
    override lateinit var manager: CookieManager
    override var fileCookie = File(externalStorage, "default.json")

    init {
        android.webkit.CookieManager.getInstance().setAcceptCookie(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.webkit.CookieManager.getInstance().acceptThirdPartyCookies(webview)
        }

        val coreCookieManager = WebkitCookieManagerProxy(null, CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(coreCookieManager)
        manager = coreCookieManager.toJavaNetCookieManager()

        handler = LibraryCookieHandling(fileCookie, coreCookieManager)
        webview.webViewClient = CookieHandlingWebviewClient()

        instance = this
    }

    override fun saveCookie() {
        try {
            val httpCookies = manager.cookieStore.cookies
            val json = gson().toJson(httpCookies)
            //println("Save Cookie", fileCookie, json, listCookies, listCookies.size)
            com.dimaslanjaka.library.helper.CookieHandling.println("(Android Save Cookie=${fileCookie}) (Total=${httpCookies.size})")
            com.dimaslanjaka.library.helper.File.write(fileCookie.absolutePath, json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun loadCookie() {
        handler.loadCookie()
    }

    override fun changeFileCookie(fileCookie: File) {
        handler.changeFileCookie(fileCookie)
    }

    companion object {
        lateinit var instance: CookieHandling
    }

    private inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, genericType<T>())
    private inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type
    private fun gson(): Gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
}