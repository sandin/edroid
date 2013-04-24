package com.edroid.location;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 获取地理位置信息帮助类
 */
public class LocationObserver {
	private static final String TAG = "LocationObserver";
	
	private LocationManager mLocationManager;

	private static final int MESSAGE_START_LISTENER = 1;
	private static final int MESSAGE_STOP_LISTENER = 2;
	
	private Handler mHander = new Handler(){
		public void handleMessage(Message msg) {
			System.out.println("handlerMessage: " + msg);
			 switch(msg.what) {
				case MESSAGE_START_LISTENER:
					startListener();
					break;
				case MESSAGE_STOP_LISTENER:
					stopListener();
					break;
				default:
					break;
			};
		};
	};
	
	private static LocationObserver sInstance;
	public static LocationObserver getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new LocationObserver(context);
		}
		return sInstance;
	}
	
	public LocationObserver(Context context) {
		mLocationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}
	
	private void startListener() {
		if (mLocationManager != null) {
			Log.i(TAG, "startLocationListener");
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mListener);
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mListener);
		}
	}
	
	private void stopListener() {
		if (mLocationManager != null) {
			Log.i(TAG, "stopLocationListener");
			mLocationManager.removeUpdates(mListener);
		}
	}
	
	private static final int MAX_TRY = 0; // 最多尝试几次（如果是NetWork定位, 即可等待GPS定位)
	private static final long TIMEOUT = 300;
	
	private Location lastLocation = null;
	private int onLocationChangedCount = 0;
	
	private void reset() {
		lastLocation = null;
		onLocationChangedCount = 0;
	}
	
	private void stopListenerFromOtherThread() {
		mHander.sendEmptyMessage(MESSAGE_STOP_LISTENER);
	}
	
	private LocationListener mListener = new LocationListener() {
		
		private void noticeListener(Location location) {
			if (locationObserverListener != null) {
				Log.i(TAG, "onGetBestLocationChanged: " + location);
				boolean noNeed = locationObserverListener.onGetBestLocation(location);
				if (noNeed) { stopListenerFromOtherThread(); }
			}
			reset();
		}
		
		@Override
		public void onLocationChanged(Location location) {
			Log.i(TAG, "onLocationChanged: " + location + ", count: " + onLocationChangedCount);
			if (locationObserverListener != null) {
				locationObserverListener.onLocationChanged(location);
			}
			// 如果是GPS定位则立即通知回调
			if (location != null && location.getProvider() == LocationManager.GPS_PROVIDER) {
				noticeListener(location);
				return;
			}
			// 总是把最好的结果缓存起来
			if (lastLocation == null || isBetterLocation(location, lastLocation)) {
				lastLocation = location;
			}
			// 如果是NETWORK则试几次会再通知回调(等GPS)
			if (onLocationChangedCount >= MAX_TRY) {
				noticeListener((lastLocation != null) ? lastLocation : location);
				return;
			}
			onLocationChangedCount++;
			lastLocation = location;
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private LocationObserverListener locationObserverListener;
	
	/**
	 * 异步获取当前位置
	 * 
	 * @param l 回调函数
	 */
	public void getCurrentLocation(LocationObserverListener l) {
		System.out.println("getCurrentLocation: " + l);
		locationObserverListener = l;
		mHander.sendEmptyMessage(MESSAGE_START_LISTENER);
		//mHander.sendEmptyMessage(MESSAGE_STOP_LISTENER);
	}
	
	public interface LocationObserverListener {
		
	    /**
	     * 当获取位置时
	     * 
	     * @param location
	     */
		void onLocationChanged(Location location);
		
		/**
		 * 当获取最佳位置时
		 * @param location
		 * @return true表示可以removeUpdateListener了
		 */
		boolean onGetBestLocation(Location location);
		
		/**
		 * 当没有provider，导致无法获取地址位置时
		 */
		void onNoProviderSupport();
	}
	
	/**
	 * 检查network和GPS至少有一个
	 * @param context
	 * @return
	 */
	public static boolean noProviderSupport(Context context) {
		List<String> prodviders = LocationObserver.getEnabledProviders(context);
		return (prodviders.size() <= 1);
		/**
		 * Provider network: false
		 * Provider passive: true
		 * Provider gps: false
		 */
	}
	
	public static List<String> getEnabledProviders(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// List all providers:
		List<String> providers = locationManager.getAllProviders();
		List<String> enabledProviders = new ArrayList<String>();
		Log.v(TAG, "LIST ALL PROVIDERS:");
		for (String provider : providers) {
			boolean isEnabled = locationManager.isProviderEnabled(provider);
			Log.v(TAG, "Provider " + provider + ": " + isEnabled);
			if (isEnabled) {
				enabledProviders.add(provider);
			}
		}
		return enabledProviders;
	}
	
	
	////////////////////////////
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}

}
