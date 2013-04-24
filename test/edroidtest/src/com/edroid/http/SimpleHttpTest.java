package com.edroid.http;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.edroid.utils.IOUtils;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class SimpleHttpTest extends InstrumentationTestCase {

    private Context mContent;
    private SimpleHttp http;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mContent = getInstrumentation().getTargetContext();
        http = SimpleHttp.getInstance();
    }

    @Override
    public void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }

    public void testGet() throws Exception {
        String html = SimpleHttp.doGet("http://www.baidu.com");
        System.out.println("response: " + html);
        assertNotNull(html);
        assertFalse("".equals(html));
        assertTrue(html.contains("baidu"));
    }

    public void testPost() throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("keyword", "china"));
        String res = SimpleHttp.doPost("http://www.google.com.hk", params);
        System.out.println("response: " + res);
        assertNotNull(res);
        assertTrue(res.contains("Error 405 (Method Not Allowed)"));
    }

    public void testAsStream() throws Exception {
        final SimpleHttp http = SimpleHttp.getInstance();
        Response res = http.get("http://www.google.com.hk");
        InputStream in = res.asStream();
        assertNotNull(in);
        
        String str = IOUtils.streamToString(in, "UTF-8");
        System.out.println("response: " + str);
        assertNotNull(str);
    }

    public void testUploadFiles() throws Exception {
        fail("此方法需要额外的JAR包支持");
        
        URI uri = new URI("http://www.google.com");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("keyword", "china"));

        Map<String, File> files = new HashMap<String, File>();
        files.put("myphoto", new File("/home/lds/some.jpg"));

        http.post(uri, params, files);
    }

}
