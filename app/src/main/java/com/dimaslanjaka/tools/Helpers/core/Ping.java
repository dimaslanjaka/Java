package com.dimaslanjaka.tools.Helpers.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

/**
 * @see "https://stackoverflow.com/questions/3905358/how-to-ping-external-ip-from-java-android"
 */
public class Ping {
	public String net = "NO_CONNECTION";
	public String host = "";
	public String ip = "";
	public int dns = Integer.MAX_VALUE;
	public int cnt = Integer.MAX_VALUE;

	public static Ping ping(URL url, Context ctx) {
		Ping r = new Ping();
		if (isNetworkConnected(ctx)) {
			r.net = getNetworkType(ctx);
			try {
				String hostAddress;
				long start = System.currentTimeMillis();
				hostAddress = InetAddress.getByName(url.getHost()).getHostAddress();
				long dnsResolved = System.currentTimeMillis();
				Socket socket = new Socket(hostAddress, url.getPort());
				socket.close();
				long probeFinish = System.currentTimeMillis();
				r.dns = (int) (dnsResolved - start);
				r.cnt = (int) (probeFinish - dnsResolved);
				r.host = url.getHost();
				r.ip = hostAddress;
			} catch (Exception ex) {
				Log.e("Ping", "Unable to ping");
			}
		}
		return r;
	}

	public static boolean executeCommand() {
		System.out.println("executeCommand");
		Runtime runtime = Runtime.getRuntime();
		try {
			Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
			int mExitValue = mIpAddrProcess.waitFor();
			System.out.println(" mExitValue " + mExitValue);
			return mExitValue == 0;
		} catch (InterruptedException x) {
			x.printStackTrace();
			System.out.println(" Exception:" + x);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(" Exception:" + e);
		}
		return false;
	}

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm =
						(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	@Nullable
	public static String getNetworkType(Context context) {
		ConnectivityManager cm =
						(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null) {
			return activeNetwork.getTypeName();
		}
		return null;
	}
}