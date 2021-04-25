package com.dimaslanjaka.library.helper

import com.google.gson.GsonBuilder
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker
import javafx.scene.web.WebView
import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpCookie
import java.net.URI
import java.util.regex.Pattern

class CookieHandling {
    constructor() {
        manager = CookieManager()
        CookieHandler.setDefault(manager)
    }

    constructor(fileCookie: java.io.File) {
        CookieHandling.fileCookie = fileCookie
        manager = CookieManager()
        CookieHandler.setDefault(manager)
    }

    constructor(fileCookie: java.io.File, manager: CookieManager) {
        CookieHandling.fileCookie = fileCookie
        CookieHandler.setDefault(manager)
        CookieHandling.manager = manager
    }

    constructor(manager: CookieManager) {
        CookieHandler.setDefault(manager)
        CookieHandling.manager = manager
    }

    constructor(web: WebView) {
        manager = CookieManager()
        CookieHandler.setDefault(manager)
        loadCookie()
        web.engine
            .loadWorker.stateProperty()
            .addListener { _: ObservableValue<out Worker.State>?, _: Worker.State?, newValue: Worker.State ->
                if (newValue == Worker.State.SUCCEEDED) {
                    saveCookies()
                }
            }
    }

    @Suppress("NewApi")
    constructor(web: WebView, cookieFileName: Any) {
        val isNotSlash = !Pattern.compile("[/\\\\]").matcher(cookieFileName as CharSequence).find()
        if (cookieFileName is String && isNotSlash) {
            val extension = File.getFileExtension(cookieFileName)
            fileCookie = if (extension != null && extension.isPresent) {
                if (extension.get() == "json") {
                    java.io.File(appdir, "build/cookies/$cookieFileName")
                } else {
                    java.io.File(appdir, "build/cookies/$cookieFileName.json")
                }
            } else {
                java.io.File(appdir, "build/cookies/$cookieFileName.json")
            }
        } else if (cookieFileName is java.io.File) {
            fileCookie = cookieFileName
        }
        //println("Cookie[$cookieFileName]: $fileCookie [$isNotSlash]")
        manager = CookieManager()
        manager.cookieStore.removeAll()
        CookieHandler.setDefault(manager)
        loadCookie()
        web.engine
            .loadWorker.stateProperty()
            .addListener { _: ObservableValue<out Worker.State>?, _: Worker.State?, newValue: Worker.State ->
                if (newValue == Worker.State.SUCCEEDED) {
                    saveCookies()
                }
            }
    }

    fun loadCookie() {
        loadCookies()
    }

    fun saveCookie() {
        saveCookies()
    }

    fun changeCookieFile(fileCookie: java.io.File) {
        changeCookieFile(fileCookie)
    }

    companion object {
        var gson = GsonBuilder().setPrettyPrinting().create()
        val appdir = java.io.File("").absolutePath

        lateinit var manager: CookieManager
        var fileCookie =
            java.io.File(appdir, "build/cookies/default.json") //temp.getTempFile("default.json")

        @JvmStatic
        fun main(args: Array<String>) {
            //println(java.io.File(appdir, "build/cookies.json").absolutePath)
        }

        fun loadCookiesAsHttpURLConnection(cookieFileName: Any, manager: CookieManager) {
            val fileCookie = when (cookieFileName) {
                is String -> {
                    java.io.File(appdir, "build/cookies/$cookieFileName.json")
                }
                is java.io.File -> {
                    cookieFileName
                }
                else -> {
                    throw Exception("fileCookie must be filename or filepath")
                }
            }
            try {
                val json = File.read(fileCookie.absolutePath) as String
                //val gson = GsonBuilder().create()
                val httpCookies: List<HttpCookie>
                val type = Var.typeListHttpCookie
                httpCookies =
                    Var.fromJson(json, type) as List<HttpCookie> // convert json string to list
                //println(httpCookies)
                for (cookie in httpCookies) {
                    manager.cookieStore.add(URI.create(cookie.domain), cookie)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun loadCookies() {
            gson = GsonBuilder().setPrettyPrinting().create()
            // if cookie file not exists, save current cookies
            if (!fileCookie.exists()) {
                saveCookies()
            }
            try {
                val json = File.read(fileCookie.absolutePath) as String
                //val gson = GsonBuilder().create()
                val httpCookies: List<HttpCookie>
                val type = Var.typeListHttpCookie
                httpCookies =
                    Var.fromJson(json, type) as List<HttpCookie> // convert json string to list
                //println(httpCookies)
                for (cookie in httpCookies) {
                    manager.cookieStore.add(URI.create(cookie.domain), cookie)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun saveCookies() {
            try {
                val listCookies = manager.cookieStore.cookies
                val json = gson.toJson(listCookies)
                File.write(fileCookie.absolutePath, json)
                //println(fileCookie)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun changeFileCookie(newCookieLocation: java.io.File) {
            fileCookie = newCookieLocation
            manager = CookieManager()
            manager.cookieStore.removeAll()
            CookieHandler.setDefault(manager)
            loadCookies()
        }
    }
}