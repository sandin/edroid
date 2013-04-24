package com.edroid.utils;

import android.test.InstrumentationTestCase;

public class MD5Test extends InstrumentationTestCase {

    public void testMd5() {
        test("5f4dcc3b5aa765d61d8327deb882cf99", "password");
        test("202cb962ac59075b964b07152d234b70", "123");
        test("5eb63bbbe01eeed093cb22bb8f5acdc3", "hello world");
    }

    private void test(String expected, String text) {
        String m = MD5.md5(text);
        assertEquals(expected, m);
    }

}
