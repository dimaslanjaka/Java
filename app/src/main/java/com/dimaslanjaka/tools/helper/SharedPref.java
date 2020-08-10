package com.dimaslanjaka.tools.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

public class SharedPref {
    public static SharedPreferences prefs = null;
    public static String id = null;
    public static Context c = null;

    @SuppressLint("HardwareIds")
    public SharedPref(Context context) {
        c = context;
        id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        prefs = context.getSharedPreferences(id, Context.MODE_PRIVATE);
    }

    public static String getDeviceId() {
        return id;
    }

    public static SharedPreferences getPref() {
        if (prefs != null) {
            return prefs;
        }
        if (id != null && c != null) {
            return c.getSharedPreferences(id, Context.MODE_PRIVATE);
        }
        return null;
    }

    // this method will save the nightMode State : True or False
    public static void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

    // this method will load the Night Mode State
    public static Boolean loadNightModeState() {
        Boolean state = prefs.getBoolean("NightMode", false);
        return state;
    }
}
