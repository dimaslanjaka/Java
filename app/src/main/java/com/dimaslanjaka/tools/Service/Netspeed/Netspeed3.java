package com.dimaslanjaka.tools.Service.Netspeed;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.dimaslanjaka.tools.Service.Notification.NotificationHandler;

public class Netspeed3 {
	private Handler mHandler = new Handler(Looper.getMainLooper());
	private long mStartRX = 0;
	private long mStartTX = 0;
	private final Runnable mRunnable = new Runnable() {
		public void run() {
			String Upload = "0";
			String Download = "0";
			// long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
			// RX.setText(Long.toString(rxBytes));
			long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
			Download = rxBytes + " bytes";
			if (rxBytes >= 1024) {
				// KB or more
				long rxKb = rxBytes / 1024;
				Download = rxKb + " KBs";
				if (rxKb >= 1024) {
					// MB or more
					long rxMB = rxKb / 1024;
					Download = rxMB + " MBs";
					if (rxMB >= 1024) {
						// GB or more
						long rxGB = rxMB / 1024;
						Download = Long.toString(rxGB);
					}// rxMB>1024
				}// rxKb > 1024
			}// rxBytes>=1024

			// long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
			// TX.setText(Long.toString(txBytes));
			long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
			Upload = txBytes + " bytes";
			if (txBytes >= 1024) {
				// KB or more
				long txKb = txBytes / 1024;
				Upload = txKb + " KBs";
				if (txKb >= 1024) {
					// MB or more
					long txMB = txKb / 1024;
					Upload = txMB + " MBs";
					if (txMB >= 1024) {
						// GB or more
						long txGB = txMB / 1024;
						Upload = Long.toString(txGB);
					}// rxMB>1024
				}// rxKb > 1024
			}// rxBytes>=1024

			NotificationHandler.setNetworkSpeed(Upload, Download).show();
			mHandler.postDelayed(mRunnable, 1000);
		}
	};

	public void init() {
		mStartRX = TrafficStats.getTotalRxBytes();
		mStartTX = TrafficStats.getTotalTxBytes();

		if (mStartRX == TrafficStats.UNSUPPORTED
						|| mStartTX == TrafficStats.UNSUPPORTED) {
			Log.e("Netspeed3", "Unsupported");
		} else {
			mHandler.postDelayed(mRunnable, 1000);
		}
	}
}