package com.edroid.utils;

import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class BitmapUtils {
	private static final String TAG = "BitmapUtils";
	
	/**
	 * Compress and resize the Image
	 * 
	 * @param targetFile
	 * @param quality
	 *            , 0~100, recommend 100
	 * @return
	 * @throws IOException
	 */
	public static Bitmap compressImage(File targetFile, int maxWidth, int maxHeight, int quality) throws IOException {
		String filepath = targetFile.getAbsolutePath();

		// 1. Calculate scale
		int scale = 1;
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, o);
		if (o.outWidth > maxWidth || o.outHeight > maxHeight) {
			scale = (int) Math.pow(
					2.0,
					(int) Math.round(Math.log(maxWidth
							/ (double) Math.max(o.outHeight, o.outWidth))
							/ Math.log(0.5)));
			// scale = 2;
		}
		Log.d(TAG, scale + " scale");

		// 2. File -> Bitmap (Returning a smaller image)
		o.inJustDecodeBounds = false;
		o.inSampleSize = scale;
		Bitmap compressedImage = BitmapFactory.decodeFile(filepath, o);
		return compressedImage;
	}

	/**
	 * 保持长宽比缩小Bitmap
	 * 
	 * 如果超过指定的长宽，则按比例缩小图片，以宽度为主，长度如果超过，则会居中截取。
	 * 
	 * @param bitmap
	 * @param maxWidth
	 * @param maxHeight
	 * @param quality
	 *            1~100
	 * @return
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {

		int originWidth = bitmap.getWidth();
		int originHeight = bitmap.getHeight();

		// no need to resize
		if (originWidth < maxWidth && originHeight < maxHeight) {
			return bitmap;
		}

		int newWidth = originWidth;
		int newHeight = originHeight;

		// 若图片过宽, 则保持长宽比缩放图片
		if (originWidth > maxWidth) {
			newWidth = maxWidth;

			double i = originWidth * 1.0 / maxWidth;
			newHeight = (int) Math.floor(originHeight / i);

			bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight,
					true);
		}

		// 若图片过长, 则从中部截取
		if (newHeight > maxHeight) {
			newHeight = maxHeight;

			int half_diff = (int) ((originHeight - maxHeight) / 2.0);
			bitmap = Bitmap.createBitmap(bitmap, 0, half_diff, newWidth,
					newHeight);
		}

		Log.d(TAG, newWidth + " width");
		Log.d(TAG, newHeight + " height");

		return bitmap;
	}
	
	/**
	 * Convert Drawable to Bitmap
	 * 
	 * @param drawable
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}


}
