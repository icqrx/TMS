package jp.co.isb.tms.googlemap;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

/**
 * The manager class to control Location (GPS and Network)
 * 
 * @author tuannguyen
 * 
 */
public class GPSManager {
	private static final String TAG = GPSManager.class.getSimpleName();
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 1; // 1
																		// minute
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private Location mCurrentLocation;

	/**
	 * init GPS manager
	 * 
	 * @param context
	 * @param locationListener
	 */
	public GPSManager(Context context, LocationListener locationListener) {
		mLocationManager = (LocationManager) context
				.getSystemService(Activity.LOCATION_SERVICE);
		mLocationListener = locationListener;
		setupLocationListener();
	}

	/**
	 * this is Location listener of this Manager class. we catch it to setup
	 * location listener again in case provider is changed status.
	 */
	private LocationListener mOwnerLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			Log.d(TAG, "onLocationChanged->location=" + location);
			mCurrentLocation = location;
			if (mLocationListener != null) {
				mLocationListener.onLocationChanged(location);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(TAG, "onStatusChanged->provider=" + provider + ", status="
					+ status);
			if (mLocationListener != null) {
				mLocationListener.onStatusChanged(provider, status, extras);
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.d(TAG, "onProviderEnabled->provider=" + provider);
			if (mLocationListener != null) {
				mLocationListener.onProviderEnabled(provider);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.d(TAG, "onProviderDisabled->provider=" + provider);
			if (mLocationListener != null) {
				mLocationListener.onProviderDisabled(provider);
			}
		}
	};

	/**
	 * setup Location Listener. This method will setup OwnerListener for
	 * LocationManager.
	 */
	private void setupLocationListener() {
		mLocationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES,
				MIN_DISTANCE_CHANGE_FOR_UPDATES, mOwnerLocationListener);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,
				mOwnerLocationListener);
	}

	/**
	 * get the cached current location.
	 * 
	 * @return
	 */
	public Location getCurrentLocation() {
		if (mCurrentLocation == null) {
			mCurrentLocation = getLastKnownLocation();
		}
		return mCurrentLocation;
	}

	/**
	 * get last know location from LocationManager.
	 * 
	 * @return {@link Location} the last know location.
	 */
	public Location getLastKnownLocation() {
		boolean isGPSEnabled = mLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnabled = mLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		Location location = null;
		if (isNetworkEnabled) {
			location = mLocationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (isGPSEnabled) {
			if (location == null) {
				location = mLocationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
		}
		return location;
	}

	/**
	 * This method will check GPS or Network provider is enabled or not.
	 * 
	 * @return true: GPS or Network enabled. false: both of them are off.
	 */
	public boolean isEnabledLocationSetting() {
		return isLocationGPSEnabled() || isLocationNetworkEnabled();
	}

	/**
	 * check GPS (location setting) enabled or not.
	 * 
	 * @return GPS status
	 */
	public boolean isLocationGPSEnabled() {
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * check network (location setting) enabled or not
	 * 
	 * @return Network location setting status
	 */
	public boolean isLocationNetworkEnabled() {
		return mLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * stop location listener.
	 */
	public void stopLocationListener() {
		mLocationManager.removeUpdates(mLocationListener);
	}

	/**
	 * get {@link LocationProvider} by provider name
	 * 
	 * @param provider
	 * @return
	 */
	public LocationProvider getProvider(String provider) {
		return mLocationManager.getProvider(provider);
	}
}
