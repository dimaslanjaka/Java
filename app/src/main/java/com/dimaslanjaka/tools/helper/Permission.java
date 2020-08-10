package com.dimaslanjaka.tools.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {
    private static final int PERMISSION_REQUEST_CODE = 1;
    public Activity Act;

    public Permission() {
    }

    public Permission(Activity activity) {
        Act = activity;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
                Log.i("permission", "already granted");
            } else {
                requestPermission(); // Code for permission
            }
        } else {
            Log.e("permission", "Running on android below API 23");
            // Code for Below 23 API Oriented Device
            // Do next code
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Act, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        String[] Perm = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ActivityCompat.shouldShowRequestPermissionRationale(Act, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(Act, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(Act, "Write External Storage permission allows us to do store files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Act, Perm, PERMISSION_REQUEST_CODE);
        }
    }

    //@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
    }
}
