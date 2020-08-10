package com.dimaslanjaka.tools.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dimaslanjaka.tools.MainActivity;
import com.dimaslanjaka.tools.R;
import com.dimaslanjaka.tools.helper.SharedPref;

public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String ids = null;
    private SharedPref pref;
    private SettingsViewModel settingsViewModel;
    private EditText appid, banner1, banner2, banner3, banner4, banner5, banner6, banner7, banner8,
            banner9, banner10, banner11, banner12, banner13, banner14, banner15, interstitial, keywords, rewarded;
    private String mParam1, mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.pref = new SharedPref(getContext());
        ids = pref.id;
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        /*final TextView textView = root.findViewById(R.id.text_slideshow);
        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        this.appid = (EditText) root.findViewById(R.id.settings_app_id);
        this.banner1 = (EditText) root.findViewById(R.id.settings_banner1);
        this.banner2 = (EditText) root.findViewById(R.id.settings_banner2);
        this.banner3 = (EditText) root.findViewById(R.id.settings_banner3);
        this.banner4 = (EditText) root.findViewById(R.id.settings_banner4);
        this.banner5 = (EditText) root.findViewById(R.id.settings_banner5);
        this.banner6 = (EditText) root.findViewById(R.id.settings_banner6);
        this.banner7 = (EditText) root.findViewById(R.id.settings_banner7);
        this.banner8 = (EditText) root.findViewById(R.id.settings_banner8);
        this.banner9 = (EditText) root.findViewById(R.id.settings_banner9);
        this.banner10 = (EditText) root.findViewById(R.id.settings_banner10);
        this.banner11 = (EditText) root.findViewById(R.id.settings_banner11);
        this.banner12 = (EditText) root.findViewById(R.id.settings_banner12);
        this.banner13 = (EditText) root.findViewById(R.id.settings_banner13);
        this.banner14 = (EditText) root.findViewById(R.id.settings_banner14);
        this.banner15 = (EditText) root.findViewById(R.id.settings_banner15);
        this.interstitial = (EditText) root.findViewById(R.id.settings_interstitial);
        this.rewarded = (EditText) root.findViewById(R.id.settings_rewarded);
        this.keywords = (EditText) root.findViewById(R.id.settings_keywords);
        formDisable();
        loadAdId();
        //setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle();
        //return inflater.inflate(R.layout.fragment_settingsq, container, false);

        Switch nightMode = root.findViewById(R.id.nightMode);
        nightMode.setChecked(this.pref.getPref().getBoolean("nightMode", false));
        final SharedPreferences prefinal = this.pref.getPref();
        nightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefinal.edit().putBoolean("nightMode", isChecked).apply();
            }
        });

        return root;
    }

    //@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        //public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.toolbar_settings, menu);
        inflater.inflate(R.menu.toolbar_settings, menu);
        return;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_cancel) {
            formDisable();
            startActivity(new Intent(getActivity(), MainActivity.class));
            return true;
        } else if (itemId == R.id.action_edit) {
            formEnable();
            return true;
        } else if (itemId != R.id.action_save) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            updateAd();
            formDisable();
            Toast.makeText(getActivity().getApplicationContext(), "Configuration saved", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public void formEnable() {
        this.appid.setEnabled(true);
        this.banner1.setEnabled(true);
        this.banner2.setEnabled(true);
        this.banner3.setEnabled(true);
        this.banner4.setEnabled(true);
        this.banner5.setEnabled(true);
        this.banner6.setEnabled(true);
        this.banner7.setEnabled(true);
        this.banner8.setEnabled(true);
        this.banner9.setEnabled(true);
        this.banner10.setEnabled(true);
        this.banner11.setEnabled(true);
        this.banner12.setEnabled(true);
        this.banner13.setEnabled(true);
        this.banner14.setEnabled(true);
        this.banner15.setEnabled(true);
        this.interstitial.setEnabled(true);
        this.rewarded.setEnabled(true);
        this.keywords.setEnabled(true);
    }

    public void formDisable() {
        this.appid.setEnabled(false);
        this.banner1.setEnabled(false);
        this.banner2.setEnabled(false);
        this.banner3.setEnabled(false);
        this.banner4.setEnabled(false);
        this.banner5.setEnabled(false);
        this.banner6.setEnabled(false);
        this.banner7.setEnabled(false);
        this.banner8.setEnabled(false);
        this.banner9.setEnabled(false);
        this.banner10.setEnabled(false);
        this.banner11.setEnabled(false);
        this.banner12.setEnabled(false);
        this.banner13.setEnabled(false);
        this.banner14.setEnabled(false);
        this.banner15.setEnabled(false);
        this.interstitial.setEnabled(false);
        this.rewarded.setEnabled(false);
        this.keywords.setEnabled(false);
    }

    public void loadAdId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(ids, 0);
        this.appid.setText(sharedPreferences.getString("appid", getString(R.string.admob_app_id)));
        this.banner1.setText(sharedPreferences.getString("banner1", getString(R.string.test_banner)));
        this.banner2.setText(sharedPreferences.getString("banner2", ""));
        this.banner3.setText(sharedPreferences.getString("banner3", ""));
        this.banner4.setText(sharedPreferences.getString("banner4", ""));
        this.banner5.setText(sharedPreferences.getString("banner5", ""));
        this.banner6.setText(sharedPreferences.getString("banner6", ""));
        this.banner7.setText(sharedPreferences.getString("banner7", ""));
        this.banner8.setText(sharedPreferences.getString("banner8", ""));
        this.banner9.setText(sharedPreferences.getString("banner9", ""));
        this.banner10.setText(sharedPreferences.getString("banner10", ""));
        this.banner11.setText(sharedPreferences.getString("banner11", ""));
        this.banner12.setText(sharedPreferences.getString("banner12", ""));
        this.banner13.setText(sharedPreferences.getString("banner13", ""));
        this.banner14.setText(sharedPreferences.getString("banner14", ""));
        this.banner15.setText(sharedPreferences.getString("banner15", ""));
        this.interstitial.setText(sharedPreferences.getString("interstitial", getString(R.string.test_inter)));
        this.rewarded.setText(sharedPreferences.getString("rewarded", getString(R.string.test_reward)));
        this.keywords.setText(sharedPreferences.getString("keywords", "Loan"));
    }

    public void updateAd() {
        SharedPreferences.Editor edit = getActivity().getSharedPreferences(ids, 0).edit();
        edit.putString("appid", this.appid.getText().toString().trim());
        edit.putString("banner1", this.banner1.getText().toString().trim());
        edit.putString("banner2", this.banner2.getText().toString().trim());
        edit.putString("banner3", this.banner3.getText().toString().trim());
        edit.putString("banner4", this.banner4.getText().toString().trim());
        edit.putString("banner5", this.banner5.getText().toString().trim());
        edit.putString("banner6", this.banner6.getText().toString().trim());
        edit.putString("banner7", this.banner7.getText().toString().trim());
        edit.putString("banner8", this.banner8.getText().toString().trim());
        edit.putString("banner9", this.banner9.getText().toString().trim());
        edit.putString("banner10", this.banner10.getText().toString().trim());
        edit.putString("banner11", this.banner11.getText().toString().trim());
        edit.putString("banner12", this.banner12.getText().toString().trim());
        edit.putString("banner13", this.banner13.getText().toString().trim());
        edit.putString("banner14", this.banner14.getText().toString().trim());
        edit.putString("banner15", this.banner15.getText().toString().trim());
        edit.putString("interstitial", this.interstitial.getText().toString().trim());
        edit.putString("rewarded", this.rewarded.getText().toString().trim());
        edit.putString("keywords", this.keywords.getText().toString().trim());
        edit.commit();
        MainActivity.enableFab();
    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
