package com.dimaslanjaka.tools.Libs.Graphics;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by ketan on 1/18/2016.
 */
public class CustomImage extends androidx.appcompat.widget.AppCompatImageView {

	public CustomImage(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);

	}

	public static Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {
		Bitmap finalBitmap;
		if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
			finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
							false);
		else
			finalBitmap = bitmap;
		Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
						finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
						finalBitmap.getHeight());

		/*change these value or make it dynamic*/
		Point point0_draw = new Point(0, 0);
		Point point1_draw = new Point(0, 175);
		Point point2_draw = new Point(101, 198);
		Point point3_draw = new Point(198, 175);
		Point point4_draw = new Point(198, 0);

		Path path = new Path();
		path.moveTo(point0_draw.x, point0_draw.y);
		path.lineTo(point1_draw.x, point1_draw.y);
		path.lineTo(point2_draw.x, point2_draw.y);
		path.lineTo(point3_draw.x, point3_draw.y);
		path.lineTo(point4_draw.x, point4_draw.y);
		path.lineTo(point0_draw.x, point0_draw.y);
		path.close();
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawPath(path, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(finalBitmap, rect, rect, paint);

		return output;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

		int w = getWidth(), h = getHeight();

		Bitmap croppedBitmap = getCroppedBitmap(bitmap, w);
		canvas.drawBitmap(croppedBitmap, 0, 0, null);

	}
}