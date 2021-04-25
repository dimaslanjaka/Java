package com.dimaslanjaka.preferences

import com.google.gson.GsonBuilder
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences

class Preferences(configName: String) {
    var gson = GsonBuilder().disableHtmlEscaping().create()
    var preferences: Preferences

    companion object {
        @Throws(BackingStoreException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val preferences = Preferences("dimas2").getPreferences()
            //Stream.of(preferences.childrenNames()).forEach { x: Array<out String>? -> println("X: $x") }

            //preferences.put("name", "L3n4r0x");
            preferences.sync()
            println(preferences["name", false.toString()])
        }
    }

    init {
        preferences = Preferences.userRoot().node("build/preferences/$configName")
    }

    @JvmName("getPreferences1")
    fun getPreferences(): Preferences {
        return preferences;
    }

    fun sync() {
        preferences.sync()
    }
}