package com.dimaslanjaka.tools.fragment.about;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.dimaslanjaka.tools.Helpers.core.RootUtil;
import com.dimaslanjaka.tools.Helpers.core.Storage;
import com.dimaslanjaka.tools.R;

public class About extends Fragment {

	@SuppressLint("SetTextI18n")
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View INF = inflater.inflate(R.layout.fragment_about, container, false);
		TextView ISROOT_view = INF.findViewById(R.id.isroot);
		boolean ISROOT = RootUtil.isDeviceRooted();
		ISROOT_view.setText(ISROOT ? "true" : "false");
		if (ISROOT) {
			ISROOT_view.setBackgroundColor(Color.parseColor("#00ff00"));
		}
		TextView cache = INF.findViewById(R.id.cacheSize);
		cache.setText(Storage.initializeCache(requireContext()));
		Button updatebtn = INF.findViewById(R.id.btn_update);
		updatebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				About.this.launchURL("https://github.com/dimaslanjaka/Android/tree/master/app/release");
			}
		});
		return INF;
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager =
						(ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private void launchURL(final String url) {
		CustomTabsIntent.Builder builderCustomTabs = new CustomTabsIntent.Builder();
		CustomTabsIntent intentCustomTabs = builderCustomTabs.build();
		intentCustomTabs.intent.setPackage("com.android.chrome");
		intentCustomTabs.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentCustomTabs.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intentCustomTabs.launchUrl(requireActivity().getApplicationContext(), Uri.parse(url));
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		AboutViewModel mViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
	}


}