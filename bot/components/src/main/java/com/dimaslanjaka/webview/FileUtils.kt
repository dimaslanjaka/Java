package com.dimaslanjaka.webview

import android.content.Context
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileWriter

object FileUtils {
    @JvmStatic
    @SuppressWarnings("unused")
    fun write(file: File, content: String) {
        resolve(file)
        val writer = FileWriter(file)
        writer.write(content)
        writer.close()
    }

    @JvmStatic
    fun resolve(file: File) {
        mkdir(file.parentFile)
        file.createNewFile()
    }

    @JvmStatic
    fun mkdir(dir: File) {
        if (!dir.exists()) dir.mkdirs()
    }

    fun isPermissionAllowed(context: Context): Boolean {
        return EasyPermissions.hasPermissions(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}