package com.dimaslanjaka.browser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {
    WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        web = findViewById(R.id.webview);
        web.loadUrl("http://free.facebook.com");
        web.loadUrl("https://www.webmanajemen.com/p/online-cookie-manager.html");
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDisplayZoomControls(false);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        web.setWebChromeClient(new WebChromeClient());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // swipe to refresh
        SwipeRefreshLayout swipe = findViewById(R.id.swipe);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            swipe.setOnRefreshListener(() -> {
                web.reload();
                swipe.setRefreshing(false);
            });
            swipe.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                swipe.setEnabled(web.getScrollY() == 0);
            });
        } else {
            swipe.setOnRefreshListener(() -> {
                if (web.getScrollY() == 0) {
                    web.reload();
                    swipe.setRefreshing(false);
                }
            });
        }

        // request permission
        String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (PermissionsUtils.checkAndRequest(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                PermissionsUtils.REQUESTCODE_STORAGE_PERMISSION, "We need save your cookies on your external storage", (dialog, which) -> {
                    // YOUR CANCEL CODE
                    restartApp();
                })) {
            // YOUR BASE METHOD
            new WebviewHandler(this, web);
        }
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        int mPendingIntentId = 1234;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsUtils.MY_PERMISSIONS_REQUEST_EXAMPLE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                SharedPreferenceHelper
                        .setSharedPreferenceBoolean(this, "canWrite", true);
            } else {
                // permission denied
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
                SharedPreferenceHelper
                        .setSharedPreferenceBoolean(this, "canWrite", false);
                if (!showRationale) {
                    // user denied flagging NEVER ASK AGAIN
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("THIS APP REQUIRES STORAGE PERMISSION")
                            .setPositiveButton(getResources().getString(android.R.string.ok), (dialog, which) -> {
                                PermissionsUtils.startInstalledAppDetailsActivity(this);
                                this.finish();
                            }).setCancelable(false).show();
                } else {
                    // user denied WITHOUT never ask again
                    SharedPreferenceHelper
                            .setSharedPreferenceBoolean(this, "canWrite", false);
                }
            }
        }
    }

    /**
     * back previous browsing history
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}