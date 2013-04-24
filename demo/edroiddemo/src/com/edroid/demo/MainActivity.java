package com.edroid.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.edroid.demo.imageloader.ImageLoaderActivity;
import com.edroid.demo.location.LocationActivity;
import com.edroid.demo.widget.ArrayAdapterDemo;
import com.edroid.imageloader.ImageLoaderManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btn01 = (Button) findViewById(R.id.array_adapter_demo);
        btn01.setOnClickListener(this);
        
        Button btn02 = (Button) findViewById(R.id.location_demo);
        btn02.setOnClickListener(this);
        
        Button btn03 = (Button) findViewById(R.id.imageloader_demo);
        btn03.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.array_adapter_demo:
            startActivity(new Intent(MainActivity.this, ArrayAdapterDemo.class));
            break;
        case R.id.location_demo:
            startActivity(new Intent(MainActivity.this, LocationActivity.class));
            break;
        case R.id.imageloader_demo:
            startActivity(new Intent(MainActivity.this, ImageLoaderActivity.class));
            break;
        default:
            break;
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}
