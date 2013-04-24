package com.edroid.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class IOUtilsTest extends InstrumentationTestCase {
    
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        context = getInstrumentation().getTargetContext();
    }
    
    private InputStream getInputStream() throws IOException {
        InputStream in = context.getAssets().open("image.jpg");
        assertNotNull(in);
        return in;
    }
    
    public void testCopy() throws IOException {
        File file = getRandomFile();
        FileOutputStream output = new FileOutputStream(file);
        
        int count = IOUtils.copy(getInputStream(), output);
        assertTrue(count > 1024);
        assertTrue(file.exists());
        
        // cleanup
        file.delete();
    }
    
    public void testStreamToString() throws IOException {
        InputStream in = context.getAssets().open("text.txt");
        assertNotNull(in);
        
        String text = IOUtils.streamToString(in, "UTF-8");
        assertNotNull(text);
        assertTrue(text.contains("It's Working"));
    }
    
    private File getRandomFile() {
        File file = new File(context.getCacheDir(), "image+" + System.currentTimeMillis());
        assertNotNull(file);
        if (file.exists()) file.delete();// 确保文件之前不存在
        return file;
    }
    
    public void testWriteInputStreamToFile() throws IOException {
        File file = getRandomFile();
        
        InputStream in = getInputStream();
        IOUtils.writeInputStreamToFile(in, file.getAbsolutePath());
        
        assertTrue(file.exists());
        assertTrue(file.length() > 1024);
        
        // cleanup
        file.delete();
    }

}
