package com.dimaslanjaka.tools.Libs;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.dimaslanjaka.tools.BuildConfig;

import org.json.JSONObject;

import java.io.File;
import java.net.HttpCookie;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Log extends Logger {
  static String methodName = null;
  public static String typeLog = "info";

  protected Log(@Nullable @org.jetbrains.annotations.Nullable String name, @Nullable @org.jetbrains.annotations.Nullable String resourceBundleName) {
    super(name, resourceBundleName);
  }

  public static void out(boolean msg) {
    /*
    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
    StackTraceElement e = stacktrace[3]; //maybe this number needs to be corrected
    if (e.getMethodName().endsWith("Log.out")) {
      e = stacktrace[2];
    }

    methodName = e.getMethodName();
    if (methodName.equals("<init>")) {
      methodName = e.getClassName();
    }
    methodName += "(" + e.getLineNumber() + ")";
    methodName = methodName.replace(BuildConfig.APPLICATION_ID, "");
    android.util.Log.i(methodName, String.valueOf(msg));
    android.util.Log.e(methodName, Arrays.toString(stacktrace));
    System.out.println();
     */
    out(Boolean.toString(msg));
  }

  public static void out(int msg) {
    /*
    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
    StackTraceElement e = stacktrace[3];//maybe this number needs to be corrected
    methodName = e.getMethodName();
    if (methodName.equals("<init>")) {
      methodName = e.getClassName();
    }
    methodName += "(" + e.getLineNumber() + ")";
    methodName = methodName.replace(BuildConfig.APPLICATION_ID, "");
    android.util.Log.i(methodName, String.valueOf(msg));
    System.out.println();
     */
    out(String.valueOf(msg));
  }

  private static boolean filter(StackTraceElement e) {
    String fullname = e.getClassName() + "." + e.getMethodName();
    return fullname.trim().endsWith("Log.out");
  }

  public static void out(String msg) {
    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
    StackTraceElement e = stacktrace[2];//maybe this number needs to be corrected
    if (filter(e)) {
      e = stacktrace[3];
      if (filter(e)) {
        e = stacktrace[4];
        if (filter(e)) {
          e = stacktrace[5];
          if (filter(e)) {
            e = stacktrace[6];
          }
        }
      }
    }
    methodName = e.getClassName() + "." + e.getMethodName();
    if (methodName.equals("<init>")) {
      methodName = e.getClassName();
    }
    methodName += "(" + e.getLineNumber() + ")";
    methodName = methodName.replace(BuildConfig.APPLICATION_ID, "");
    if (msg == null) {
      msg = "null";
    }
    switch (typeLog) {
      case "debug":
        android.util.Log.d(methodName, msg);
        break;
      case "verbose":
        android.util.Log.v(methodName, msg);
        break;
      case "write":
        android.util.Log.w(methodName, msg);
        break;
      default:
        android.util.Log.i(methodName, msg);
        break;
    }
    /*
    for (StackTraceElement stackTraceElement : stacktrace) {
      android.util.Log.e("",
              stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
    }
     */
    System.out.println();
  }

  public static void out(JSONObject msg) {
    out(msg.toString());
    /*
    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
    StackTraceElement e = stacktrace[3];//maybe this number needs to be corrected
    methodName = e.getMethodName();
    if (methodName.equals("<init>")) {
      methodName = e.getClassName();
    }
    methodName += "(" + e.getLineNumber() + ")";
    methodName = methodName.replace(BuildConfig.APPLICATION_ID, "");
    android.util.Log.i(methodName, String.valueOf(msg));
    System.out.println();
     */
  }

  public static void out(Uri uri) {
    out(uri.toString());
  }

  public static void out(File file) {
    out(file.getPath());
  }

  public static void out(List<?> listStr) {
    out(listStr.toString());
  }

  public static void out(Map<String, List<String>> listMap) {
    out(listMap.toString());
  }

  public static void out(HttpCookie httpCookie) {
    out(httpCookie.toString());
  }

  public static void out(String[] data) {
    out(Arrays.toString(data));
  }

  public static void out(double v) {
    out(Double.toString(v));
  }

  public static void out(String action, boolean bool) {
    out(action + ", " + bool);
  }

  public static void out(Object... messages) {
    out(Arrays.toString(messages));
  }
}
