package com.dimaslanjaka.tools.Service.Notification;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.dimaslanjaka.tools.Helpers.core.Index;
import com.dimaslanjaka.tools.Libs.Log;
import com.dimaslanjaka.tools.MainActivity;
import com.dimaslanjaka.tools.R;
import com.dimaslanjaka.tools.Service.Facebook.Timer;
import com.dimaslanjaka.tools.Service.Facebook.Worker;
import com.dimaslanjaka.tools.Service.Netspeed.v2.init;
import kotlin.jvm.internal.Intrinsics;

import java.text.ParseException;

public class NotificationHandler extends Service {
	private static final int NOTIFICATION_CHANNEL_ID = 1005;
	public static boolean ShowNotify = false;
	private static String NOTIFICATION_CHANNEL_NAME = NotificationHandler.class.getName();
	private static String NOTIFICATION_CHANNEL_TITLE = "Tools Notification";
	private static String NOTIFICATION_CHANNEL_DESCRIPTION = "Tools Notification";
	private static RemoteViews contentView = null;
	private static Notification notification = null;
	private static NotificationManager notificationManager = null;
	private static NotificationHandler INSTANCE = new NotificationHandler();
	private static RemoteViews expandedView = null;
	private static String TAG = NotificationHandler.class.getName();
	private static Context context;
	private init net2;

	public static void clearNotification() {
		if (notificationManager != null) notificationManager.cancel(NOTIFICATION_CHANNEL_ID);
	}

	public static void updateNotification(Context applicationContext) {
		context = applicationContext;
		updateNotification();
	}

	public static void updateNotification() {
		if (notificationManager != null && notification != null) {
			clearNotification();
			notificationManager.cancelAll();
			notificationManager.notify(NOTIFICATION_CHANNEL_ID, notification);
		}
		//Log.out("Update notification");
   /*
    try {
      notificationManager.cancelAll();
      notificationManager.notify(NOTIFICATION_CHANNEL_ID, mBuilder.build());
      notificationManager.notify(NOTIFICATION_CHANNEL_ID, notification);
      notificationManager.notify(NOTIFICATION_CHANNEL_ID, notification);
      notificationManager.notify(NOTIFICATION_CHANNEL_ID, notification);
    } catch (Exception e) {
      e.printStackTrace();
    }
    */
	}

	public static void FacebookController(Context context) {
		// bot button controller
		Intent intent = new Intent(context, NotificationIntentService.class);
		String btnmsg;
		if (Worker.isActive()) {
			intent.setAction("stop_bot");
			btnmsg = "Stop bot";
		} else {
			intent.setAction("start_bot");
			btnmsg = "Start bot";
		}
		//Log.out(intent.getAction(), Worker.isActive());
		expandedView.setTextViewText(
						R.id.stop_bot_button_notification, btnmsg
		);
		// set intent action
		expandedView.setOnClickPendingIntent(
						R.id.stop_bot_button_notification,
						PendingIntent.getService(context,
										0, intent,
										PendingIntent.FLAG_UPDATE_CURRENT));
		if (isNotificationVisible(context)) {
			notificationManager.cancel(NOTIFICATION_CHANNEL_ID);
		}
		updateNotification(context);
	}

	private static boolean isNotificationVisible(Context context) {
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent test = PendingIntent.getActivity(context, NOTIFICATION_CHANNEL_ID, notificationIntent, PendingIntent.FLAG_NO_CREATE);
		return test != null;
	}

	public static NotificationHandler setNetworkSpeed(String up, String down) {
		if (contentView != null) {
			ShowNotify = true;
			contentView.setTextViewText(R.id.netUp, "U: " + up);
			contentView.setTextViewText(R.id.netDown, "D: " + down);
		}
		if (expandedView != null) {
			ShowNotify = true;
			expandedView.setTextViewText(R.id.netUpExpanded, "U: " + up);
			expandedView.setTextViewText(R.id.netDownExpanded, "D: " + down);
		}
		return INSTANCE;
	}

	public static NotificationHandler setBotLog(String log) {
		if (contentView != null) {
			ShowNotify = true;
			contentView.setTextViewText(R.id.bot_status, log);
		}
		if (expandedView != null) {
			ShowNotify = true;
			expandedView.setTextViewText(R.id.bot_status_expanded, log);
		}

		return INSTANCE;
	}

	public static NotificationHandler setHotspotStatus(String log) {
		if (expandedView != null) {
			ShowNotify = true;
			expandedView.setTextViewText(R.id.hotspot_status, log);
		}

		return INSTANCE;
	}

	public NotificationCompat.Builder BuildNotify(){
		notificationManager =
						(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_NAME);
		contentView = new RemoteViews(getPackageName(), R.layout.notification);
		contentView.setImageViewResource(androidx.appcompat.R.id.image, R.mipmap.ic_launcher);
		// notification will be dismissed when tapped
		mBuilder.setAutoCancel(true);
		mBuilder.setOngoing(true);
		mBuilder.setPriority(Notification.PRIORITY_LOW);
		mBuilder.setPriority(Notification.FLAG_NO_CLEAR);
		mBuilder.setOnlyAlertOnce(true);

		// setting the custom collapsed and expanded views
		expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);
		FacebookController(getApplicationContext());

		expandedView.setOnClickPendingIntent(
						R.id.open_app_button_notification,
						PendingIntent.getActivity(getApplicationContext(),
										0, new Intent(getApplicationContext(),
														MainActivity.class), 0));
		mBuilder
						// set notification collapsed view
						.setCustomContentView(contentView)
						// set notification expanded view
						.setCustomBigContentView(expandedView)
						// setting style to DecoratedCustomViewStyle() is necessary for custom views to display
						.setStyle(new NotificationCompat.DecoratedCustomViewStyle());

		mBuilder.setContentTitle(NOTIFICATION_CHANNEL_TITLE);
		mBuilder.setContentText(NOTIFICATION_CHANNEL_DESCRIPTION);

		// tapping notification will open MainActivity
		mBuilder.setContentIntent(
						PendingIntent.getActivity(getApplicationContext(),
										0, new Intent(getApplicationContext(),
														MainActivity.class), 0));

    /*
    // show large icon on right notification layer
    mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
            R.drawable.ic_facebook_holo_light));
     */
		mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mBuilder.setSmallIcon(R.drawable.ic_facebook_holo_light);
			mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
		} else {
			mBuilder.setSmallIcon(R.drawable.ic_facebook_holo_dark);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NOTIFICATION_CHANNEL_NAME += "-OREO";
			NOTIFICATION_CHANNEL_TITLE += "-OREO";
			NOTIFICATION_CHANNEL_DESCRIPTION += "-OREO";
			// set default collapsed notification
			int importance = NotificationManager.IMPORTANCE_MIN;
			NotificationChannel channel = new NotificationChannel(
							NOTIFICATION_CHANNEL_NAME, NOTIFICATION_CHANNEL_TITLE,
							importance);
			channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
			channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
			channel.enableVibration(false);
			channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
			notificationManager.createNotificationChannel(channel);
			mBuilder.setChannelId(NOTIFICATION_CHANNEL_NAME);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mBuilder.setCategory(Notification.CATEGORY_SERVICE);
		}

		//silent sound
		mBuilder.setDefaults(Notification.DEFAULT_ALL);
		return mBuilder;
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public void onCreate() {
		super.onCreate();
		if (notificationManager == null) {
			notification = BuildNotify().build();
			Intrinsics.checkNotNullExpressionValue(notification, "notification");
			try {
				startForeground(NOTIFICATION_CHANNEL_ID, notification);
				notificationManager.notify(NOTIFICATION_CHANNEL_ID, notification);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					getNetworkSpeed();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TODO start facebook reaction bot scheduler
		if (!Timer.isScheduled && Worker.isActive()) {
			Timer.setOnetimeTimer(getApplicationContext());
		}
		// TODO check hotspot AP
		// com.dimaslanjaka.tools.Service.Hotspot.Worker.test(getApplicationContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
    /*
    START_STICKY	: the system will try to re-create your service after it is killed
    START_NOT_STICKY: the system will not try to re-create your service after it is killed
     */
		return START_STICKY;
	}

	/**
	 * Notify show
	 */
	public void show() {
		if (ShowNotify) {
			updateNotification();
			// turn off ShowNotify prevents loop
			ShowNotify = false;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//TODO restart notification service
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("restart_service");
		broadcastIntent.setClass(this, Restarter.class);
		this.sendBroadcast(broadcastIntent);
		Log.out("restart notification service");
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	private void getNetworkSpeed() {
    /*
    Netspeed speed = new Netspeed(getApplicationContext());
    speed.doTest();
     */
		net2 = new init(getApplicationContext());
		net2.start();
		net2.register();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		net2.register();
		new Index(getApplicationContext());
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		net2.unregister();
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		new Index(getApplicationContext());
	}
}
