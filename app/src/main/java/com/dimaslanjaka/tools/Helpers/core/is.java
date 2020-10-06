package com.dimaslanjaka.tools.Helpers.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class is {
	public static boolean url(String url) {
		return URLUtil.isValidUrl(url);
	}

	public static boolean hasActiveInternetConnection(Context context) {
		if (isNetworkAvailable(context)) {
			try {
				HttpURLConnection urlc =
								(HttpURLConnection) (new URL("http://www.example.com").openConnection());
				urlc.setRequestProperty("User-Agent", "Test");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(1500);
				urlc.setReadTimeout(1500);
				urlc.setInstanceFollowRedirects(true);
				urlc.connect();
				return (urlc.getResponseCode() == 200);
			} catch (IOException e) {
				Log.e("Network", "Error checking internet connection", e);
				return false;
			}
		} else {
			Log.d("Network", "No network available!");
			return false;
		}
	}

	private static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager
						= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
		return (netInfo != null && netInfo.isConnected());
	}
}
