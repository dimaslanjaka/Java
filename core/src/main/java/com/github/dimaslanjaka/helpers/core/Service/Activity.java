package com.dimaslanjaka.tools.Helpers.core.Service;

import android.content.Context;
import android.content.Intent;

public class Activity {
  /**
   * start/switch activity
   *
   * @param packageContext from activity
   * @param cls            to activity
   */
  public static void start(Context packageContext, Class<?> cls) {
    Intent myIntent = new Intent(packageContext, cls);
    myIntent.putExtra("user", "value"); //Optional parameters
    //packageContext.this.startActivity(myIntent);
    packageContext.startActivity(myIntent);
  }
}
