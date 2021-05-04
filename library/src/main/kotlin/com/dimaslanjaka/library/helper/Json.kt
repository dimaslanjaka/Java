package com.dimaslanjaka.library.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class Json {
    var gson: Gson = gson()
    var serialized = "{}"
    lateinit var deserialized: Any

    constructor(gs: Gson) {
        gson = gs
    }

    constructor(json: String, type: Type) {
        deserialized = gson.fromJson(json, type)
    }

    constructor(json: String, type: Class<*>) {
        deserialized = gson.fromJson(json, type)
    }

    constructor() {}

    companion object {
        @JvmStatic
        fun isJSONValid(test: String?): Boolean {
            try {
                JSONObject(test)
            } catch (ex: JSONException) {
                // edited, to include @Arthur's comment
                // e.g. in case JSONArray is valid as well...
                try {
                    JSONArray(test)
                } catch (ex1: JSONException) {
                    return false
                }
            }
            return true
        }
    }

    inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, genericType<T>())
    inline fun <reified T> genericType(): Type = object : TypeToken<T>() {}.type
    fun gson(): Gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
}