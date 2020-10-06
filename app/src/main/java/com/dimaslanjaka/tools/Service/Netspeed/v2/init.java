package com.dimaslanjaka.tools.Service.Netspeed.v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import com.dimaslanjaka.tools.Global;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;
import com.dimaslanjaka.tools.Service.Notification.NotificationHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @see "https://stackoverflow.com/questions/53393180/how-to-detect-upload-download-transfer
 * -rate-in-android"
 */
public class init {
  private static final String TAG = init.class.getName();
  TrafficSpeedMeasurer mTrafficSpeedMeasurer;
  Context context;
  Handler handler = new Handler(Looper.getMainLooper());
  Database dbh;
  private boolean initialized = false;

  private ITrafficSpeedListener mStreamSpeedListener = new ITrafficSpeedListener() {
    @Override
    public void onTrafficSpeedMeasured(final double upStream, final double downStream) {
      Runnable runner = new Runnable() {
        @Override
        public void run() {
          double upStreamtruncatedDouble = BigDecimal.valueOf(upStream)
                  .setScale(2, RoundingMode.HALF_UP)
                  .doubleValue();
          double downStreamtruncatedDouble = BigDecimal.valueOf(downStream)
                  .setScale(2, RoundingMode.HALF_UP)
                  .doubleValue();
          String upStreamSpeed = Utils.parseSpeed(upStreamtruncatedDouble, false);
          String downStreamSpeed = Utils.parseSpeed(downStreamtruncatedDouble, false);
          //Database.sync(upStreamtruncatedDouble, downStreamtruncatedDouble);
          if (!initialized) {
            new SharedPref(Global.getContext());
            new Database(Global.getContext());
            initialized = true;
          }
          String today = Database.parseDateToday();
          SharedPreferences pref = Objects.requireNonNull(SharedPref.getPref());
          long upload = BigDecimal.valueOf(upStreamtruncatedDouble).longValue();
          long download = BigDecimal.valueOf(downStreamtruncatedDouble).longValue();

          String type = Database.getNetworkType();
          String typeKey = today + "-" + type;
          String uploadKey = today + "-upload";
          String downloadKey = today + "-download";

          long todayUpload = pref.getLong(uploadKey, 0);
          todayUpload += upload;
          pref.edit().putLong(uploadKey, todayUpload).apply();

          long todayDownload = pref.getLong(downloadKey, 0);
          todayDownload += download;
          pref.edit().putLong(downloadKey, todayDownload).apply();

          long typeTotal = pref.getLong(typeKey, 0);
          typeTotal += upload;
          typeTotal += download;
          pref.edit().putLong(typeKey, typeTotal).apply();

          Database.sync();

          //Show notification
          NotificationHandler.setNetworkSpeed(upStreamSpeed, downStreamSpeed).show();
        }
      };

      handler.post(runner);
    }
  };

  public init(Context applicationContext) {
    context = applicationContext;
    dbh = new Database(applicationContext);
  }

  public void register() {
    mTrafficSpeedMeasurer.registerListener(mStreamSpeedListener);
  }

  public void unregister() {
    mTrafficSpeedMeasurer.removeListener(mStreamSpeedListener);
  }

  public void stop() {
    mTrafficSpeedMeasurer.stopMeasuring();
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  public void start() {
    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    boolean isMobile = Objects.requireNonNull(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE))
            .isConnectedOrConnecting();
    mTrafficSpeedMeasurer = new TrafficSpeedMeasurer(
            isMobile ? TrafficSpeedMeasurer.TrafficType.MOBILE : TrafficSpeedMeasurer.TrafficType.ALL
    );
    mTrafficSpeedMeasurer.startMeasuring();
  }
}
