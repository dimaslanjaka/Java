package com.dimaslanjaka.tools.Helpers.core;

import android.content.Context;

import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;

public class Index {
  public Index(Context c) {
    new Toastr(c);
    new SharedPref(c);
  }
}
