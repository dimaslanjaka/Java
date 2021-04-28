package android

import android.App.Companion.APPLICATION_ID
import android.text.TextUtils
import android.util.Log
import com.dimaslanjaka.library.helper.Var.gson

/**
 * Improved android.util.Log.
 *
 *
 * Logs class and method automatically:
 * [MyApplication onCreate():34] Hello, world!
 * [MyClass myMethod():42] my log msg
 *
 *
 * Install:
 * $ find -name "*.java" -type f -exec sed -i 's/import android.util.Log/import me.shkschneider.skeleton.helper.Log/g' {} \;
 *
 *
 * Better to use with adb -s com.example.myapp
 */
@Suppress("ProtectedInFinal", "MemberVisibilityCanBePrivate", "LocalVariableName")
class Log protected constructor() {
    companion object {
        private const val VERBOSE = Log.VERBOSE
        private const val DEBUG = Log.DEBUG
        private const val INFO = Log.INFO
        private const val WARN = Log.WARN
        private const val ERROR = Log.ERROR
        private const val WTF = Log.ASSERT

        // Used to identify the source of a log message.
        // It usually identifies the class or activity where the log call occurs.
        protected var TAG = "LogHelper"

        // Here I prefer to use the application's packageName
        protected fun log(level: Int, msg: String?, throwable: Throwable?) {
            val elements = Throwable().stackTrace
            var callerClassName = "?"
            var callerMethodName = "?"
            var callerLineNumber = "?"
            //println(gson.toJson(elements))
            if (elements.size >= 4) {
                callerClassName = elements[3].className
                callerClassName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1)
                if (callerClassName.indexOf("$") > 0) {
                    callerClassName = callerClassName.substring(0, callerClassName.indexOf("$"))
                }
                callerMethodName = elements[3].methodName
                callerMethodName = callerMethodName.substring(callerMethodName.lastIndexOf('_') + 1)
                if (callerMethodName == "<init>") {
                    callerMethodName = callerClassName
                }
                callerLineNumber = elements[3].lineNumber.toString()
            }
            var msgstack = ""
            msgstack = if (TextUtils.isEmpty(msg)) "" else " "
            val stack = "[$callerClassName.$callerMethodName():$callerLineNumber]$msgstack"
            val result_stack = "${stack}\n${msg}" //stack + msg
            when (level) {
                VERBOSE -> Log.v(TAG, result_stack, throwable)
                DEBUG -> Log.d(TAG, result_stack, throwable)
                INFO -> Log.i(TAG, result_stack, throwable)
                WARN -> Log.w(TAG, result_stack, throwable)
                ERROR -> Log.e(TAG, result_stack, throwable)
                WTF -> Log.wtf(TAG, result_stack, throwable)
                else -> {
                    Log.e("E-$TAG", result_stack, throwable)
                }
            }
        }

        fun d(msg: String) {
            log(DEBUG, msg, null)
        }

        fun d(msg: String?, throwable: Throwable) {
            log(DEBUG, msg, throwable)
        }

        fun d(msg: Any) {
            d(msg.toString())
        }

        @JvmStatic
        fun d(vararg msg: Any?) {
            d(varargparser(msg))
        }

        /**
         * Parse vararg by variable types
         * - Object will transformed into json using GSON
         */
        private fun varargparser(vararg msg: Any): String {
            val result_msg = mutableListOf<String>()
            msg.forEachIndexed { index, any ->
                if (any is String) {
                    result_msg.add(any.toString())
                } else if (any !is String) {
                    if (any is Boolean || any is Int || any is Double || any is Float) {
                        result_msg.add(any.toString())
                    } else {
                        result_msg.add(gson.toJson(any))
                    }
                }
            }
            return result_msg.joinToString("\n--------\n")
        }

        fun v(msg: String) {
            log(VERBOSE, msg, null)
        }

        fun v(msg: String?, throwable: Throwable) {
            log(VERBOSE, msg, throwable)
        }

        fun i(msg: String) {
            log(INFO, msg, null)
        }

        fun i(msg: String?, throwable: Throwable) {
            log(INFO, msg, throwable)
        }

        fun w(msg: String) {
            log(WARN, msg, null)
        }

        fun w(msg: String?, throwable: Throwable) {
            log(WARN, msg, throwable)
        }

        fun e(msg: String) {
            log(ERROR, msg, null)
        }

        fun e(msg: String?, throwable: Throwable) {
            log(ERROR, msg, throwable)
        }

        fun wtf(msg: String) {
            log(WTF, msg, null)
        }

        fun wtf(msg: String?, throwable: Throwable) {
            log(WTF, msg, throwable)
        }

        @Deprecated("")
        fun wtf(throwable: Throwable) {
            log(WTF, null, throwable)
        }

        fun e(message: Any) {
            e(message.toString())
        }

        @JvmStatic
        fun e(vararg msg: Any?) {
            e(varargparser(msg))
        }

        fun w(vararg msg: Any?) {
            w(msg.contentToString())
        }

        fun i(msg: Any) {
            i(msg.toString())
        }

        @JvmStatic
        fun i(vararg msg: Any?) {
            i(varargparser(msg))
        }

        fun v(vararg msg: Any?) {
            v(msg.contentToString())
        }

        /**
         * Filter current file script from output print
         */
        private fun filter(e: StackTraceElement): Boolean {
            val fullname = e.className + "." + e.methodName
            return (fullname.trim { it <= ' ' }.endsWith("Log.out")
                    || fullname.trim { it <= ' ' }.endsWith("Log.v")
                    || fullname.trim { it <= ' ' }.endsWith("Log.i")
                    || fullname.trim { it <= ' ' }.endsWith("Log.e")
                    || fullname.trim { it <= ' ' }.endsWith("Log.d")
                    || fullname.trim { it <= ' ' }.endsWith("fixTag")
                    || fullname.trim { it <= ' ' }.contains("UtilsKt.log")
                    || fullname.trim { it <= ' ' }.contains("service.Log"))
        }

        fun out(msg: String?) {
            fixTag()
            i(msg)
        }

        private fun fixTag() {
            val stacktrace = Thread.currentThread().stackTrace
            var e = stacktrace[2] //maybe this number needs to be corrected
            if (filter(e)) {
                e = stacktrace[3]
                if (filter(e)) {
                    e = stacktrace[4]
                    if (filter(e)) {
                        e = stacktrace[5]
                        if (filter(e)) {
                            e = stacktrace[6]
                        }
                    }
                }
            }
            TAG = e.className + "." + e.methodName
            if (TAG == "<init>") {
                TAG = e.className
            }
            TAG += "(" + e.lineNumber + ")"
            TAG = TAG.replace(APPLICATION_ID, "")
            if (APPLICATION_ID.isEmpty()) {
                e(
                    "APPLICATION_ID must be not empty!!!\n" +
                            " set it by android.App.setAPPLICATION_ID(\"your.application.id\")"
                )
            }
        }

        @JvmStatic
        fun w(o: Any) {
            w(o.toString())
        }

        fun out(o: Any) {
            i(o.toString())
        }

        fun out(vararg o: Any?) {
            i(o.contentToString())
        }

        init {
            fixTag()
        }
    }

    init {
        fixTag()
    }
}