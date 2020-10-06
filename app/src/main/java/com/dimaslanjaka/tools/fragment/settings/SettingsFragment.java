package com.dimaslanjaka.tools.fragment.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.dimaslanjaka.tools.Helpers.firebase.SharedFirebasePreferences;
import com.dimaslanjaka.tools.Helpers.firebase.SharedFirebasePreferencesContextWrapper;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;
import com.dimaslanjaka.tools.MainActivity;
import com.dimaslanjaka.tools.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SettingsFragment extends Fragment
				implements FirebaseAuth.AuthStateListener,
				SharedPreferences.OnSharedPreferenceChangeListener {

	private final String TAG = this.getClass().getName();
	private EditText appid, banner1, banner2, banner3, banner4, banner5, banner6, banner7, banner8,
					banner9, banner10, banner11, banner12, banner13, banner14, banner15, interstitial, keywords, rewarded;
	private SharedFirebasePreferences mPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		new SharedPref(requireContext());

		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true); //Make sure you have this line of code.

		//initialize firebase
		FirebaseApp.initializeApp(requireContext());
		FirebaseAuth.getInstance().addAuthStateListener(this);
	}

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_settings, container, false);

		this.appid = root.findViewById(R.id.settings_app_id);
		this.banner1 = root.findViewById(R.id.settings_banner1);
		this.banner2 = root.findViewById(R.id.settings_banner2);
		this.banner3 = root.findViewById(R.id.settings_banner3);
		this.banner4 = root.findViewById(R.id.settings_banner4);
		this.banner5 = root.findViewById(R.id.settings_banner5);
		this.banner6 = root.findViewById(R.id.settings_banner6);
		this.banner7 = root.findViewById(R.id.settings_banner7);
		this.banner8 = root.findViewById(R.id.settings_banner8);
		this.banner9 = root.findViewById(R.id.settings_banner9);
		this.banner10 = root.findViewById(R.id.settings_banner10);
		this.banner11 = root.findViewById(R.id.settings_banner11);
		this.banner12 = root.findViewById(R.id.settings_banner12);
		this.banner13 = root.findViewById(R.id.settings_banner13);
		this.banner14 = root.findViewById(R.id.settings_banner14);
		this.banner15 = root.findViewById(R.id.settings_banner15);
		this.interstitial = root.findViewById(R.id.settings_interstitial);
		this.rewarded = root.findViewById(R.id.settings_rewarded);
		this.keywords = root.findViewById(R.id.settings_keywords);
		formDisable();
		loadAdId();
		//setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
		//((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle();
		//return inflater.inflate(R.layout.fragment_settingsq, container, false);

		SwitchMaterial nightMode = root.findViewById(R.id.nightMode);
		nightMode.setChecked(Objects.requireNonNull(SharedPref.getPref()).getBoolean("nightMode", false));
		final SharedPreferences prefinal = SharedPref.getPref();
		nightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				prefinal.edit().putBoolean("nightMode", isChecked).apply();
			}
		});

		return root;
	}

	//@Override
	public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
		// TODO Add your menu entries here
		super.onCreateOptionsMenu(menu, inflater);
		//public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.toolbar_settings, menu);
		inflater.inflate(R.menu.toolbar_settings, menu);
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
			Toast.makeText(requireActivity().getApplicationContext(), "Configuration saved", Toast.LENGTH_SHORT).show();
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
		SharedPreferences sharedPreferences = SharedPref.getPref();
		assert sharedPreferences != null;
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
		SharedPreferences sharedPreferences = SharedPref.getPref();
		assert sharedPreferences != null;
		SharedPreferences.Editor edit = sharedPreferences.edit();
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
		edit.apply();
	}

	public void onBackPressed() {
		Intent a = new Intent(Intent.ACTION_MAIN);
		a.addCategory(Intent.CATEGORY_HOME);
		a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(a);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

	}

	/**
	 * Called when a fragment is first attached to its context.
	 * {@link #onCreate(Bundle)} will be called after this.
	 *
	 * @param context context
	 */
	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(new SharedFirebasePreferencesContextWrapper(context));
	}

	@Override
	public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
		if (firebaseAuth.getCurrentUser() != null) {
			mPreferences = SharedFirebasePreferences.getDefaultInstance(requireContext());
			mPreferences.keepSynced(true);
			mPreferences.registerOnSharedPreferenceChangeListener(this);
			mPreferences.pull().addOnPullCompleteListener(new SharedFirebasePreferences.OnPullCompleteListener() {
				@Override
				public void onPullSucceeded(SharedFirebasePreferences preferences) {
					//showView();
					Toast.makeText(requireContext(), "Fetch success", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onPullFailed(Exception e) {
					//showView();
					Toast.makeText(requireContext(), "Fetch failed", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mPreferences != null) {
			mPreferences.keepSynced(true);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mPreferences != null) {
			mPreferences.keepSynced(false);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mPreferences != null) {
			mPreferences.unregisterOnSharedPreferenceChangeListener(this);
		}
	}
}
