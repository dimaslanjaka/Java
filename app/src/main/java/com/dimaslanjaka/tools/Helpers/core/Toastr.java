package com.dimaslanjaka.tools.Helpers.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class Toastr extends Toast {
	public static int duration = Toast.LENGTH_LONG;
	@SuppressLint("StaticFieldLeak")
	public static Context context = null;

	public Toastr(Context context) {
		super(context);
		Toastr.context = context;
	}

	public static void create(CharSequence text) {
		if (context != null) {
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
}
