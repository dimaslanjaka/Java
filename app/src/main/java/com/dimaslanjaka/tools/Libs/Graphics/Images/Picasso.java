package com.dimaslanjaka.tools.Libs.Graphics.Images;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import com.dimaslanjaka.tools.Libs.Hash.md5;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Request;

public class Picasso {
	private static boolean initializedPicasso = false;

	private static com.squareup.picasso.Picasso getCustomPicasso(Context context) {
		com.squareup.picasso.Picasso.Builder builder = new com.squareup.picasso.Picasso.Builder(context);
		//set 12% of available app memory for image cache
		builder.memoryCache(new LruCache(12));
		//set request transformer
		com.squareup.picasso.Picasso.RequestTransformer requestTransformer =
						new com.squareup.picasso.Picasso.RequestTransformer() {
							/**
							 * Transform a request before it is submitted to be processed.
							 *
							 * @param request
							 * @return The original request or a new request to replace it. Must not be null.
							 */
							@Override
							public Request transformRequest(Request request) {
								Log.d("image request", request.toString());
								return request;
							}
						};
		builder.requestTransformer(requestTransformer);

		return builder.build();
	}

	public static void fromURL(Context context, String url, ImageView view) {
		if (!initializedPicasso) {
			com.squareup.picasso.Picasso.setSingletonInstance(getCustomPicasso(context));
			initializedPicasso = true;
		}
		com.squareup.picasso.Picasso.get().load(url).tag(md5.encode(url)).into(view);
	}
}
