package com.dimaslanjaka.tools.Libs.Graphics;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

public class FromURL {
	private int counter = 0;
	private Target target = new Target() {
		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
		}

		/**
		 * Callback indicating the image could not be successfully loaded.
		 * <p>
		 * <strong>Note:</strong> The passed {@link Drawable} may be {@code null} if none has been
		 * specified via {@link RequestCreator#error(Drawable)}
		 * or {@link RequestCreator#error(int)}.
		 *
		 * @param e error
		 * @param errorDrawable error
		 */
		@Override
		public void onBitmapFailed(Exception e, Drawable errorDrawable) {

		}

		@Override
		public void onPrepareLoad(Drawable placeHolderDrawable) {
		}
	};

	public void BitmapFromUrl(Target target, String url) {
		Picasso.get().load(url).into(target);
	}

	public void cancel(Target target) {
		Picasso.get().cancelRequest(target);
	}
}
