package com.dimaslanjaka.root

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.InputStreamReader
import java.util.*


object Utils {
    fun isRootAvailable(): Boolean {
        val path = System.getenv("PATH")!!
        for (pathDir in path.split(":").toTypedArray()) {
            if (File(pathDir, "su").exists()) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isProbablyAnEmulator() = Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.BOARD == "QC_Reference_Phone" //bluestacks
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.HOST.startsWith("Build") //MSI App Player
            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            || "google_sdk" == Build.PRODUCT

    @JvmStatic
    fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos = activityManager.runningAppProcesses
        if (procInfos != null) {
            for (processInfo in procInfos) {
                if (processInfo.processName == packageName) {
                    return true
                }
            }
        }
        return false
    }

    fun isRootGiven(): Boolean {
        if (isRootAvailable()) {
            var process: Process? = null
            try {
                process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
                val `in` = BufferedReader(InputStreamReader(process.inputStream))
                val output: String = `in`.readLine()
                if (output.toLowerCase(Locale.ROOT).contains("uid=0")) return true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                process?.destroy()
            }
        }
        return false
    }

    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val `in` = BufferedReader(InputStreamReader(process.inputStream))
            `in`.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    @JvmStatic
    fun prepareKvmKernelModule() {
        try {
            val p = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(p.outputStream)
            outputStream.writeBytes("cd /\n")
            outputStream.flush()
            outputStream.writeBytes("exit\n")
            outputStream.flush()
            p.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun println(vararg arg: Any?) {
        if (arg.size == 1) {
            kotlin.io.println(arg[0])
        } else {
            kotlin.io.println(arg.toList())
        }
    }

    /**
     *  PackageManager pm = context.getPackageManager();
     *  boolean isInstalled = isPackageInstalled("com.somepackage.name", pm);
     */
    @JvmStatic
    fun isPackageInstalled(packageName: String?, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName!!, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}