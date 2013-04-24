package com.edroid.cache;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.text.TextUtils;

import com.edroid.http.Response;
import com.edroid.http.SimpleHttp;
import com.edroid.utils.IOUtils;
import com.edroid.utils.MD5;

public class DiskCacheTest extends InstrumentationTestCase {
    
    private Context mContext;
    private StreamCache cache;
    
    private File cacheDir;
    private String cacheSuffix;

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        
        mContext = getInstrumentation().getTargetContext();
        cacheDir = mContext.getCacheDir();
        cacheSuffix = ".stream";
        cache = new StreamCache(cacheDir, "", cacheSuffix);
    }
    
    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }
    
    private InputStream getStream(String url) throws Exception {
        Response res = SimpleHttp.getInstance().get(url);
        assertNotNull(res);
        InputStream in = res.asStream();
        assertNotNull(in);
        return in;
    }
    
    public void testPut() throws Exception {
        String url = "http://www.baidu.com";
        InputStream in = getStream(url);
        
        // cache it
        String key = MD5.md5(url);
        cache.put(key, in);
        
        // check this cache file is exists
        File cacheFile = new File(cacheDir, cache.hash(key) + cacheSuffix);
        System.out.println("cachefile: " + cacheFile);
        File[] cacheFilesList = cacheDir.listFiles();
        for (File f: cacheFilesList) System.out.println("cachefiles: " + f);
        assertTrue(Arrays.asList(cacheFilesList).contains(cacheFile));
    }
    
    public void  testGet() throws Exception {
        String url = "http://www.douban.com";
        InputStream in = getStream(url);
        String key = MD5.md5(url);
        cache.put(key, in);
        
        // contains
        assertTrue(cache.contains(key));
        InputStream obj = cache.get(key);
        assertNotNull(obj);
        
        // get cache
        String html = IOUtils.streamToString(obj, "UTF-8");
        assertNotNull(html);
        assertFalse(TextUtils.isEmpty(html));
        System.out.println("html: " + html);
    }

}
