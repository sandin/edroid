package com.edroid.cache;

import java.io.File;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.edroid.http.SimpleHttp;

public class ObjectCacheTest extends InstrumentationTestCase {
    
    private Context mContext;
    private ObjectCache cache;
    
    private File cacheDir;
    private String cacheSuffix;
    private int cacheSize = 128*1024;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        mContext = getInstrumentation().getTargetContext();
        cacheDir = mContext.getCacheDir();
        cacheSuffix = ".obj";
        cache = new ObjectCache(cacheDir, "", cacheSuffix, cacheSize);
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        
        //cache.clear();
    }
    
    public void testA() throws Exception {
        String name = "jack";
        int age = 25;
        
        Person p = new Person();
        p.setName(name);
        p.setAge(age);
        
        cache.put(name, p);
        cache.put(name+1, p);
        cache.put(name+2, p);
        assertTrue(cache.contains(name+2));
        assertTrue(cache.contains(name+1));
        assertTrue(cache.contains(name));
        
        Person obj = (Person) cache.get(name);
        System.out.println("Object: " + obj);
        assertNotNull(obj);
        assertEquals(name, obj.getName());
        assertEquals(age, obj.getAge());
        
        int size = cache.getCacheSize();
        System.out.println("size: " + size);
    }
    
    public void testB() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println("i: " + i);
            String url = "http://www.baidu.com";
            String html = SimpleHttp.getAsString(url);
            assertNotNull(html);
            cache.put(url+i, html);
            assertTrue(cache.contains(url+i));
        }
        
        System.out.println("cache size: " + cache.getCacheSize());
        assertTrue(cache.getCacheSize() <= cacheSize);
    }

}
