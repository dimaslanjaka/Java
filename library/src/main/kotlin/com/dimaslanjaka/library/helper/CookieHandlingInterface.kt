package com.dimaslanjaka.library.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.net.CookieManager

interface CookieHandlingInterface {
    var fileCookie: File
    var manager: CookieManager
    fun loadCookie() {}
    fun saveCookie() {}
    fun changeFileCookie(fileCookie: File) {}

    private inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, genericType<T>())
    private inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type
    private fun gson(): Gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
}