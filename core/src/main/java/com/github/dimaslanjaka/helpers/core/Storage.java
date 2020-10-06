package com.dimaslanjaka.tools.Helpers.core;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Objects;

public class Storage {
  /**
   * @param name folder name
   * @todo create folder if not exists
   */
  public static boolean folder(String name) {
    File folder = new File(Environment.getExternalStorageState() +
            File.separator + name);
    boolean success = false;
    if (!folder.exists()) {
      success = folder.mkdirs();
    }
    return success;
  }

  public static String path(String... str) {
    StringBuilder strb = new StringBuilder();
    for (String arg : str) {
      strb.append(File.separator).append(arg);
    }
    return strb.toString();
  }

  public static void deleteCache(Context context) {
    try {
      File dir = context.getCacheDir();
      deleteDir(dir);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public static String initializeCache(Context context) {
    long size = 0;
    size += getDirSize(context.getCacheDir());
    size += getDirSize(Objects.requireNonNull(context.getExternalCacheDir()));
    android.util.Log.e("size cache", readableFileSize(size));
    //((TextView) findViewById(R.id.yourTextView)).setText(readableFileSize(size));
    return readableFileSize(size);
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public static long getDirSize(File dir) {
    long size = 0;
    for (File file : Objects.requireNonNull(dir.listFiles())) {
      if (file != null && file.isDirectory()) {
        size += getDirSize(file);
      } else if (file != null && file.isFile()) {
        size += file.length();
      }
    }
    return size;
  }

  public static String readableFileSize(long size) {
    if (size <= 0) return "0 Bytes";
    final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
    return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
  }

  public static boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
      return dir.delete();
    } else if (dir != null && dir.isFile()) {
      return dir.delete();
    } else {
      return false;
    }
  }

  public static void openFolder(Context c, String FolderLocation) {
    Intent openFolder = new Intent(Intent.ACTION_VIEW);
    ActivityInfo resolveFolderIntent = openFolder.resolveActivityInfo(c.getPackageManager(), 0);
    Log.i("Intent folder: ", String.valueOf(resolveFolderIntent));
    openFolder.setDataAndType(Uri.parse(FolderLocation), "resource/folder");
    if (resolveFolderIntent != null) {
      try {
        c.startActivity(openFolder);
      } catch (ActivityNotFoundException e) {
        android.util.Log.e("open folder: ", "failed");
      }
    }
  }

  public static boolean isFileExists(String filepath) {
    File folder1 = new File(filepath);
    boolean exist = folder1.exists();
    Log.i("isFileExists", folder1 + " is exists: " + exist);
    return exist;
  }

  public static boolean deleteFile(String filepath) {
    File folder1 = new File(filepath);
    boolean exist = isFileExists(filepath);
    Log.i("deleteFile", "deleting " + filepath);
    if (exist) {
      return folder1.delete();
    }
    return false;
  }
}
