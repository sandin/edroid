package com.edroid.utils;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class NetworkUtilsTest extends InstrumentationTestCase {

    private Context context;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        context = getInstrumentation().getTargetContext();
    }
    
    public void testIsNetworkOnline() {
        boolean isOnline = NetworkUtils.isNetworkOnline(context);
        assertTrue(isOnline); // 确保手机有网络，否则断言不会通过
        // TODO: 无网络时也需要测试
        //assertFalse(isOnline);
    }
}
