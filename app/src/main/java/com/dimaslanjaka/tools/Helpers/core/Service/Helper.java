package com.dimaslanjaka.tools.Helpers.core.Service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class Helper {
	final public static boolean debug = false;
	public static boolean running = false;
	/**
	 * Repeaters list
	 */
	public static List<String> Repeaters = new ArrayList<>();

	public Helper(Context c) {
		running = true;

	}

	/**
	 * set timer for run service every x minutes
	 *
	 * @param extra   intent putExtra(name
	 * @param context context
	 * @param minutes x minutes
	 * @see "https://stackoverflow.com/a/13820775"
	 */
	public static void setOnetimeTimer(Context context, String extra, int minutes,
	                                   Class<?> classname) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, classname);
		intent.putExtra(extra, Boolean.TRUE);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * minutes), pi);
	}

	@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
	public static boolean isAppOnForeground(Context context, String appPackageName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null) {
			return false;
		}
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(appPackageName)) {
				Log.i("isAppOnForeground", appPackageName + " in foreground");
				return true;
			}
		}
		Log.e("isAppOnForeground", appPackageName + " in background");
		return false;
	}

	public static boolean isRunning() {
		return running;
	}

	/**
	 * @param serviceClass class
	 * @return boolean
	 * @see "https://stackoverflow.com/questions/30525784/android-keep-service-running-when-app-is-killed"
	 */
	public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				if (debug) {
					Log.i(serviceClass.getName(), " Is Running");
				}
				return true;
			}
		}
		if (debug) {
			Log.e(serviceClass.getName(), " Stopped");
		}
		return false;
	}

	/**
	 * Restart service
	 *
	 * @param packageContext context
	 * @todo call this function from onDestroy override
	 */
	public static void restartService(
					@androidx.annotation.NonNull Context packageContext,
					Class<?> serviceRestarterClass
	) {
		//packageContext.stopService(new Intent(packageContext, serviceRestarterClass));
		//packageContext.startService(new Intent(packageContext, serviceRestarterClass));
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("restartservice");
		broadcastIntent.setClass(packageContext, serviceRestarterClass);
		packageContext.sendBroadcast(broadcastIntent);
	}

	public static void startService(
					@androidx.annotation.NonNull Context packageContext,
					Class<?> serviceClass
	) {
		if (!isServiceRunning(packageContext, serviceClass)) {
			Intent mServiceIntent = new Intent(packageContext, serviceClass);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				packageContext.startForegroundService(mServiceIntent);
			} else {
				packageContext.startService(mServiceIntent);
			}
		}
	}

	public static void stopService(
					@androidx.annotation.NonNull Context packageContext,
					Class<?> serviceClass
	) {
		if (!isServiceRunning(packageContext, serviceClass)) {
			packageContext.stopService(new Intent(packageContext, serviceClass));
		}
	}

	/**
	 * Repeater service that trigger the restarter of service
	 *
	 * @param context        Context
	 * @param broadcastClass Broadcast Class Restarter Service
	 * @param minutes        Minutes to repeat
	 * @param activate       Activate or Deactivate
	 */
	public static void Repeater(Context context, Class<?> broadcastClass, int minutes,
	                            boolean activate) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, broadcastClass);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		if (isRepeaterActive(broadcastClass)) {
			Repeater(context, broadcastClass, 1, false);
		}
		if (activate) {
			Repeaters.add(broadcastClass.getName());
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (1000 * 60 * minutes),
							pendingIntent);
		} else {
			Repeaters.remove(broadcastClass.getName());
			alarmManager.cancel(pendingIntent);
		}
	}

	public static boolean isRepeaterActive(Class<?> className) {
		return Repeaters.contains(className.getName());
	}
}
