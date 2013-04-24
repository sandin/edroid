package com.edroid.demo.location;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.edroid.demo.R;
import com.edroid.location.LocationObserver;
import com.edroid.location.LocationObserver.LocationObserverListener;

public class LocationActivity extends Activity {

    private ProgressDialog progressDialog;
    private Button btn;
    private TextView textView;
    private LocationObserver locationObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initViews();
        
        locationObserver = LocationObserver.getInstance(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void initViews() {
        btn = (Button) findViewById(R.id.get_location_btn);
        textView = (TextView) findViewById(R.id.location);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }

        });

    }

    /**
     * 获取当前位置
     */
    private void getLocation() {
        progressDialog = ProgressDialog.show(this, "提示", "正在获取您的位置...", true, true);
        
        locationObserver.getCurrentLocation(new LocationObserverListener() {
            
            @Override
            public void onNoProviderSupport() {
                Toast.makeText(getApplicationContext(), "GPS是否开启?", Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onLocationChanged(Location location) {
            }
            
            @Override
            public boolean onGetBestLocation(Location location) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                textView.setText(location.toString());
                return true;
            }
        });
    }
}
