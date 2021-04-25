package com.dimaslanjaka.library.helper

import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.*

object Curl {
    @JvmStatic
    var msCookieManager = CookieManager(null, CookiePolicy.ACCEPT_ALL)

    @JvmStatic
    fun main(args: Array<String>) {
        // set cookie handler default
        CookieHandler.setDefault(msCookieManager)
        // load cookie to cookie manager
        CookieHandling.loadCookiesAsHttpURLConnection("default", msCookieManager)
        // start connection
        val url = "https://mbasic.facebook.com"
        val html = getHtml(url)
        val doc = Jsoup.parse(html)
        println(doc.title())

        //println(msCookieManager.cookieStore.cookies.joinToString(";"))
    }

    @JvmStatic
    fun getCookieManager(cookieName: Any): CookieManager {
        if (cookieName is String || cookieName is File) {
            // load cookie to cookie manager
            CookieHandling.loadCookiesAsHttpURLConnection(cookieName, msCookieManager)
        }
        // set cookie handler default
        CookieHandler.setDefault(msCookieManager)
        return msCookieManager
    }

    @JvmStatic
    fun getHtml(url: String, manager: CookieManager, cookieName: String) {
        // set cookie handler default
        CookieHandler.setDefault(msCookieManager)
        // load cookie to cookie manager
        CookieHandling.loadCookiesAsHttpURLConnection(cookieName, msCookieManager)
    }

    @JvmStatic
    fun getHtml(url: String): String {
        val content = StringBuilder()
        val connection: HttpURLConnection
        try {
            connection = URL(url).openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = true
            HttpURLConnection.setFollowRedirects(true)
            // open the stream and put it into BufferedReader
            val br = BufferedReader(InputStreamReader(connection.inputStream))
            var lines: String?
            while (br.readLine().also { lines = it } != null) {
                content.append(lines).append("\n")
                //println(lines)
            }
            br.close()
            connection.disconnect()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return content.toString()
    }
}