package com.dimaslanjaka.root

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@Suppress("unused")
class Core : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        isContextInitialized = true
    }

    companion object {
        @JvmStatic
        @Suppress("unused", "setter")
        var isAppInBackground : Boolean = false
            set(value) {
                field = value
            }

        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var context: Context

        @JvmStatic
        val debug = BuildConfig.DEBUG

        @JvmStatic
        val androidR = R::javaClass

        @JvmStatic
        val appContext: Context
            get() = context

        @JvmStatic
        var isContextInitialized: Boolean = false
    }
}