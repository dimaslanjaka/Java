package com.dimaslanjaka.tools.Libs.Net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class Net {
  static String TAG = Net.class.getName();
  static boolean debug = false;

  public static boolean isInternetAvailable(Context context) {
    if (isNetworkConnected(context)) {
      try {
        InetAddress[] addr = InetAddress.getAllByName("www.google.com");
        return addr[0].isReachable(10000);
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }
    return false;
  }

  public static boolean isNetworkConnected(Context context) {
    ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
  }

  public String ping(String url) {
    String str = "";
    try {
      java.lang.Process process = Runtime.getRuntime().exec(
              "ping -c 1 " + url);
      BufferedReader reader = new BufferedReader(new InputStreamReader(
              process.getInputStream()));
      int i;
      char[] buffer = new char[4096];
      StringBuffer output = new StringBuffer();
      String op[] = new String[64];
      String delay[] = new String[8];
      while ((i = reader.read(buffer)) > 0)
        output.append(buffer, 0, i);
      reader.close();
      op = output.toString().split("\n");
      delay = op[1].split("time=");

      // body.append(output.toString()+"\n");
      str = delay[1];
      Log.i("Pinger", "Ping: " + delay[1]);
    } catch (IOException e) {
      // body.append("Error\n");
      e.printStackTrace();
    }
    return str;
  }
}
