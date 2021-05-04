package android

import android.content.Intent

@Suppress("unused")
object Activity {
    @JvmStatic
    fun start(clazz: Class<*>, extras: Map<String, String>) {
        val intent = Intent(App.globalContext(), clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        for ((key, value) in extras) {
            println("EXTRAS $key = $value")
            intent.putExtra(key, value)
        }
        App.globalContext().startActivity(intent)
    }

    @JvmStatic
    fun start(clazz: Class<*>) {
        start(clazz, mapOf())
    }
}