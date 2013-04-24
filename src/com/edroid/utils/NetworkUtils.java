package com.edroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetworkUtils {

    /**
     * 当前是否有网络连接
     * 
     * @param context
     * @return true表示有网络;反之。
     */
    public static boolean isConnected(Context context) {
	ConnectivityManager cm = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo ni = cm.getActiveNetworkInfo();
	return ni != null && ni.isAvailable() && ni.isConnected();
    }

    /**
     * 当前网络类型(WIFI/GPRS)
     * 
     * @param context
     * @return
     */
    public static int getAvailableNetworkType(Context context) {
	ConnectivityManager cm = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo ni;
	ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	if (ni != null && State.CONNECTED.equals(ni.getState()))
	    return ConnectivityManager.TYPE_WIFI;
	ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	if (ni != null && State.CONNECTED.equals(ni.getState()))
	    return ConnectivityManager.TYPE_MOBILE;
	return -1;
    }
}
