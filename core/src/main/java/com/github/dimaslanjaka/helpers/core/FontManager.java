package com.dimaslanjaka.tools.Helpers.core;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {
  public static final String ROOT = "fonts/",
          FONTAWESOME_BRANDS = ROOT + "fa-brands-400.ttf";

  public static Typeface getTypeface(Context context, String font) {
    return Typeface.createFromAsset(context.getAssets(), font);
  }
}