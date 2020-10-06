package com.dimaslanjaka.tools.Helpers.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Connection {
  /**
   * Check if vpn running in network interface
   *
   * @return boolean
   */
  public static boolean isVPN() {
    List<String> networkList = new ArrayList<>();
    try {
      for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
        if (networkInterface.isUp())
          networkList.add(networkInterface.getName());
      }
    } catch (Exception ex) {
      //Log.d("is Vpn Using Network List didn't received");
    }

    return networkList.contains("tun0");
  }

  /**
   * Check internet connection is available
   *
   * @return
   */
  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivityManager
            = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  /**
   * Check network connection active on spesific adapter
   *
   * @return "wifi"|"mobile"|"both"|null
   */
  private static String NetworkConnection(Context context) {
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;

    ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
    for (NetworkInfo ni : netInfo) {
      if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
        if (ni.isConnected()) {
          haveConnectedWifi = true;
        }
      }
      if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
        if (ni.isConnected()) {
          haveConnectedMobile = true;
        }
      }
    }
    //return haveConnectedWifi || haveConnectedMobile;
    if (haveConnectedWifi && haveConnectedMobile) {
      return "both";
    } else if (haveConnectedWifi) {
      return "wifi";
    } else if (haveConnectedMobile) {
      return "mobile";
    }
    return null;
  }
}
