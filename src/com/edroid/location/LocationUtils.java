package com.edroid.location;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.edroid.location.LocationObserver.LocationObserverListener;

public class LocationUtils {
    private static Location currentLocation = null;

    /**
     * 获取当前位置的同步版接口（会阻塞当前进程，直到拿到位置信息)
     * 
     * @param locationObserver
     * @param context
     * @param timeout
     * @return
     */
    public static Location getCurrentLocation(
            LocationObserver locationObserver, final Context context,
            long timeout) {
        currentLocation = null;
        locationObserver.getCurrentLocation(new LocationObserverListener() {

            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
            }

            @Override
            public boolean onGetBestLocation(Location location) {
                currentLocation = location;
                return true;
            }

            @Override
            public void onNoProviderSupport() {
                Toast.makeText(context.getApplicationContext(),
                        "无法获取地理位置信息, 请开启定位服务", Toast.LENGTH_SHORT).show();
            }

        });
        // 直到监听到第一个结果
        long startTime = System.currentTimeMillis();
        while (currentLocation == null) {
            if (System.currentTimeMillis() - startTime > timeout) {
                Log.w("LocationUtil", "timeout");
                break; // TIMEOUT
            }
            try {
                Log.i("LOCATION", "未得到结果，请等待...");
                Log.i("LOCATION", "LastLocation: " + currentLocation);
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return currentLocation;
    }

}
