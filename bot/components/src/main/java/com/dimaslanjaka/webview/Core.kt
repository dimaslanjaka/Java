package com.dimaslanjaka.webview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.CookieSyncManager
import androidx.annotation.RequiresApi
import com.dimaslanjaka.components.webview.CookieManager
import java.io.File


@SuppressLint("ViewConstructor")
class Core : android.webkit.WebView {
    var cookieManager = CookieManager()
    var webCookieSync = CookieSyncManager.createInstance(this)
    var webCookieManager = CookieManager.getInstance()
    var cookiePath: String = "cookie.json"
        set(path) {
            if (FileUtils.isPermissionAllowed(context)) FileUtils.resolve(File(path))
            field = path
        }

    constructor(context: Context) : super(context) {
        initWebView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initWebView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initWebView(attrs, defStyleAttr)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initWebView(attrs, defStyleAttr)
    }

    private fun initWebView(attrs: AttributeSet?, defStyleAttr: Int) {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        cookieManager.setAcceptCookie(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.acceptThirdPartyCookies(this)
            cookieManager.setAcceptThirdPartyCookies(this, true)
        }

        settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            allowContentAccess = true
            allowFileAccess = true
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, privateBrowsing: Boolean) : super(
        context,
        attrs,
        defStyleAttr,
        privateBrowsing
    ) {

        initWebView(attrs, defStyleAttr)
    }

    fun saveCookie() {

    }
}