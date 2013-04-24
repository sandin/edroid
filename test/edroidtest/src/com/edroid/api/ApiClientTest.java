package com.edroid.api;

import com.edroid.demo.api.ApiClient;

import android.test.InstrumentationTestCase;

public class ApiClientTest extends InstrumentationTestCase {
    private ApiClient client;
    
    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        
        client = new ApiClient();
    }
    
    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }
    
    public void testSearch() throws Exception {
        String keyword = "hot";
        String response = client.search(keyword);
        System.out.println("response: " + response);
        assertNotNull(response);
        assertFalse("".equals(response));
    }
    

}
