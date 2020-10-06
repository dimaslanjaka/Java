package com.dimaslanjaka.tools.Helpers.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;

import java.io.*;

/**
 * Root detector
 *
 * @author Kevin Kowalewski
 */
public class RootUtil extends SharedPref {
	private static final String TAG = RootUtil.class.getName();
	public static boolean isRooted = false;
	public static SharedPreferences pref = null;

	public RootUtil(Context context) {
		super(context);
		new SharedPref(context);
		pref = SharedPref.getPref();
	}

	@RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
	public static void requestRoot() {
		if (isDeviceRooted()) {
			CheckSuperuser();
		} else {
			Log.e("requestRoot", "Device Is Not Rooted");
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
	public static boolean isDeviceRooted() {
		pref = SharedPref.getPref();
		isRooted = checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
		if (pref != null) pref.edit().putBoolean("RootStatus", isRooted).apply();
		return isRooted;
	}

	private static boolean checkRootMethod1() {
		String buildTags = Build.TAGS;
		return buildTags != null && buildTags.contains("test-keys");
	}

	private static boolean checkRootMethod2() {
		String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
						"/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
		for (String path : paths) {
			if (new File(path).exists()) return true;
		}
		return false;
	}

	private static boolean checkRootMethod3() {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			return in.readLine() != null;
		} catch (Throwable t) {
			return false;
		} finally {
			if (process != null) process.destroy();
		}
	}

	/**
	 * run command with root access
	 *
	 * @param strArr command to run
	 */
	public static void Execute(final String... strArr) {
		Handler handler = new Handler(Looper.getMainLooper());
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					Process exec = Runtime.getRuntime().exec("su");
					DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
					for (String str : strArr) {
						//com.dimaslanjaka.tools.Libs.Log.out(str);
						dataOutputStream.writeBytes(str + "\n");
						dataOutputStream.flush();
					}
					dataOutputStream.writeBytes("exit\n");
					dataOutputStream.flush();
					try {
						exec.waitFor();
					} catch (InterruptedException e) {
						Log.e(e.getClass().getName(), e.getMessage(), e);
						e.printStackTrace();
					}
					dataOutputStream.close();
					exec.destroy();
				} catch (IOException e2) {
					Log.e(e2.getClass().getName(), e2.getMessage(), e2);
				}
			}
		}, 3000);
	}

	public static boolean CheckSuperuser() {
		try {
			Process exec = Runtime.getRuntime().exec("su");
			DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
			dataOutputStream.writeBytes("id\nexit\n");
			dataOutputStream.flush();
			dataOutputStream.close();
			StringBuilder sb = new StringBuilder();
			while (true) {
				String readLine = bufferedReader.readLine();
				if (readLine != null) {
					sb.append(readLine);
				} else {
					bufferedReader.close();
					exec.waitFor();
					exec.destroy();
					return !sb.toString().contains("uid=0");
				}
			}
		} catch (Exception unused) {
			return true;
		}
	}

	private void prepareKvmKernelModule() {
		try {
			Process p = Runtime.getRuntime().exec("su");
			DataOutputStream outputStream = new DataOutputStream(p.getOutputStream());
			outputStream.writeBytes("insmod /lib/modules/kvm.ko\n");
			outputStream.flush();
			outputStream.writeBytes("insmod /lib/modules/kvm-intel.ko\n");
			outputStream.flush();
			outputStream.writeBytes("chmod 777 /dev/kvm\n");
			outputStream.flush();
			outputStream.writeBytes("exit\n");
			outputStream.flush();
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String Execute2(String... strArr) {
		try {
			Process exec = Runtime.getRuntime().exec("su");
			DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
			for (String str : strArr) {
				dataOutputStream.writeBytes(str + "\n");
				dataOutputStream.flush();
			}
			dataOutputStream.writeBytes("exit\n");
			dataOutputStream.flush();
			StringBuilder sb = new StringBuilder();
			while (true) {
				String readLine = bufferedReader.readLine();
				if (readLine == null) {
					break;
				}
				sb.append(readLine);
			}
			bufferedReader.close();
			try {
				exec.waitFor();
			} catch (InterruptedException e) {
				Log.e(e.getClass().getName(), e.getMessage(), e);
				e.printStackTrace();
			}
			dataOutputStream.close();
			exec.destroy();
			return sb.toString();
		} catch (IOException e2) {
			Log.e(e2.getClass().getName(), e2.getMessage(), e2);
			return null;
		}
	}
}