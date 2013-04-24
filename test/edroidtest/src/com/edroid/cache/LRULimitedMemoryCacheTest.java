package com.edroid.cache;

import android.content.Context;
import android.support.v4.util.LruCache;
import android.test.InstrumentationTestCase;

import com.edroid.http.SimpleHttp;

public class LRULimitedMemoryCacheTest extends InstrumentationTestCase {
    
    private Context mContext;
    private LruCache<String, String> cache;
    
    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        
        mContext = getInstrumentation().getTargetContext();
        cache = new LruCache<String, String>(1024*1024);
    }
    
    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }
            
    public void testA() throws Exception {
        String url = "http://www.baidu.com";
        String html = SimpleHttp.getAsString(url);
        System.out.println("html: " + html);
        cache.put(url, html);
        
        html = cache.get(url);
        System.out.println("html from cache: " + html);
        assertNotNull(html);
    }

}
