package com.dimaslanjaka.tools.Helpers.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class Formsaver {
  /**
   * {@link SharedPreferences}
   */
  public static SharedPreferences pref = null;
  Context context = null;

  private void loadPref() {
    if (pref == null) {
      pref = context.getSharedPreferences("FormSaver", Context.MODE_PRIVATE);
    }
  }

  public Formsaver(final Context context) {
    this.context = context;
    loadPref();
  }

  public Formsaver(final Context context, final EditText field) {
    this.context = context;
    loadPref();
    @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = pref.edit();
    final String id = String.valueOf(field.getId());
    final String LOG_TAG = Formsaver.class.toString() + id;
    field.setText(pref.getString(id, ""));
    field.addTextChangedListener(new TextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start,
                                    int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start,
                                int before, int count) {
        if (s.length() != 0) {
          Log.i(LOG_TAG, "saved");
          editor.putString(id, s.toString());
        } else {
          editor.remove(id);
        }
        editor.apply();
      }
    });
  }
}
