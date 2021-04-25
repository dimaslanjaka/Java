package android

import android.annotation.SuppressLint
import android.os.Build
import java.lang.Double.parseDouble

@SuppressLint("ObsoleteSdkInt")
object Device {
    /**
     * Is device is Gingerbread and later
     */
    val gingerbread = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD

    /**
     * Is device is Ice Cream Sandwich or later
     */
    val ics = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH

    /**
     * Is device is Nougat or later
     */
    val nougat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    fun currentVersion(): String {
        val release = parseDouble(
            java.lang.String(Build.VERSION.RELEASE).replaceAll("(\\d+[.]\\d+)(.*)", "$1")
        )
        var codeName = "UNSUPPORTED"//below Jelly Bean
        if (release >= 4.1 && release < 4.4) codeName = "JELLYBEAN"
        else if (release < 5) codeName = "KITKAT"
        else if (release < 6) codeName = "LOLLIPOP"
        else if (release < 7) codeName = "MARSHMALLOW"
        else if (release < 8) codeName = "NOUGAT"
        else if (release < 9) codeName = "OREO"
        else if (release < 10) codeName = "PIE"
        else if (release >= 10) codeName =
            "Android " + (release.toInt())//since API 29 no more candy code names
        return codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT
    }
}