package com.dimaslanjaka.root

import android.content.Context

class Toast {
    val long = android.widget.Toast.LENGTH_LONG
    val short = android.widget.Toast.LENGTH_SHORT

    /**
     * Creates a Toast.
     *
     * @param context The context.
     * @param isLong Whether the Toast should have a Toast.LENGTH_LONG or Toast.LENGTH_SHORT.
     * @param text The text to display in the Toast.
     */
    fun make(context: Context?, isLong: Boolean, text: String?) {
        val duration: Int = if (isLong) long else short
        android.widget.Toast.makeText(context, text, duration).show()
    }

    /**
     * Creates a Toast with duration = Toast.LENGTH_LONG.
     *
     * @param context The context.
     * @param text The text to display in the Toast.
     */
    fun make(context: Context?, text: String?) {
        make(context, true, text)
    }
}