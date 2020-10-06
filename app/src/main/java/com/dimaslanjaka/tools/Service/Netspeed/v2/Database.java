package com.dimaslanjaka.tools.Service.Netspeed.v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.dimaslanjaka.tools.Global;
import com.dimaslanjaka.tools.Helpers.core.DateTime;
import com.dimaslanjaka.tools.Helpers.database.DataBaseHelper;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;
import com.dimaslanjaka.tools.Libs.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Database {
  public static String TAG = com.dimaslanjaka.tools.Facebook.Database.class.getName();
  public static SQLiteDatabase db;
  public static Date c = Calendar.getInstance().getTime();
  public static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
  public static Date today = new Date();
  public static Date todayWithZeroTime;
  static final boolean debug = false;

  static {
    try {
      todayWithZeroTime = formatter.parse(formatter.format(today));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public Database(Context context) {
    DataBaseHelper dbhelper = new DataBaseHelper(context, "netspeed.db");
    dbhelper.createDatabase();
    db = dbhelper.getWritableDatabase();
    String tablename = "traffic";
    if (!dbhelper.isTableExists(db, tablename, false)) {
      db.execSQL("CREATE TABLE \"traffic\" ( \"date\" TEXT NOT NULL, \"total\" REAL NOT " +
              "NULL DEFAULT 0, \"download\" REAL NOT NULL DEFAULT 0, \"upload\" REAL NOT NULL DEFAULT 0 );");
    }
    if (!dbhelper.isFieldExist(tablename, "date")) {
      try {
        db.execSQL("ALTER TABLE " + tablename + " ADD COLUMN \"date\" TEXT NOT NULL");
      } catch (SQLiteException ex) {
        Log.out("Altering " + tablename + ": " + ex.getMessage());
      }
    }
    if (!dbhelper.isFieldExist(tablename, "total")) {
      try {
        db.execSQL("ALTER TABLE " + tablename + " ADD COLUMN \"total\" REAL NOT NULL DEFAULT 0");
      } catch (SQLiteException ex) {
        Log.out("Altering " + tablename + ": " + ex.getMessage());
      }
    }
    if (!dbhelper.isFieldExist(tablename, "download")) {
      try {
        db.execSQL("ALTER TABLE " + tablename + " ADD COLUMN \"download\" REAL NOT NULL " +
                "DEFAULT 0");
      } catch (SQLiteException ex) {
        Log.out("Altering " + tablename + ": " + ex.getMessage());
      }
    }
    if (!dbhelper.isFieldExist(tablename, "upload")) {
      try {
        db.execSQL("ALTER TABLE " + tablename + " ADD COLUMN \"upload\" REAL NOT NULL DEFAULT 0");
      } catch (SQLiteException ex) {
        Log.out("Altering " + tablename + ": " + ex.getMessage());
      }
    }
    if (!dbhelper.isFieldExist(tablename, "mobile")) {
      try {
        db.execSQL("ALTER TABLE " + tablename + " ADD COLUMN \"mobile\" REAL NOT NULL DEFAULT 0");
      } catch (SQLiteException ex) {
        Log.out("Altering " + tablename + ": " + ex.getMessage());
      }
    }
    if (!dbhelper.isFieldExist(tablename, "wifi")) {
      try {
        db.execSQL("ALTER TABLE " + tablename + " ADD COLUMN \"wifi\" REAL NOT NULL DEFAULT 0");
      } catch (SQLiteException ex) {
        Log.out("Altering " + tablename + ": " + ex.getMessage());
      }
    }
    String sql = "INSERT INTO traffic (date) SELECT \"" + parseDateToday(todayWithZeroTime) + "\" WHERE NOT EXISTS(SELECT 1 " +
            "FROM " +
            "traffic WHERE date = \"" + parseDateToday(todayWithZeroTime) + "\");";
    db.execSQL(sql);
    db.close();
    //Log.e(TAG, "is table postInfo exist: " + dbhelper.isTableExists(db, "postInfo", false));
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  public static Map<String, String> getToday() throws Throwable {
    return getData(todayWithZeroTime);
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  public static Map<String, String> getToday(Context context) throws Throwable {
    DataBaseHelper dbhelper = new DataBaseHelper(context, "netspeed.db");
    dbhelper.createDatabase();
    db = dbhelper.getWritableDatabase();
    return getToday();
  }

  public static String parseDateToday(Date todayWithZeroTime) {
    return DateTime.getDate(todayWithZeroTime, "MMMM d, yyyy");
  }

  public static String parseDateToday() {
    return DateTime.getDate(todayWithZeroTime, "MMMM d, yyyy");
  }

  /**
   * Get
   *
   * @return
   */
  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  public static List<Map<String, String>> getDatas() {
    DataBaseHelper dbhelper = new DataBaseHelper(Global.getContext(), "netspeed.db");
    db = dbhelper.getWritableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM traffic", null
            , null);
    List<Map<String, String>> results = new ArrayList<Map<String, String>>();
    if (cursor.moveToFirst()) {
      while (!cursor.isAfterLast()) {
        Map<String, String> result = new HashMap<>();
        String upload = cursor.getString(cursor.getColumnIndex("upload"));
        result.put("upload", upload);
        String download = cursor.getString(cursor.getColumnIndex("download"));
        result.put("download", download);
        String wifi = cursor.getString(cursor.getColumnIndex("wifi"));
        result.put("wifi", wifi);
        String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
        result.put("mobile", mobile);
        String date = DateTime.getDate(cursor.getString(cursor.getColumnIndex("date")), "MMMM d, yyyy");
        result.put("date", date);
        result.put("total", cursor.getString(cursor.getColumnIndex("total")));
        result.put("type", getNetworkType());
        results.add(result);
        cursor.moveToNext();
      }
    }
    cursor.close();
    return results;
  }

  public static void save(String type, int totalNetworkType, int totalUpload,
                          int totalDownload) {
    DataBaseHelper dbhelper = new DataBaseHelper(Global.getContext(), "netspeed.db");
    dbhelper.createDatabase();
    db = dbhelper.getWritableDatabase();
    SharedPreferences pref = Objects.requireNonNull(SharedPref.getPref());
    String keyinit = parseDateToday().trim() + "-init";
    if (!pref.getBoolean(keyinit, false)) {
      new Database(Global.getContext());
      pref.edit().putBoolean(keyinit, true).apply();
    }
    db.execSQL("UPDATE traffic SET download = '" + totalDownload + "' WHERE date='" + parseDateToday() + "'");
    db.execSQL("UPDATE traffic SET upload = '" + totalUpload + "' WHERE date='" + parseDateToday() + "'");
    db.execSQL("UPDATE traffic SET " + type.toLowerCase() + " = '" + totalNetworkType + "' " + "WHERE date='" + parseDateToday() + "'");
    db.close();
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  public static Map<String, String> getData(Context context, Date fromDate) throws Throwable {
    DataBaseHelper dbhelper = new DataBaseHelper(context, "netspeed.db");
    dbhelper.createDatabase();
    db = dbhelper.getWritableDatabase();
    return getData(fromDate);
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  public static Map<String, String> getData(Date fromDate) throws Throwable {
    DataBaseHelper dbhelper = new DataBaseHelper(Global.getContext(), "netspeed.db");
    if (!db.isOpen()) db = dbhelper.getWritableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM traffic WHERE date = \"" + parseDateToday(fromDate) + "\"", null
            , null);
    Map<String, String> result = new HashMap<>();
    if (cursor.moveToFirst()) {
      while (!cursor.isAfterLast()) {
        double upload = cursor.getDouble(cursor.getColumnIndex("upload"));
        result.put("upload", Double.toString(upload));
        double download = cursor.getDouble(cursor.getColumnIndex("download"));
        result.put("download", Double.toString(download));
        double wifi = cursor.getDouble(cursor.getColumnIndex("wifi"));
        result.put("wifi", Double.toString(wifi));
        double mobile = cursor.getDouble(cursor.getColumnIndex("mobile"));
        result.put("mobile", Double.toString(mobile));
        String date = DateTime.getDate(cursor.getString(cursor.getColumnIndex("date")), "MMMM d, yyyy");
        result.put("date", date);
        result.put("total", Double.toString(cursor.getDouble(cursor.getColumnIndex("total"))));
        result.put("type", getNetworkType());
        cursor.moveToNext();
      }
    }
    cursor.close();
    //db.close();
    return result;
  }

  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  public static String formatSize(int size) {
    double m = size / 1024.0;
    double g = size / 1048576.0;
    double t = size / 1073741824.0;
    String hrSize = "";
    DecimalFormat dec = new DecimalFormat("0.00");
    if (t > 1) {
      hrSize = dec.format(t).concat("TB");
    } else if (g > 1) {
      hrSize = dec.format(g).concat("GB");
    } else if (m > 1) {
      hrSize = dec.format(m).concat("MB");
    } else {
      hrSize = dec.format(size).concat("KB");
    }
    return hrSize;
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  public static void sync() {
    DataBaseHelper
            dbhelper = new DataBaseHelper(Global.getContext(), "netspeed.db");
    dbhelper.createDatabase();
    db = dbhelper.getWritableDatabase();
    String today = parseDateToday();
    SharedPreferences pref = Objects.requireNonNull(SharedPref.getPref());
    String type = getNetworkType();
    String typeKey = today + "-" + type;
    String uploadKey = today + "-upload";
    String downloadKey = today + "-download";
    long totalDownload = pref.getLong(downloadKey, 0);
    long totalUpload = pref.getLong(uploadKey, 0);
    long totalNetworkType = pref.getLong(typeKey, 0);
    String keyinit = today + "-init";
    if (!pref.getBoolean(keyinit, false)) {
      new Database(Global.getContext());
      pref.edit().putBoolean(keyinit, true).apply();
    }
    String sqlDown = "UPDATE traffic SET download = '" + totalDownload + "' WHERE date='" + parseDateToday() + "'";
    if (debug) Log.out(sqlDown);
    db.execSQL(sqlDown);
    String sqlUp =
            "UPDATE traffic SET upload = '" + totalUpload + "' WHERE date='" + parseDateToday() + "'";
    if (debug) Log.out(sqlUp);
    db.execSQL(sqlUp);
    if (type != null) {
      String sqlType =
              "UPDATE traffic SET " + type.toLowerCase() + " = '" + totalNetworkType + "' " + "WHERE date='" + parseDateToday() + "'";
      if (debug) Log.out(sqlType);
      db.execSQL(sqlType);
    }
    db.close();
  }

  public static boolean isDecimal(double d) {
    return (d - (int) d) != 0;
  }

  public static boolean isNumberOrDecimal(Object d) {
    return String.valueOf(d).matches("^[1-9]\\d*(\\.\\d+)?$");
  }

  public static String getNetworkType() {
    String networkType = null;
    ConnectivityManager connectivityManager =
            (ConnectivityManager) Global.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    if (activeNetwork != null) { // connected to the internet
      if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
        networkType = "wifi";
      } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
        networkType = "mobile";
      }
    } else {
      return null;
    }
    return networkType;
  }

  public void close() {
    if (db != null) {
      db.close();
    }
  }
}
