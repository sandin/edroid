package com.edroid.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.edroid.demo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.test.InstrumentationTestCase;


public class BitmapUtilsTest extends InstrumentationTestCase {
    
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        context = getInstrumentation().getTargetContext();
    }
    
    // download a image for test
    // WARNING: 依赖IOUtils
    private File getImageFile() throws IOException {
        InputStream in = context.getAssets().open("image.jpg");
        assertNotNull(in);
        
        File file = new File(context.getCacheDir(), "image.jpg");
        System.out.println("file: " + file.getAbsolutePath());
        IOUtils.writeInputStreamToFile(in, file.getAbsolutePath());
        assertTrue(file.exists());
        assertTrue(file.length() > 1024);
        return file;
    }
    
    public void testCompressImage() throws Exception {
        File imageFile = getImageFile();
        int maxWidth = 10;
        int maxHeight = 10;
        int quality = 100;
        Bitmap bm = BitmapUtils.compressImage(imageFile, maxWidth, maxHeight, quality);
        int width = bm.getWidth();
        int height = bm.getHeight();
        System.out.println("size: " + width + "*" + height);
        
        assertTrue(width > 0);
        assertTrue(height > 0);
        
        assertTrue(Math.abs(maxWidth - width) < 10); // 此压缩方式不是精确的
        assertTrue(Math.abs(maxHeight - height) < 10);
    }
    
    public void testDrawableToBitmap() throws Exception {
        Bitmap bitmap = BitmapUtils.drawableToBitmap(getDrawable());
        assertNotNull(bitmap);
        assertTrue(bitmap.getWidth() > 0);
        assertTrue(bitmap.getHeight() > 0);
        
        bitmap.recycle();
    }
    
    
    public void testResizeBitmap() {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_launcher);
        assertNotNull(drawable);
        Bitmap bitmap = BitmapUtils.drawableToBitmap(getDrawable());
        assertNotNull(bitmap);
        
        int maxWidth = 10;
        int maxHeight = 10;
        Bitmap bitmap2 = BitmapUtils.resizeBitmap(bitmap, maxWidth, maxHeight);
        assertNotNull(bitmap2);
        
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        assertTrue(width > 0);
        assertTrue(height > 0);
        assertTrue(width <= maxWidth);
        assertTrue(height <= maxHeight);
        
        bitmap.recycle();
        bitmap2.recycle();
    }
    
    private Drawable getDrawable() {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_launcher);
        assertNotNull(drawable);
        return drawable;
    }
}
