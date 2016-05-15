package jp.co.isb.tms.util;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class GoogleMapUtils {

	private static final String TAG = GoogleMapUtils.class.getSimpleName();

	private static LatLng getPositionFromAddress(Context aContext,
			String address) {
		Geocoder coder = new Geocoder(aContext);
		List<Address> arrAddress = null;
		try {
			arrAddress = coder.getFromLocationName(address, 5);
		} catch (IOException e) {
			Log.e(TAG, "" + e.toString());
		}
		if (address != null && arrAddress.size() > 0) {
			LatLng latLng = new LatLng(arrAddress.get(0).getLatitude(),
					arrAddress.get(0).getLongitude());
			return latLng;
		}
		return null;
	}

	

	public static double getDistance(Context aContext, Location fromAddress, String toAddress) {
		double distance = 0;
		if (fromAddress == null || toAddress == null) {
			return 0;
		}
		
		LatLng endPos = getPositionFromAddress(aContext, toAddress);
		
		if (endPos == null) {
			return 0;
		}
		
		Location locationA = new Location("A");
		locationA.setLatitude(fromAddress.getLatitude());
		locationA.setLongitude(fromAddress.getLongitude());
		Location locationB = new Location("B");
		locationB.setLatitude(endPos.latitude);
		locationB.setLongitude(endPos.longitude);
		distance = locationA.distanceTo(locationB);
		Log.d(TAG, "getDistance = " + distance);
		return distance;
	}
}
