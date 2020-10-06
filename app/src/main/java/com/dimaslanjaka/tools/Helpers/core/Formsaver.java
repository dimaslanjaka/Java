package com.dimaslanjaka.tools.Helpers.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import com.dimaslanjaka.tools.Global;

public class Formsaver {
	/**
	 * {@link SharedPreferences}
	 */
	public SharedPreferences pref = null;
	private Context context = null;

	public Formsaver(final Context context) {
		this.context = context;
		loadPref();
	}

	public Formsaver() {
		this.context = Global.getContext();
		loadPref();
	}

	public Formsaver(final Context context, final EditText field) {
		this.context = context;
		loadPref();
		final SharedPreferences.Editor editor = pref.edit();
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

	public Formsaver(EditText editText) {
		new Formsaver(Global.getContext(), editText);
	}

	private void loadPref() {
		if (pref == null)
			pref = Global.getContext().getSharedPreferences("FormSaver", Context.MODE_PRIVATE);
	}
}
