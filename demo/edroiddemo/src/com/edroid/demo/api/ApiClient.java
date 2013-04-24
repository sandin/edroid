package com.edroid.demo.api;

import java.io.IOException;

import android.net.Uri;
import android.net.Uri.Builder;

import com.edroid.http.Response;
import com.edroid.http.SimpleHttp;
import com.edroid.http.SimpleHttpException;

/**
 * 与接口交互类
 * 
 */
public class ApiClient {
    
    /**
     * 接口路径
     */
    private static final String BASE_URL = "http://www.google.com.hk/";
    
    /**
     * 搜索方法路径
     */
    private static final String SEARCH_URL =  BASE_URL + "search";
    
    /**
     * HttpClient
     */
    private SimpleHttp http;
    
    public ApiClient() {
        http = SimpleHttp.getInstance();
    }
    
    /**
     * 接口方法 - Search
     * 
     * @param keyword
     * @return
     * @throws SimpleHttpException
     * @throws IOException
     */
    public String search(String keyword) throws SimpleHttpException, IOException {
        Builder builder = Uri.parse(SEARCH_URL).buildUpon()
               .appendQueryParameter("q", keyword);
        Response res = http.get(builder.build().toString());
        return res.asString();
    }

}
