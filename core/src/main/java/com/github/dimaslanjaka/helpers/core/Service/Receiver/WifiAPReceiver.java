package com.dimaslanjaka.tools.Helpers.core.Service.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class WifiAPReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction() == "android.net.wifi.WIFI_AP_STATE_CHANGED") {
      int apState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
      if (apState == Constant.AP_STATE_ENABLED) {
        Constant.AP_STATE = "enabled";
      } else if (apState == Constant.AP_STATE_DISABLED) {
        Constant.AP_STATE = "disabled";
      } else if (apState == Constant.AP_STATE_DISABLING) {
        Constant.AP_STATE = "disabling";
      }
    }
  }
}