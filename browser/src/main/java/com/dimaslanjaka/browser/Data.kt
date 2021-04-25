package com.dimaslanjaka.browser

import android.content.Context

object Data {
    @JvmStatic
    lateinit var home: String

    @JvmStatic
    @Throws(Exception::class)
    fun getAnyDataDir(context: Context, packageName: String): String? {
        return context.packageManager.getPackageInfo(packageName, 0).applicationInfo.dataDir
    }
}