package com.dimaslanjaka.tools.Service.Netspeed;

import com.dimaslanjaka.tools.Libs.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Converter {
  public static String parseSize(long bytes) {
    long n = 1024;
    String result;
    long kb = bytes / n;
    long mb = kb / n;
    long gb = mb / n;
    long tb = gb / n;
    //Log.out(bytes, kb, mb, gb, tb);
    if (bytes < n) {
      result = Long.valueOf(bytes).doubleValue() + " Bytes";
    } else if (bytes <= n * n) {
      result = Long.valueOf(kb).doubleValue() + " KB";
    } else if (bytes <= n * n * n) {
      result = Long.valueOf(mb).doubleValue() + " MB";
    } else if (bytes <= n * n * n * n) {
      result = Long.valueOf(gb).doubleValue() + " GB";
    } else {
      result = tb + " TB";
    }
    return result;
  }

  public static String getSize(long bytes) {
    long n = 1000;
    String s;
    double kb = bytes / n;
    double mb = kb / n;
    double gb = mb / n;
    double tb = gb / n;
    if (bytes < n) {
      s = bytes + " Bytes";
    } else if (bytes < n * n) {
      s = String.format("%.2f", kb) + " KB";
    } else if (bytes < n * n * n) {
      s = String.format("%.2f", mb) + " MB";
    } else if (bytes < n * n * n * n) {
      s = String.format("%.2f", gb) + " GB";
    } else {
      s = String.format("%.2f", tb) + " TB";
    }

    BigDecimal dec = new BigDecimal(bytes);
    BigDecimal div = dec.setScale(2, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(1000));
    Log.out(div);

    //Log.out(bytes, kb, mb, gb, tb);
    return s;
  }
}
