package com.dimaslanjaka.tools.Helpers.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json extends JSONObject {

	public Json(String jsonStr) throws JSONException {
		super(jsonStr.trim());
	}

	public String str(String keyname) throws JSONException {
		if (this.has(keyname)) {
			return this.getString(keyname);
		}
		return null;
	}

	public static class is {
		public static boolean valid(String test) {
			try {
				new JSONObject(test);
			} catch (JSONException ex) {
				// edited, to include @Arthur's comment
				// e.g. in case JSONArray is valid as well...
				try {
					new JSONArray(test);
				} catch (JSONException ex1) {
					return false;
				}
			}
			return true;
		}
	}
}
