package com.dimaslanjaka.tools.Service.Netspeed;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;
import com.dimaslanjaka.tools.Service.Notification.NotificationHandler;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Netspeed {
	private static final boolean SHOW_SPEED_IN_BITS = false;
	public static Handler handler = new Handler(Looper.getMainLooper());
	private static int NotificationID;
	private static RemoteViews contentView = null;
	private static Notification notification = null;
	private static NotificationManager notificationManager = null;
	private static NotificationCompat.Builder mBuilder = null;
	private long exRX;
	private long exTX;
	private long nowTX;
	private long nowRX;
	private double rxBPS;
	private double txBPS;
	private Context context;

	public Netspeed(Context applicationContext) {
		this.context = applicationContext;
	}

	public void doTest() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				new SharedPref(context);
				if (Objects.requireNonNull(SharedPref.getPref()).getBoolean("netspeed", true)) {
					String s, d, u;
					double rxDiff = 0, txDiff = 0;
					if (exRX == 0 || exTX == 0) {
						exTX = TrafficStats.getTotalTxBytes();
						exRX = TrafficStats.getTotalRxBytes();
					}
					nowTX = TrafficStats.getTotalTxBytes();
					nowRX = TrafficStats.getTotalRxBytes();
					rxDiff = nowRX - exRX;
					txDiff = nowTX - exTX;

					rxBPS = (rxDiff / (1000 / 1000));
					txBPS = (txDiff / (1000 / 1000));

					exRX = nowRX;
					exTX = nowTX;

					s = calculateData(txBPS + rxBPS);
					d = calculateData(rxBPS);
					u = calculateData(txBPS);
					//Data.setData(s, d, u, txBPS + rxBPS, rxBPS, txBPS);
					//Log.out("total: " + s);
					//Log.out("upload: " + u);
					//Log.out("download: " + d);
					//Log.e(String.valueOf(txBPS + rxBPS), Utils.parseSpeed(txBPS + rxBPS, false));
					NotificationHandler.setNetworkSpeed(u, d).show();
				}
			}

		};
		Timer timer = new Timer();
		timer.schedule(task, 0, 999);
	}

	private String calculateData(double a) {
		String res;

		if (a / 1000 >= 1) {
			if ((a / 1000) / 1000000 >= 1) {
				res = Math.abs((int) (a / 1000) / 1000000) + " GB/s";
			} else {
				if ((a / 1000) / 1000 >= 1) {
					res = Math.abs((int) (a / 1000) / 1000) + " MB";
				} else {
					res = Math.abs((int) a / 1000) + " KB";
				}
			}
		} else {
			res = Math.abs((int) a) + " B";
		}
		return res;

	}

	private void getTrans() {
		Data.dailyTotlSend = TrafficStats.getTotalTxBytes();
		Data.dailyTotalRecive = TrafficStats.getTotalRxBytes();
		Data.totalsend = TrafficStats.getTotalTxBytes();
		Data.totalrec = TrafficStats.getTotalRxBytes();
		Data.mobilesend = TrafficStats.getMobileTxBytes();
		Data.mobilerec = TrafficStats.getMobileRxBytes();
		Data.wifirec = TrafficStats.getTotalRxBytes() - TrafficStats.getMobileRxBytes();
		Data.wifisend = TrafficStats.getTotalTxBytes() - TrafficStats.getMobileTxBytes();
	}


}
