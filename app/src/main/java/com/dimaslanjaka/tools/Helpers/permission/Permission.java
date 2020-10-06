package com.dimaslanjaka.tools.Helpers.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import com.dimaslanjaka.tools.Helpers.core.Storage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.File;

public class Permission {
	public Activity Act;
	public Context context;
	public String TAG = "Permissions";

	public Permission(Activity activity) {
		Act = activity;
		/*
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
		 */
	}

	public static void installApk(Context c, String fileLocation) {
		Intent install;
		if (!Storage.isFileExists(fileLocation)) {
			return;
		}
		try {
			install = new Intent(Intent.ACTION_VIEW);
			Uri location = FileProvider.getUriForFile(
							c,
							c.getApplicationContext().getPackageName() + ".provider",
							new File(fileLocation));
			install.setDataAndType(location, "application/vnd.android" +
							".package-archive");
			install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
			c.startActivity(install);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean hasCameraPermission() {
		return EasyPermissions.hasPermissions(Act, Manifest.permission.CAMERA);
	}

	private boolean hasLocationAndContactsPermissions() {
		return EasyPermissions.hasPermissions(Act, PermissionCode.LOCATION_AND_CONTACTS);
	}

	private boolean hasSmsPermission() {
		return EasyPermissions.hasPermissions(Act, Manifest.permission.READ_SMS);
	}

	@RequiresApi(api = Build.VERSION_CODES.DONUT)
	public boolean hasStoragePermission() {
		return EasyPermissions.hasPermissions(Act, Manifest.permission.WRITE_EXTERNAL_STORAGE);
	}

	public boolean hasUnknownSources() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			return Act.getPackageManager().canRequestPackageInstalls();
		return false;
	}

	@RequiresApi(api = Build.VERSION_CODES.DONUT)
	public void reqStoragePermission() {
		if (!hasStoragePermission()) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				ActivityCompat.requestPermissions(Act, new String[]{
								Manifest.permission.WRITE_EXTERNAL_STORAGE
				}, 1);
			}
		}
	}

	public void reqUnknownSources() {
		//request unknown sources
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			//Act.startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES));
			Act.startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData
							(Uri.parse(String.format("package:%s", Act.getPackageName()))), 1234);
		}
	}

	@AfterPermissionGranted(PermissionCode.LOCATION)
	public void requestLocationPermission() {
		String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
		if (EasyPermissions.hasPermissions(Act, perms)) {
			Toast.makeText(Act, "Permission already granted", Toast.LENGTH_SHORT).show();
		} else {
			EasyPermissions.requestPermissions(Act, "Please grant the location permission",
							PermissionCode.LOCATION,
							perms);
		}
	}
}
