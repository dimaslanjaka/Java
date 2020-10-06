package com.dimaslanjaka.tools.Libs;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.dimaslanjaka.tools.BuildConfig;
import com.dimaslanjaka.tools.Helpers.core.is;
import com.dimaslanjaka.tools.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class UpdateApp extends Activity implements Listener {
	String fileLocation;
	String downloadFolder;
	String filename;
	String extension;
	BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent intent) {
			viewLog();
		}
	};
	private DownloadManager mgr = null;
	private long lastDownload = -1L;
	BroadcastReceiver onComplete = new BroadcastReceiver() {
		public void onReceive(Context ctxt, Intent intent) {
			queryStatus();
			if (extension.equals("apk")) {
				//Permission.installApk(getApplicationContext(), fileLocation);
				try {
          /*
          Intent install = new Intent(Intent.ACTION_VIEW);
          String mimeType = "application/vnd.android.package-archive";
          Uri location = FileProvider.getUriForFile(
                  getApplicationContext(),
                  getPackageName() + ".provider",
                  new File(fileLocation));
          install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, false);
          install.setDataAndType(location, mimeType);
          install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          install.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
          install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
          startActivity(install);
           */
					File fileApkToInstall = new File(getExternalFilesDir(downloadFolder), filename);

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
						Uri apkUri = FileProvider.getUriForFile(ctxt, BuildConfig.APPLICATION_ID + ".provider",
										fileApkToInstall);
						intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
						intent.setData(apkUri);
						intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
						startActivity(intent);
					} else {
						Uri apkUri = Uri.fromFile(fileApkToInstall);
						intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	public static boolean isNewVersion(String currentVersion, String newVersion) {
		DefaultArtifactVersion minVersion = new DefaultArtifactVersion(currentVersion);
		DefaultArtifactVersion maxVersion = new DefaultArtifactVersion(newVersion);

		Log.e("current", currentVersion);
		Log.e("newer", newVersion);
		Log.e("result", "is newer " + (maxVersion.compareTo(minVersion) > 0));

		//return (version.compareTo(minVersion) < 0 || version.compareTo(maxVersion) > 0);
		return maxVersion.compareTo(minVersion) > 0;
	}

	public static Object check() throws Exception {
		int random = (int) (Math.random() * 50 + 1);
		String json = readUrl(
						"https://raw.githubusercontent.com/dimaslanjaka/Android/master/app/release/update" +
										".json?rev=" + random);
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();

		return gson.fromJson(json, Page.class);
	}

	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder buffer = new StringBuilder();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_manager);

		mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		registerReceiver(onComplete,
						new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		registerReceiver(onNotificationClick,
						new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
		Log.i(getClass().getSimpleName(), "receiver registered");

		String url2download;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				url2download = null;
			} else {
				url2download = extras.getString("URL");
			}
		} else {
			url2download = Objects.requireNonNull(savedInstanceState.getSerializable("URL")).toString();
		}
		if (url2download == null) {
			url2download = "NULL";
		}
		Log.i(getClass().getSimpleName(), url2download);
		if (is.url(url2download)) {
			download(url2download);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unregisterReceiver(onComplete);
		unregisterReceiver(onNotificationClick);
	}

	public void queryStatus() {
		Cursor c = mgr.query(new DownloadManager.Query().setFilterById(lastDownload));

		if (c == null) {
			Toast.makeText(this, "Download not found!", Toast.LENGTH_LONG).show();
		} else {
			c.moveToFirst();

			Log.d(getClass().getName(), "COLUMN_ID: " +
							c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));
			Log.d(getClass().getName(), "COLUMN_BYTES_DOWNLOADED_SO_FAR: " +
							c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
			Log.d(getClass().getName(), "COLUMN_LAST_MODIFIED_TIMESTAMP: " +
							c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
			Log.d(getClass().getName(), "COLUMN_LOCAL_URI: " +
							c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
			Log.d(getClass().getName(), "COLUMN_STATUS: " +
							c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
			Log.d(getClass().getName(), "COLUMN_REASON: " +
							c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)));

			Toast.makeText(this, statusMessage(c), Toast.LENGTH_LONG).show();
		}
	}

	public void viewLog() {
		startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
	}

	private String statusMessage(Cursor c) {
		String msg = "???";

		switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
			case DownloadManager.STATUS_FAILED:
				msg = "Download failed!";
				break;

			case DownloadManager.STATUS_PAUSED:
				msg = "Download paused!";
				break;

			case DownloadManager.STATUS_PENDING:
				msg = "Download pending!";
				break;

			case DownloadManager.STATUS_RUNNING:
				msg = "Download in progress!";
				break;

			case DownloadManager.STATUS_SUCCESSFUL:
				msg = "Download complete!";
				break;

			default:
				msg = "Download is nowhere in sight";
				break;
		}

		return (msg);
	}

	private void download(String arg0) {
		try {
			downloadFolder = Environment.DIRECTORY_DOWNLOADS; //download folder
			filename = "update.apk";
			final Uri DownloadFolderLocation = Uri.parse(
							new File(Environment.getExternalStorageDirectory(),
											downloadFolder).toString()
			); //download filename
			fileLocation = new File(
							String.valueOf(DownloadFolderLocation),
							filename
			).toString(); //full download file path
			extension = fileLocation.substring(fileLocation.lastIndexOf(".")); //FilenameUtils
			// .getExtension(fileLocation); //apk, mp3, mp4 etc

			//create folder if not exists
			File file = new File(downloadFolder);
			if (!file.exists()) {
				file.mkdirs();
			}
			//delete previous file if exists
			File outputFile = new File(fileLocation);
			if (outputFile.exists()) {
				outputFile.delete();
			}

			lastDownload =
							mgr.enqueue(
											new DownloadManager.Request(Uri.parse(arg0))
															.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
																			DownloadManager.Request.NETWORK_MOBILE)
															.setAllowedOverRoaming(false)
															.setTitle("Demo")
															.setDescription("Something useful. No, really.")
															.setDestinationInExternalPublicDir(downloadFolder, filename)
							);


      /*
      try {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri location = FileProvider.getUriForFile(
                context,
                context.getApplicationContext().getPackageName() + ".provider",
                new File(fileLocation));
        intent.setDataAndType(location, "application/vnd.android" +
                ".package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
        context.startActivity(intent);
      } catch (Exception e) {
        e.printStackTrace();
      }
       */
		} catch (Exception e) {
			Log.e("UpdateAPP", "Download error! " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void callback() {

	}

	public static class Page {
		public String code;
		public String version;
		public String url;

		@NotNull
		@Override
		public String toString() {
			return "Page{" +
							"code='" + code + '\'' +
							", version='" + version + '\'' +
							", url='" + url + '\'' +
							'}';
		}
	}
}