package com.dimaslanjaka.tools.Service.Facebook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;

public class RunnerBroadcast extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// restart scheduler
		Timer.restart(context);

		// start runner again
		try {
			Worker.runBot(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}