package com.dimaslanjaka.tools.Helpers.core;

import android.content.Context;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;
import com.dimaslanjaka.tools.Service.Netspeed.v2.Database;

public class Index {
	public Index(Context c) {
		new Toastr(c);
		new SharedPref(c);
		new Database(c);
	}
}
