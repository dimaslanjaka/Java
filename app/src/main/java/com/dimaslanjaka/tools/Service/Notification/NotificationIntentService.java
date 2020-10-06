package com.dimaslanjaka.tools.Service.Notification;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import com.dimaslanjaka.tools.Helpers.core.Toastr;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;
import com.dimaslanjaka.tools.Libs.Log;
import com.dimaslanjaka.tools.Service.Facebook.Timer;
import com.dimaslanjaka.tools.Service.Facebook.Worker;

import java.util.Objects;

public class NotificationIntentService extends IntentService {

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 */
	public NotificationIntentService() {
		super("notificationIntentService");
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		assert intent != null;
		new SharedPref(getApplicationContext());
		NotificationHandler.clearNotification();
		String action = Objects.requireNonNull(intent.getAction()).toLowerCase().trim();
		Handler handler = new Handler(Looper.getMainLooper());
		//Log.out(action);
		switch (action) {
			case "stop_bot":
				handler.post(new Runnable() {
					@Override
					public void run() {
						Worker.setBot(NotificationIntentService.this.getApplicationContext(), false);
						Worker.stopBot(NotificationIntentService.this.getApplicationContext());
						NotificationHandler.setBotLog("Stopped");
						Toastr.create("Facebook reaction bot deactivated");
					}
				});
				break;
			case "start_bot":
				handler.post(new Runnable() {
					@Override
					public void run() {
						Worker.setBot(NotificationIntentService.this.getApplicationContext(), true);
						Timer.restart(NotificationIntentService.this.getApplicationContext());
						NotificationHandler.setBotLog("Starting");
						Toastr.create("Facebook reaction bot activated");
					}
				});
				break;
		}
		Log.out("Update action notification: " + action);

		// TODO change facebook button notification and update the notification
		NotificationHandler.FacebookController(getApplicationContext());
		NotificationHandler.updateNotification(getApplicationContext());
	}
}