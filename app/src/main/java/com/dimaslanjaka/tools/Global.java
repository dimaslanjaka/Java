package com.dimaslanjaka.tools;

import android.app.Application;
import android.content.Context;

import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;

public class Global extends Application {
  private static Application sApplication;

  public static Application getApplication() {
    return sApplication;
  }

  public static Context getContext() {
    return getApplication().getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    sApplication = this;
    //reset();
  }

  private void reset() {
    new SharedPref(getContext());
    SharedPref.reset();
    deleteDatabase("netspeed.db");
  }
}
