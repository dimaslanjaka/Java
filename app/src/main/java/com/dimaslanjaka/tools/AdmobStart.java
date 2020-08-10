package com.dimaslanjaka.tools;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dimaslanjaka.tools.helper.SharedPref;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class AdmobStart extends AppCompatActivity implements RewardedVideoAdListener {
    private AdRequest adRequest;
    private AdView banner1, banner2, banner3, banner4, banner5, banner6, banner7, banner8,
            banner9, banner10, banner11, banner12, banner13, banner14, banner15;
    private RewardedVideoAd rewardedVideoAd;
    private TextView bannerCounter, rewardedCounter, interCounter;
    private int[] bannerTotal;
    private Editor dataEditor;
    private SharedPreferences dataPref;
    private SharedPref pref;
    private int[] interTotal;
    private InterstitialAd interstitialAd;
    private int[] rewardedTotal;
    private Button startBtnInfo, startBtnRewarded, startBtnRefresh, startBtnInter;
    private Switch startAutoMode;
    private EditText refreshEditTime;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;


    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.pref = new SharedPref(getApplicationContext());
        this.dataPref = getApplicationContext().getSharedPreferences(this.pref.id, 0);
        this.dataEditor = this.dataPref.edit();
        this.bannerTotal = new int[]{Integer.valueOf(this.dataPref.getString("bannertotal", "0"))};
        this.interTotal = new int[]{Integer.valueOf(this.dataPref.getString("intertotal", "0"))};
        this.rewardedTotal = new int[]{Integer.valueOf(this.dataPref.getString("rewardedtotal", "0"))};
        if (this.dataPref.getString("appid", "").toString().isEmpty()) {
            setContentView(R.layout.admob_disable);
        } else {
            setContentView(R.layout.activity_start);

            this.bannerCounter = findViewById(R.id.start_banner_counter);
            this.interCounter = findViewById(R.id.start_inter_counter);
            this.rewardedCounter = findViewById(R.id.start_rewarded_counter);
            this.startBtnRefresh = findViewById(R.id.start_btn_refresh);

            this.startBtnInter = findViewById(R.id.start_btn_interstitial);
            this.startBtnRewarded = findViewById(R.id.start_btn_rewarded);
            this.startBtnInfo = findViewById(R.id.start_btn_info);
            this.startAutoMode = findViewById(R.id.auto_mode);
            this.refreshEditTime = findViewById(R.id.refresh_time);

            //this.rewardedVideoAd.destroy(this);
            int refreshTime = this.dataPref.getInt("refreshTime", 10000);
            if (refreshTime / 1000 < 30){
                refreshTime = 60000;
                AdmobStart.this.dataPref.edit().putInt("refreshTime", refreshTime).apply();
                Toast.makeText(
                        getApplicationContext(),
                        "Your refresh time under 30 seconds, is violating admob rules. using default refreshtime (60 seconds)",
                        Toast.LENGTH_LONG
                ).show();
            }
            this.refreshEditTime.setText(String.valueOf(refreshTime / 1000));
            this.refreshEditTime.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int val = Integer.valueOf(s.toString()) * 1000;
                    AdmobStart.this.dataPref.edit().putInt("refreshTime", val).apply();
                    Toast.makeText(getApplicationContext(), "Refresh active in " + (val / 1000) + " secs", Toast.LENGTH_SHORT).show();
                }
            });
            Toast.makeText(this, "Refresh active in " + (refreshTime / 1000) + " secs", Toast.LENGTH_SHORT).show();
            this.startAutoMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AdmobStart.this.dataPref.edit().putBoolean("autoMode", isChecked).apply();
                }
            });
            boolean AuTo = this.dataPref.getBoolean("autoMode", false);
            this.startAutoMode.setChecked(AuTo);
            this.startBtnRefresh.setEnabled(false);
            CountDownTimer r2 = new CountDownTimer(refreshTime, 1000) {
                public void onTick(long j) {
                    Button access$000 = AdmobStart.this.startBtnRefresh;
                    String sb = "REFRESH (" + (j / 1000) + ")";
                    access$000.setText(sb);
                }

                public void onFinish() {
                    if (AdmobStart.this.dataPref.getBoolean("autoMode", false)) {
                        restartActivity();
                    } else {
                        AdmobStart.this.startBtnRefresh.setEnabled(true);
                        AdmobStart.this.startBtnRefresh.setText("REFRESH");
                    }
                }
            };
            r2.start();


            loadBuilder();
            startLoadAd();
            //setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void restartActivity() {
        AdmobStart.this.finish();
        AdmobStart.this.startActivity(AdmobStart.this.getIntent());
        return;
    }


    private void btnRefresh() {
        this.startBtnRefresh.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AdmobStart.this.interstitialAd.isLoaded()) {
                    AdmobStart.this.interstitialAd.show();
                    AdmobStart.this.interstitialAd.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            super.onAdClosed();
                            restartActivity();
                        }
                    });
                    return;
                }
                restartActivity();
            }
        });
    }

    private void btnInter() {
        this.startBtnInter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AdmobStart.this.interstitialAd.isLoaded()) {
                    if (AdmobStart.this.interstitialAd.isLoaded()) {
                        AdmobStart.this.interstitialAd.show();
                        AdmobStart.this.interstitialAd.setAdListener(new AdListener() {
                            public void onAdClosed() {
                                super.onAdClosed();
                                AdmobStart.this.interstitialAd.loadAd(AdmobStart.this.adRequest);
                            }
                        });
                    }
                    AdmobStart.this.interstitialAd.show();
                    AdmobStart.this.interstitialAd.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            super.onAdClosed();
                            AdmobStart.this.interstitialAd.loadAd(AdmobStart.this.adRequest);
                            int[] access$500 = AdmobStart.this.interTotal;
                            access$500[0] = access$500[0] + 1;
                            AdmobStart.this.dataEditor.putString("intertotal", Integer.toString(AdmobStart.this.interTotal[0]));
                            AdmobStart.this.dataEditor.commit();
                            TextView interstitialCounter = AdmobStart.this.interCounter;
                            String sb = "Interstitial = " +
                                    AdmobStart.this.dataPref.getString("intertotal", "");
                            interstitialCounter.setText(sb);
                        }
                    });
                    return;
                }
                //this.rewardedVideoAd.destroy(this);
                Toast.makeText(AdmobStart.this, "Interstitial ads not yet loaded!", Toast.LENGTH_SHORT).show();
                AdmobStart.this.interstitialAd.loadAd(new Builder().build());
            }
        });
    }

    private void btnReward() {
        this.startBtnRewarded.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AdmobStart.this.rewardedVideoAd.isLoaded()) {
                    AdmobStart.this.rewardedVideoAd.show();
                    return;
                }
                //this.rewardedVideoAd.destroy(this);
                Toast.makeText(AdmobStart.this, "Rewarded ads not yet loaded!", Toast.LENGTH_SHORT).show();
                AdmobStart.this.loadRewardedVideoAd();
            }
        });
    }

    private void btnInfo() {
        this.startBtnInfo.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AdmobStart.this.info();
            }
        });
    }

    private void startLoadAd() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage("Loading your ads, please wait...");
        progressDialog.setProgressStyle(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
        this.progressDialog = progressDialog;
        this.linearLayout = (LinearLayout) findViewById(R.id.start_ad_layout);
        startCounter();
        btnRefresh();
        //MobileAds.initialize(this, this.dataPref.getString("appid", ""));
        btnInter();
        //MobileAds.initialize(this, this.dataPref.getString("appid", ""));
        btnReward();
        btnInfo();
        MobileAds.initialize(this, this.dataPref.getString("appid", ""));
        runBanner(this.banner1, getApplicationContext(), "banner1");
        runBanner(this.banner2, getApplicationContext(), "banner2");
        runBanner(this.banner3, getApplicationContext(), "banner3");
        runBanner(this.banner4, getApplicationContext(), "banner4");
        runBanner(this.banner5, getApplicationContext(), "banner5");
        runBanner(this.banner6, getApplicationContext(), "banner6");
        runBanner(this.banner7, getApplicationContext(), "banner7");
        runBanner(this.banner8, getApplicationContext(), "banner8");
        runBanner(this.banner9, getApplicationContext(), "banner9");
        runBanner(this.banner10, getApplicationContext(), "banner10");
        runBanner(this.banner11, getApplicationContext(), "banner11");
        runBanner(this.banner12, getApplicationContext(), "banner12");
        runBanner(this.banner13, getApplicationContext(), "banner13");
        runBanner(this.banner14, getApplicationContext(), "banner14");
        runBanner(this.banner15, getApplicationContext(), "banner15");
        boolean interRun = runInter();
        if (interRun == false) {
            this.startBtnInter.setClickable(false);
        }
        boolean rewardRun = runReward();
        if (rewardRun == false) {
            this.startBtnRewarded.setClickable(false);
        }
    }

    /**
     * Run Banner Init
     *
     * @param ad      Container AdView
     * @param context
     * @param config
     */
    private void runBanner(AdView ad, Context context, String config) {
        final String bannerId = this.dataPref.getString(config, "");
        if (bannerId.isEmpty()) {
            return;
        }
        ad = new AdView(context);
        ad.setAdSize(AdSize.BANNER);
        ad.setAdUnitId(bannerId);
        ad.loadAd(this.adRequest);
        final AdView ads = ad;
        final ProgressDialog progressDialog = this.progressDialog;
        final LinearLayout linearLayout = this.linearLayout;
        TextView text = new TextView(getApplicationContext());
        text.setPadding(5, 5, 5, 5);
        text.setText(bannerId);
        text.setTypeface(text.getTypeface(), Typeface.BOLD_ITALIC);
        final TextView tx = text;
        ad.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (linearLayout != null) {
                    linearLayout.removeView(tx);
                    linearLayout.removeView(ads);
                    linearLayout.addView(tx);
                    linearLayout.addView(ads);
                } else {
                    linearLayout.removeView(tx);
                    linearLayout.addView(ads);
                }
                countBanner();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                progressDialog.dismiss();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });
    }

    private void startCounter() {
        TextView textView = this.bannerCounter;
        String sb = "Banner = " +
                this.dataPref.getString("bannertotal", "");
        textView.setText(sb);
        TextView textView2 = this.interCounter;
        String sb2 = "Interstitial = " +
                this.dataPref.getString("intertotal", "");
        textView2.setText(sb2);
        TextView textView3 = this.rewardedCounter;
        String sb3 = "Rewarded = " +
                this.dataPref.getString("rewardedtotal", "");
        textView3.setText(sb3);
        @SuppressLint("SimpleDateFormat") String format = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
        String string = this.dataPref.getString("lastSavedDate", "");
        if (format.equals(string)) {
            this.dataEditor.putString("lastSavedDate", string);
            this.dataEditor.commit();
        }
        if (!format.equals(string)) {
            this.dataEditor.putString("lastSavedDate", format);
            this.bannerTotal[0] = 0;
            this.interTotal[0] = 0;
            this.rewardedTotal[0] = 0;
            this.dataEditor.putString("bannertotal", Integer.toString(this.bannerTotal[0]));
            this.dataEditor.putString("intertotal", Integer.toString(this.interTotal[0]));
            this.dataEditor.putString("rewardedtotal", Integer.toString(this.rewardedTotal[0]));
            this.dataEditor.commit();
            TextView textView4 = this.bannerCounter;
            String sb4 = "Banner = " +
                    this.dataPref.getString("bannertotal", "");
            textView4.setText(sb4);
            TextView textView5 = this.interCounter;
            String sb5 = "Interstitial = " +
                    this.dataPref.getString("intertotal", "");
            textView5.setText(sb5);
            TextView textView6 = this.rewardedCounter;
            String sb6 = "Rewarded = " +
                    this.dataPref.getString("rewardedtotal", "");
            textView6.setText(sb6);
        }
    }

    private void countBanner() {
        //int[] AdmobStart.this.bannerTotal = AdmobStart.this.bannerTotal;
        //AdmobStart.this.bannerTotal[0] = AdmobStart.this.bannerTotal[0] + 1;
        //AdmobStart.this.dataEditor.putString("bannertotal", Integer.toString(AdmobStart.this.bannerTotal[0]));
        AdmobStart.this.interstitialAd.loadAd(AdmobStart.this.adRequest);
        int[] access$500 = AdmobStart.this.bannerTotal;
        access$500[0] = access$500[0] + 1;
        AdmobStart.this.dataEditor.putString("bannertotal", Integer.toString(AdmobStart.this.bannerTotal[0]));
        AdmobStart.this.dataEditor.commit();
        TextView bannerCounter = AdmobStart.this.bannerCounter;
        String sb = "Banner = " +
                AdmobStart.this.dataPref.getString("bannertotal", "");
        bannerCounter.setText(sb);
        this.progressDialog.dismiss();
    }

    private boolean runInter() {
        this.interstitialAd = new InterstitialAd(this);
        String interId = this.dataPref.getString("interstitial", "").toString();
        if (interId.isEmpty()) {
            return false;
        }
        this.interstitialAd.setAdUnitId(interId);
        this.interstitialAd.loadAd(this.adRequest);
        return true;
    }

    private boolean runReward() {
        this.rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        this.rewardedVideoAd.setRewardedVideoAdListener(this);
        return loadRewardedVideoAd();
    }

    private void loadBuilder() {
        String kw = this.dataPref.getString("keywords", "loan").toString();
        if (kw.isEmpty()) {
            kw = "game";
        }
        if (BuildConfig.DEBUG) {
            this.adRequest = new Builder()
                    .addTestDevice("13D3C1E4AE5C28530B932DBD1BDAEA0F")
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("77C5686EF25BC56A920149DCFF595E48")
                    .addKeyword(kw)
                    .build();
        } else {
            this.adRequest = new Builder()
                    .addKeyword(kw)
                    .build();
        }
    }

    private boolean loadRewardedVideoAd() {
        String rewardId = this.dataPref.getString("rewarded", "").toString();
        if (rewardId.isEmpty()) {
            return false;
        }
        this.rewardedVideoAd.loadAd(rewardId, this.adRequest);
        return true;
    }

    private void info() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("INFO");
        String sb2 = "BANNER = " +
                this.dataPref.getString("bannertotal", "");
        String sb4 = "INTERSTITIAL = " +
                this.dataPref.getString("intertotal", "");
        String sb6 = "REWARDED = " +
                this.dataPref.getString("rewardedtotal", "");
        String sb7 = "Total counter today " +
                "\n\n" +
                sb2 +
                "\n" +
                sb4 +
                "\n" +
                sb6 +
                "\n\n" +
                "Every day counter reset to (0). ";
        builder.setMessage(sb7);
        builder.setPositiveButton("OKE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void onRewardedVideoAdClosed() {
        boolean loadReward = loadRewardedVideoAd();
        TextView rewardText = this.rewardedCounter;
        if (loadReward) {
            int[] iArr = this.rewardedTotal;
            iArr[0] = iArr[0] + 1;
            this.dataEditor.putString("rewardedtotal", Integer.toString(this.rewardedTotal[0]));
            this.dataEditor.commit();
            String sb = "Rewarded = " + this.dataPref.getString("rewardedtotal", "0");
            rewardText.setText(sb);
            rewardText.setVisibility(View.VISIBLE);
        } else {
            rewardText.setVisibility(View.GONE);
        }
    }

    public void onRewarded(RewardItem rewardItem) {
    }

    public void onRewardedVideoAdFailedToLoad(int i) {
    }

    public void onRewardedVideoAdLeftApplication() {
    }

    public void onRewardedVideoAdLoaded() {
    }

    public void onRewardedVideoAdOpened() {
    }

    public void onRewardedVideoCompleted() {
    }

    public void onRewardedVideoStarted() {
    }

    @Override
    public void onBackPressed() {
        AdmobStart.this.rewardedVideoAd.pause(this);
        AdmobStart.this.finish();
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        AdmobStart.this.rewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        AdmobStart.this.rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        AdmobStart.this.rewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    //@Override
    public boolean onUnbind(Intent intent) {
        Log.d(this.getClass().getName(), "UNBIND");
        return true;
    }
}
