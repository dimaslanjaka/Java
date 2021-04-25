package com.dimaslanjaka.library.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Json {
    lateinit var gson: Gson

    constructor(jsonString: String) {
        gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
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
}