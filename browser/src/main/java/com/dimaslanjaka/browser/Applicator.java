package com.dimaslanjaka.browser;

import android.App;
import android.app.Application;

public class Applicator extends Application {
    public void onCreate() {
        super.onCreate();
        App.Companion.setApplication(this);
        App.Companion.setAPPLICATION_ID(BuildConfig.APPLICATION_ID);
    }

    public Application getApplication() {
        return App.Companion.getApplication();
    }
}
