package android

import android.app.Application
import android.content.Context

/**
 * <a href="https://stackoverflow.com/a/5114361">https://stackoverflow.com/a/5114361</a>
 */
class App {
    constructor() {
        throw Exception("Need argument of android.app.Application to run this library")
    }

    constructor(app: Application) {
        sApplication = app
    }

    companion object {
        @JvmStatic
        var APPLICATION_ID = ""
        private lateinit var sApplication: Application
        fun getApplication(): Application {
            return sApplication
        }

        fun setApplication(app: Application) {
            sApplication = app
        }

        /**
         * Static android.content.Context
         * <a href="https://stackoverflow.com/a/28021111">https://stackoverflow.com/a/28021111</a>
         */
        @JvmStatic
        fun globalContext(): Context {
            return getApplication().applicationContext
        }
    }
}