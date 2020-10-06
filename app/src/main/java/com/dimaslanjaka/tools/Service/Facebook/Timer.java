package com.dimaslanjaka.tools.Service.Facebook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;
import com.dimaslanjaka.tools.Service.Notification.NotificationHandler;

public class Timer {

	public static boolean isScheduled = false;
	public static long triggerAtMillis = 0;
	private static PendingIntent pi;

	public static void setOnetimeTimer(Context context) {
		if (!isScheduled) {
			if (Worker.isActive()) {
				isScheduled = true;
				long minutes = SharedPref.getPref().getLong("fb-bot-run-every", 5);
				AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				pi = PendingIntent.getBroadcast(context, 0, new Intent(context, RunnerBroadcast.class), 0);
				triggerAtMillis = System.currentTimeMillis() + (1000 * 60 * minutes);
				NotificationHandler.setBotLog("Bot will run at " + getHtime(triggerAtMillis)).show();
				am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
			} else {
				NotificationHandler.setBotLog("Stopped, bot inactive").show();
			}
		}
	}

	public static String getNextScheduleTime() {
		if (triggerAtMillis != 0) return getHtime(triggerAtMillis);
		return null;
	}

	public static void cancel(Context context) {
		if (pi != null) {
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pi);
		}
		isScheduled = false;
		NotificationHandler.setBotLog("timer cancelled, with pending intent = " + (pi != null)).show();
	}

	public static void restart(Context context) {
		cancel(context);
		NotificationHandler.setBotLog("timer restarted at = " + getHtime(0)).show();
		setOnetimeTimer(context);
	}

	public static String getHtime(long milliseconds) {
		java.util.Date date;
		if (milliseconds != 0) {
			date = new java.util.Date(milliseconds);
		} else {
			date = new java.util.Date();
		}
		return android.text.format.DateFormat.format(
						"hh:mm:ss a", date
		).toString();
	}
}
