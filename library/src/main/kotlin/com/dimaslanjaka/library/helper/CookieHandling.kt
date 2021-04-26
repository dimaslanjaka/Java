package com.dimaslanjaka.library.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker
import javafx.scene.web.WebView
import java.lang.reflect.Type
import java.net.CookieHandler
import java.net.HttpCookie
import java.net.URI
import java.util.regex.Pattern
import java.io.File as JavaIoFile
import java.net.CookieManager as JavaNetCookieManager

class CookieHandling : CookieHandlingInterface {
    override lateinit var fileCookie: JavaIoFile
    override lateinit var manager: JavaNetCookieManager

    constructor() {
        manager = JavaNetCookieManager()
        CookieHandler.setDefault(manager)
        this.fileCookie = JavaIoFile(appdir, "build/cookies/default.json")
    }

    constructor(fileCookie: JavaIoFile) {
        this.fileCookie = fileCookie
        manager = JavaNetCookieManager()
        CookieHandler.setDefault(manager)
    }

    constructor(fileCookie: JavaIoFile, manager: JavaNetCookieManager) {
        this.fileCookie = fileCookie
        CookieHandler.setDefault(manager)
        this.manager = manager
    }

    constructor(manager: JavaNetCookieManager) {
        CookieHandler.setDefault(manager)
        this.manager = manager
    }

    constructor(web: WebView) {
        manager = JavaNetCookieManager()
        CookieHandler.setDefault(manager)
        loadCookie()
        web.engine
            .loadWorker.stateProperty()
            .addListener { _: ObservableValue<out Worker.State>?, _: Worker.State?, newValue: Worker.State ->
                if (newValue == Worker.State.SUCCEEDED) {
                    saveCookie()
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
                    JavaIoFile(appdir, "build/cookies/$cookieFileName")
                } else {
                    JavaIoFile(appdir, "build/cookies/$cookieFileName.json")
                }
            } else {
                JavaIoFile(appdir, "build/cookies/$cookieFileName.json")
            }
        } else if (cookieFileName is JavaIoFile) {
            fileCookie = cookieFileName
        }
        //println("Cookie[$cookieFileName]: $fileCookie [$isNotSlash]")
        manager = JavaNetCookieManager()
        manager.cookieStore.removeAll()
        CookieHandler.setDefault(manager)
        loadCookie()
        web.engine
            .loadWorker.stateProperty()
            .addListener { _: ObservableValue<out Worker.State>?, _: Worker.State?, newValue: Worker.State ->
                if (newValue == Worker.State.SUCCEEDED) {
                    saveCookie()
                }
            }
    }

    override fun loadCookie() {
        gson = GsonBuilder().setPrettyPrinting().create()
        // if cookie file not exists, save current cookies
        if (!fileCookie.exists()) {
            println("FIrst creating cookie")
            saveCookie()
        }
        try {
            val json = File.read(fileCookie.absolutePath) as String
            // convert json string to list
            val httpCookies = Gson().fromJson<List<HttpCookie>>(json)
            println("Load Cookie", fileCookie, httpCookies)
            for (cookie in httpCookies) {
                manager.cookieStore.add(URI.create(cookie.domain), cookie)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun saveCookie() {
        try {
            val listCookies = manager.cookieStore.cookies
            val json = gson.toJson(listCookies)
            println("Save Cookie", fileCookie, json, listCookies, listCookies.size)
            File.write(fileCookie.absolutePath, json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun changeFileCookie(fileCookie: JavaIoFile) {
        this@CookieHandling.fileCookie = fileCookie
        // removing previous cookies
        manager.cookieStore.removeAll()
        // reload cookies from new file cookie
        loadCookie()
    }

    companion object {
        private var gson = GsonBuilder().setPrettyPrinting().create()
        val appdir = JavaIoFile("").absolutePath

        @JvmStatic
        fun main(args: Array<String>) {
            //println(java.io.File(appdir, "build/cookies.json").absolutePath)
        }

        fun loadCookiesAsHttpURLConnection(cookieFileName: Any, manager: JavaNetCookieManager) {
            val fileCookie = when (cookieFileName) {
                is String -> {
                    JavaIoFile(appdir, "build/cookies/$cookieFileName.json")
                }
                is JavaIoFile -> {
                    cookieFileName
                }
                else -> {
                    throw Exception("fileCookie must be filename or filepath")
                }
            }
            try {
                val json = File.read(fileCookie.absolutePath) as String
                // convert json string to list
                val httpCookies = Gson().fromJson<List<HttpCookie>>(json)
                //println(httpCookies)
                for (cookie in httpCookies) {
                    manager.cookieStore.add(URI.create(cookie.domain), cookie)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun println(vararg obj: Any?) {
            obj.map { msg -> kotlin.io.println(msg ?: "NULL") }
        }

        private inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, genericType<T>())
        private inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type
    }
}