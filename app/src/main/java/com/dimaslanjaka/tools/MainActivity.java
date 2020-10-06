package com.dimaslanjaka.tools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.dimaslanjaka.tools.Helpers.core.Index;
import com.dimaslanjaka.tools.Helpers.permission.Permission;
import com.dimaslanjaka.tools.Libs.Net.Curl;
import com.dimaslanjaka.tools.Libs.Net.Net;
import com.dimaslanjaka.tools.Libs.UpdateApp;
import com.dimaslanjaka.tools.Service.Facebook.Worker;
import com.dimaslanjaka.tools.Service.Notification.NotificationHandler;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
				EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
	Handler handler = new Handler(Looper.getMainLooper());
	String TAG = this.getClass().getName();
	private AppBarConfiguration mAppBarConfiguration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG);
		new Index(getApplicationContext());
		if (BuildConfig.DEBUG) {
			if (!isMyServiceRunning(com.facebook.stetho.Stetho.class)) {
				//Stetho.initializeWithDefaults(this);
				com.facebook.stetho.Stetho.initialize(
								com.facebook.stetho.Stetho.newInitializerBuilder(this)
												.enableDumpapp(com.facebook.stetho.Stetho.defaultDumperPluginsProvider(this))
												.enableWebKitInspector(com.facebook.stetho.Stetho.defaultInspectorModulesProvider(this))
												.build()
				);
				new OkHttpClient.Builder()
								.addNetworkInterceptor(new com.facebook.stetho.okhttp3.StethoInterceptor())
								.build();
			}
		}

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Permission perm = new Permission(this);
		if (!perm.hasStoragePermission() || !perm.hasUnknownSources()) {
			perm.reqStoragePermission();
			perm.reqUnknownSources();
		} else {
			startApp();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		// Forward results to EasyPermissions
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	public void onPermissionsGranted(int requestCode, @NotNull List<String> list) {
		startApp();
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
		setContentView(R.layout.permission_required);
		Button restarter = findViewById(R.id.btn_restart);
		restarter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity.this.doRestart();
			}
		});
		// (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
		// This will display a dialog directing them to enable the permission in app settings.
		if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
			new AppSettingsDialog.Builder(this).build().show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
			// Do something after user returned from app settings screen, like showing a Toast.
			Toast.makeText(this, "Cannot access this app without allow given permissions",
							Toast.LENGTH_SHORT).show();
		}
	}

	private void startApp() {
		setContentView(R.layout.activity_main);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		NavigationView navigationView = findViewById(R.id.nav_view);
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		mAppBarConfiguration = new AppBarConfiguration.Builder(
						R.id.nav_home, R.id.nav_ytmp3, R.id.nav_settings, R.id.nav_about
		).setDrawerLayout(drawer).build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);

		handler = new Handler(Looper.getMainLooper());
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (Net.isInternetAvailable(getApplicationContext()) && !BuildConfig.DEBUG) {
					checkUpdate();
				}
			}
		}, 3000);

		// start notification
		if (!isMyServiceRunning(NotificationHandler.class)) {
			Intent notifIntent = new Intent(getApplicationContext(), NotificationHandler.class);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				startForegroundService(notifIntent);
			} else {
				startService(notifIntent);
			}
		}
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				Log.i("Service status", "Service Running");
				return true;
			}
		}
		Log.i("Service status", "Not running");
		return false;
	}

	/**
	 * Restart activity
	 */
	public void doRestart() {
		finish();
		startActivity(getIntent());
		overridePendingTransition(0, 0);
	}

	@Override
	public boolean onOptionsItemSelected(@NotNull MenuItem menuItem) {
		super.onOptionsItemSelected(menuItem);
		int itemId = menuItem.getItemId();
		if (itemId == R.id.action_settings) {
			Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void checkUpdate() {
		Log.e("update", "check");
		try {
			final UpdateApp.Page check = (UpdateApp.Page) UpdateApp.check();
			boolean up = UpdateApp.isNewVersion(
							BuildConfig.VERSION_NAME, check.version);
			Log.e("update", up ? "current has newer" : "current is newer");
			if (up) {
				Log.i(TAG, "Show dialog update");
				URL read = new URL("https://raw.githubusercontent" +
								".com/dimaslanjaka/Android/master/release-notes.txt");
				BufferedReader in = new BufferedReader(new InputStreamReader(read.openStream()));

				String inputLine;
				StringBuilder textLine = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					textLine.append(inputLine).append("\n");
				}

				in.close();

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				// Get the layout inflater
				View inflater = (this).getLayoutInflater().inflate(R.layout.dialog, null);
				// Inflate and set the layout for the dialog
				// Pass null as the parent view because its going in the
				// dialog layout
				TextView dialogMsg = inflater.findViewById(R.id.dialogmsg);
				if (textLine.length() > 0) {
					dialogMsg.setText(textLine);
				} else {
					dialogMsg.setText("Failed getting changelog");
				}
				builder.setTitle("Update Available");
				builder.setCancelable(false);
				//builder.setIcon(R.drawable.common_google_signin_btn_icon_light_normal);
				builder.setView(inflater);
				// Add action buttons
				builder.setPositiveButton("Update", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(android.content.DialogInterface dialogInterface, int i) {
						if (!check.url.isEmpty()) {
							startBrowser(check.url);
						}
						dialogInterface.dismiss();
					}
				});
				builder.setCancelable(false);
				builder.create();
				builder.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startBrowser(@NotNull String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		MainActivity.this.startActivity(browserIntent);
	}

	@Override
	public void onRationaleAccepted(int requestCode) {

	}

	@Override
	public void onRationaleDenied(int requestCode) {

	}
}
