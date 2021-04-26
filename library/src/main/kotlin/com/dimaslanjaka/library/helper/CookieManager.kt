package com.dimaslanjaka.library.helper

import java.net.CookieHandler
import java.net.CookiePolicy
import java.net.HttpCookie
import java.net.CookieManager as JavaNetCookieManager

/**
 * java.net.CookieManager
 */
private class CookieManager() {
    private var mCookieManager: JavaNetCookieManager? = null
    private val cookies: List<HttpCookie>?
        get() = mCookieManager?.cookieStore?.cookies

    fun clearCookies() {
        mCookieManager?.cookieStore?.removeAll()
    }

    val isCookieManagerEmpty: Boolean
        get() = mCookieManager?.cookieStore?.cookies?.isEmpty() ?: true
    val cookieValue: String
        get() {
            var cookieValue = String()
            if (!isCookieManagerEmpty) {
                for (eachCookie in cookies!!) {
                    cookieValue += String.format("%s=%s; ", eachCookie.name, eachCookie.value)
                }
            }
            return cookieValue
        }

    init {
        mCookieManager = JavaNetCookieManager()
        mCookieManager?.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(mCookieManager)
    }
}