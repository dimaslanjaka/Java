package com.dimaslanjaka.tools.Service.Facebook;

import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

public class Handler {
	public static android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
	public static List<Runnable> Tasks = new ArrayList<>();

	public static void addTask(Runnable runnable, long delayMillis) {
		Tasks.add(runnable);
		handler.postDelayed(Tasks.get(Tasks.indexOf(runnable)), delayMillis);
	}

	public static void cancelTask(Runnable runnable) {
		handler.removeCallbacks(runnable);
	}

	public static void cancelTask() {
		if (!Tasks.isEmpty()) {
			for (int i = 0; i < Tasks.size(); i++) {
				cancelTask(Tasks.get(i));
			}
		}
	}

	public static void removeTask(Runnable runnable) {
		Tasks.remove(Tasks.get(Tasks.indexOf(runnable)));
	}
}
