package com.dimaslanjaka.tools.Libs.Graphics.Images;

import android.content.Context;
import android.widget.ImageView;
import com.pixplicity.sharp.Sharp;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class SVG {
	private static OkHttpClient httpClient;

	public static void fetch(Context context, String url, final ImageView target) {
		if (httpClient == null) {
			// Use cache for performance and basic offline capability
			int cacheSize = 10 * 1024 * 1024; // 10MB
			httpClient = new OkHttpClient.Builder()
							.cache(new Cache(context.getCacheDir(), cacheSize))
							.build();
		}

		// force cache for basic offline capability
		Request request = new Request.Builder().url(url)
						/*.cacheControl(CacheControl.FORCE_CACHE)*/
						.build();
		httpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				//target.setImageDrawable(R.drawable.ic_close);
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				ResponseBody body = response.body();
				if (body != null) {
					InputStream stream = body.byteStream();
					Sharp.loadInputStream(stream).into(target);
					stream.close();
				}
			}
		});
	}
}