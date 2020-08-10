package com.dimaslanjaka.tools.helper;

import android.os.Environment;

import java.io.File;

public class storage {
    /**
     * @todo create folder if not exists
     * @param name folder name
     */
    public static void folder(String name) {
        File folder = new File(Environment.getExternalStorageState() +
                File.separator + name);
        boolean success = false;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
    }

    public static String path(String... str) {
        StringBuilder strb = new StringBuilder();
        for (String arg : str) {
            strb.append(File.separator).append(arg);
        }
        return strb.toString();
    }
}
