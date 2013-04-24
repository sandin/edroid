package com.edroid.utils;

import android.test.InstrumentationTestCase;

public class TextUtilTest extends InstrumentationTestCase {
    
    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
    }
    
    public void testTruncate() {
        String text = "123456789";
        String suffix = "...";
        int length = 5;
        
        String shortText = TextUtil.truncate(text, length, suffix);
        System.out.println("shortText: " + shortText);
        assertEquals("12345...", shortText);
    }
    
    public void testTruncate2() {
        String text = "中文测试56789";
        String suffix = "...";
        int length = 5;
        
        String shortText = TextUtil.truncate(text, length, suffix);
        System.out.println("shortText: " + shortText);
        assertEquals("中文测试5...", shortText);
    }
    

}
