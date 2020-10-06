package com.dimaslanjaka.tools.Helpers.firebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SharedPref implements SharedPreferences {
  /**
   * The placeholder in {@link #sPathPattern} for the preferences' names
   */
  public static final String NAME_PLACEHOLDER = "$name";
  /**
   * The placeholder in {@link #sPathPattern} for the user's id
   */
  public static final String UID_PLACEHOLDER = "$uid";
  public static SharedPreferences prefs = null;
  public static String id = null;
  @SuppressLint("StaticFieldLeak")
  public static Context c = null;
  /**
   * The pattern for the paths to the roots of the preferences
   */
  private static String sPathPattern = String.format(Locale.ENGLISH, "/shared_prefs/%s/%s", UID_PLACEHOLDER, NAME_PLACEHOLDER);
  Map<String, Datamodel> datamodelHashMap = new HashMap<>();
  private SharedPreferences mCache;
  private DatabaseReference mRoot;

  public SharedPref(Context context) {
    if (c == null) c = context;
    if (id == null) id = getDeviceId(context);
    prefs = context.getSharedPreferences(id, Context.MODE_PRIVATE);
  }

  public SharedPref(DatabaseReference mRoot) {
    this.mRoot = mRoot;
  }

  public SharedPref(SharedPreferences mCache) {
    this.mCache = mCache;
  }

  /**
   * Creates a new instance
   *
   * @param cache the wrapped {@link SharedPreferences}
   * @param root  the {@link DatabaseReference} used for storing
   */
  protected SharedPref(SharedPreferences cache, DatabaseReference root) {
    mCache = cache;
    mRoot = root;
    //mSyncAdapter = new SharedFirebasePreferences.SyncAdapter(this);
  }

  /**
   * Save List<T> to SharedPreferences
   *
   * @param key    key prefs
   * @param list   List<T> item
   * @param unique unique or not
   * @param <T>    any
   */
  public static <T> void setList(String key, List<T> list, boolean unique) {
    Gson gson = new Gson();
    if (unique) {
      HashSet<T> hs = new HashSet<>(list);
      list.clear();
      list.addAll(hs);
    }
    String json = gson.toJson(list);
    saveList(key, json);
  }

  public static <T> void setList(String key, List<T> list) {
    setList(key, list, false);
  }

  private static void saveList(String key, String value) {
    SharedPreferences appSharedPrefs = SharedPref.getPref();
    assert appSharedPrefs != null;
    appSharedPrefs.edit().putString(key, value).apply();
  }

  public static List<String> getList(String key) {
    List<String> arrayItems = new ArrayList<>();
    SharedPreferences appSharedPrefs = SharedPref.getPref();
    assert appSharedPrefs != null;
    String serializedObject = appSharedPrefs.getString(key, null);
    if (serializedObject != null) {
      Gson gson = new Gson();
      Type type = new TypeToken<List<String>>() {
      }.getType();
      arrayItems = gson.fromJson(serializedObject, type);
    }
    return arrayItems;
  }

  /**
   * Reset shared preferences
   */
  public static void reset(){
    Objects.requireNonNull(SharedPref.getPref()).edit().clear().apply();
  }

  public static SharedPreferences getPref() {
    if (prefs != null) {
      return prefs;
    }
    if (id != null && c != null) {
      return c.getSharedPreferences(id, Context.MODE_PRIVATE);
    }
    return null;
  }

  /**
   * storing object in preferences
   *
   * @param modal Object JSONObject
   * @param key   key preferences
   */
  static public void setxObject(Object modal, String key) {
    SharedPreferences appSharedPrefs = SharedPref.getPref();
    assert appSharedPrefs != null;
    Editor prefsEditor = appSharedPrefs.edit();

    Gson gson = new Gson();
    String jsonObject = gson.toJson(modal);
    prefsEditor.putString(key, jsonObject);
    prefsEditor.apply();
  }

  /**
   * get object from preferences
   *
   * @param key          key preferences
   * @param classAdapter class adapter that will process the json object
   * @return Object
   */
  static public Object getxObject(String key, Class<?> classAdapter) {
    SharedPreferences appSharedPrefs = SharedPref.getPref();
    assert appSharedPrefs != null;
    String json = appSharedPrefs.getString(key, "");
    Gson gson = new Gson();
    return gson.fromJson(json, classAdapter);
  }

  /**
   * Returns the default instance from {@link PreferenceManager}
   *
   * @param con a {@link Context}
   * @return the instance
   * @see Context#MODE_PRIVATE
   */
  public synchronized static SharedPref getDefaultInstance(Context con) {
    return (SharedPref) PreferenceManager.getDefaultSharedPreferences(new SharedFirebasePreferencesContextWrapper(con));
  }

  public String getDeviceId(Context context) {
    return Settings.Secure.getString(
            context.getContentResolver(),
            Settings.Secure.ANDROID_ID
    );
  }

  @Override
  public Map<String, ?> getAll() {
    return null;
  }

  @Nullable
  @Override
  public String getString(String s, @Nullable String s1) {
    return prefs.getString(s, s1);
  }

  @Nullable
  @Override
  public Set<String> getStringSet(String s, @Nullable Set<String> set) {
    return prefs.getStringSet(s, set);
  }

  @Override
  public int getInt(String s, int i) {
    return prefs.getInt(s, i);
  }

  @Override
  public long getLong(String s, long l) {
    return prefs.getLong(s, l);
  }

  @Override
  public float getFloat(String s, float v) {
    return prefs.getFloat(s, v);
  }

  @Override
  public boolean getBoolean(String s, boolean b) {
    return prefs.getBoolean(s, b);
  }

  @Override
  public boolean contains(String s) {
    return prefs.contains(s);
  }

  @Override
  public Editor edit() {
    return null;
  }

  @Override
  public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

  }

  @Override
  public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {

  }

  public static class json {
    public static void save(String key, JSONObject json) {
      SharedPreferences appSharedPrefs = SharedPref.getPref();
      assert appSharedPrefs != null;
      appSharedPrefs.edit().putString(key, json.toString()).apply();
    }

    public static JSONObject load(String key) throws JSONException {
      SharedPreferences appSharedPrefs = SharedPref.getPref();
      assert appSharedPrefs != null;
      String jsonStr = appSharedPrefs.getString(key, null);
      if (jsonStr == null) {
        return new JSONObject();
      }

      return new JSONObject(jsonStr);
    }

    public static Object getJSONObjectByKey(String SharedPrefKey, String JSONKey) throws JSONException {
      JSONObject JSON = json.load(SharedPrefKey);
      if (JSON.has(JSONKey)) {
        return JSON.get(JSONKey);
      }
      return null;
    }
  }

  public static void putDouble(final String key, final double value) {
    SharedPref.getPref().edit().putLong(key, Double.doubleToRawLongBits(value)).apply();
  }

  public static double getDouble(final String key, final double defaultValue) {
    if (!SharedPref.getPref().contains(key)) {
      return defaultValue;
    }

    return Double.longBitsToDouble(SharedPref.getPref().getLong(key, 0));
  }
}