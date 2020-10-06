package com.dimaslanjaka.tools.Libs.Net;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.dimaslanjaka.tools.Facebook.WebkitCookieManager;
import com.dimaslanjaka.tools.Global;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;
import com.dimaslanjaka.tools.Libs.Log;
import org.json.JSONObject;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Curl {
	public static final boolean debug = false;
	public static final String COOKIES_HEADER = "Set-Cookie";
	public static final java.net.CookieManager msCookieManager = new java.net.CookieManager();
	private static final List<String> policied = new ArrayList<>();
	public static List<String> post_ids = new ArrayList<>();
	public static HttpURLConnection conn;
	public static Object Worker;
	public static JSONObject postInfos;
	public static String lastId = null;
	public static RemoteViews contentView;
	public static Notification notification;
	public static NotificationManager notificationManager;
	public static Handler handler = new Handler(Looper.getMainLooper());

	static {
		new SharedPref(Global.getContext());
		lastId = Objects.requireNonNull(SharedPref.getPref()).getString("fb-last-reacted-id", "");
	}

	public static HttpURLConnection connection(String uRL) throws IOException {
		return connection(uRL, "get", true);
	}

	/**
	 * Curl
	 *
	 * @param uRL      url target
	 * @param Facebook facebook header
	 * @return HttpURLConnection
	 * @throws IOException error
	 */
	public static HttpURLConnection connection(String uRL, String method, boolean Facebook) throws IOException {
		if (!policied.contains(uRL)) {
			policy();
			policied.add(uRL);
		}
		URL url = new URL(uRL);
		conn = (HttpURLConnection) url.openConnection();
		loadCookie(conn);
		conn.setRequestMethod(method.toUpperCase());
		if (Facebook) {
			conn.setRequestProperty("Accept-Language", "id-ID,id;q=0.9,en-US;q=0.8,en;q=0.7,ms;q=0.6");
			conn.setRequestProperty("User-Agent", "Opera/9.80 (J2ME/MIDP; Opera Mini/4.5.40312/37.7751; U; en) Presto/2.12.423 Version/12.16");
			conn.getContent();
		}
		//Log.out(connection.getRequestProperties());

		return conn;
	}

	public static HttpURLConnection post(String uRL, String data) throws IOException {
		policy();
		URL url = new URL(uRL);
		conn = (HttpURLConnection) url.openConnection();
		loadCookie(conn);
		conn.setRequestMethod("POST");
		byte[] postData = data.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		conn.setUseCaches(false);

		return conn;
	}

	public static WebkitCookieManager setupCookie() {
		//setup cookie handler
		android.webkit.CookieSyncManager.createInstance(Global.getContext());
		android.webkit.CookieManager.getInstance().setAcceptCookie(true);
		WebkitCookieManager coreCookieManager = new WebkitCookieManager(null, java.net.CookiePolicy.ACCEPT_ALL);
		java.net.CookieHandler.setDefault(coreCookieManager);
		return coreCookieManager;
	}

	public static File getCookieFile(HttpURLConnection connection) throws IOException {
		String url = connection.getURL().toString();
		return getCookieFile(url);
	}

	public static File getCookieFile(String url) {
		//Log.out("Getting Cookie file of " + url);
		String cookieName;
		if (url.contains("facebook.com")) {
			cookieName = "facebook.txt";
		} else if (url.contains("telkomsel.com")) {
			SharedPreferences preferences = Global
							.getContext()
							.getSharedPreferences("telkomsel", Context.MODE_PRIVATE);
			String msisdn = preferences.getString("msisdn", "default");
			cookieName = msisdn + ".txt";
		} else {
			cookieName = "default.txt";
		}
		File homedir = new File(Global.getContext().getFilesDir().getAbsolutePath());
		if (!homedir.exists()) homedir.mkdir();
		File cookieDir = new File(homedir, "curl");
		if (!cookieDir.exists()) cookieDir.mkdir();
		File cookiePath = new File(cookieDir, cookieName);
		if (!cookiePath.exists()) {
			try {
				cookiePath.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cookiePath;
	}

	public static String md5(String input) {
		try {

			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			//  of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Save cookie as name1=value1; name2=value2
	 *
	 * @param url
	 * @param cookies
	 */
	public static void saveCookie(String url, String cookies) {
		try {
			URL Url = new URL(url);
			conn = (HttpURLConnection) Url.openConnection();
			File cookiePath = getCookieFile(conn);
			FileWriter writer = new FileWriter(cookiePath.getAbsoluteFile());
			writer.write(cookies);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveCookie(HttpURLConnection connection) {
		//Log.out("Saving cookies of " + conn.getURL().toString());
		//Get Cookies from response header and load them into cookieManager:
		Map<String, List<String>> headerFields = connection.getHeaderFields();
		List<String> cookiesHeader = headerFields.get("Set-Cookie");

		if (cookiesHeader != null) {
			//Log.out("cookiesHeader", cookiesHeader);
			//HashSet<String> cookies2save = new HashSet<>();
			for (String cookie : cookiesHeader) {
				List<HttpCookie> parse = HttpCookie.parse(cookie);
				msCookieManager.getCookieStore().add(null, parse.get(0));
				//cookies2save.add(cookie);
			}
			//Log.out("parse", cookies2save);
		}
	}

	private static String readFromFile(File path) {

		String ret = "";

		try {
			FileInputStream fis = new FileInputStream(path.getAbsoluteFile());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
			StringBuilder stringBuilder = new StringBuilder();

			String receiveString;
			while ((receiveString = bufferedReader.readLine()) != null) {
				stringBuilder.append("\n").append(receiveString);
			}
			bufferedReader.close();
			fis.close();
			ret = stringBuilder.toString();
		} catch (FileNotFoundException e) {
			Log.out("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.out("login activity", "Can not read file: " + e.toString());
		}

		return ret;
	}

	public static void parseCookie(HttpURLConnection connection) {
		saveCookie(connection);
		List<HttpCookie> cookies = msCookieManager.getCookieStore().getCookies();
		Log.out(connection.getHeaderField("Set-Cookie"));
	}

	public static void loadCookie(HttpURLConnection connection) {
		if (msCookieManager.getCookieStore().getCookies().size() > 0) {
			// While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
			connection.setRequestProperty("Cookie",
							TextUtils.join(";", msCookieManager.getCookieStore().getCookies()));
		}
	}

	public static String readResponse(HttpURLConnection connection) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		StringBuilder response = new StringBuilder();
		while ((line = in.readLine()) != null) {
			response.append(line).append("---");
		}
		in.close();
		return response.toString();
	}

	public static void policy() {
		//Load Policy
		int SDK_INT = android.os.Build.VERSION.SDK_INT;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
}
