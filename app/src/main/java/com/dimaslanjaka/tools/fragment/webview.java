package com.dimaslanjaka.tools.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.dimaslanjaka.tools.Facebook.WebView;
import com.dimaslanjaka.tools.R;

public class webview extends Fragment {
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.webview, container, false);

		String url = "https://www.google.com/";
		WebView view = rootView.findViewById(R.id.webView);
		view.getSettings().setJavaScriptEnabled(true);
		view.loadUrl(url);

		return rootView;
	}
}
