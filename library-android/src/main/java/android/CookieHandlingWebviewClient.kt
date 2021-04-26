package android

import android.os.Build
import android.util.Log
import android.webkit.*

class CookieHandlingWebviewClient : WebViewClient {
    private var cookieHandler: CookieHandling? = null

    @Suppress("unused")
    constructor() : super() {
    }

    constructor(cookieHandler: CookieHandling) {
        this.cookieHandler = cookieHandler
        cookieHandler.loadCookie()
    }

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
        if (cookies != null) Log.d("COOKIE", cookies)
        cookieHandler?.saveCookie()
    }
}