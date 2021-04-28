package android

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowManager
import android.view.autofill.AutofillManager
import androidx.annotation.RequiresApi

object Fun {
    /**
     * <a href="https://stackoverflow.com/a/46901226">https://stackoverflow.com/a/46901226</a>
     */
    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.O)
    fun disableAutoFill(context: Context) {
        val autofillManager: AutofillManager = context.getSystemService(AutofillManager::class.java)
        autofillManager.disableAutofillServices()
    }

    /**
     * Remove title bar
     */
    @JvmStatic
    fun removeTitleBar(compat: Activity): Boolean {
        return compat.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * Remove notification bar
     */
    @JvmStatic
    fun removeNotificationBar(compat: Activity) {
        @Suppress("UsePropertyAccessSyntax")
        compat.getWindow()
            .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}