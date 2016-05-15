package jp.co.isb.tms.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jp.co.isb.tms.R;
import jp.co.isb.tms.common.ConfirmEnableNetworkDialog;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.googlemap.GPSManager;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.util.NetworkUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import android.support.v4.app.FragmentTransaction;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.BitmapDescriptor;
/**
 * Load specific location on the map
 * @author ltmen
 *
 */
public class GoogleMapActivity extends FragmentActivity {
	private static final String TAG = GoogleMapActivity.class.getSimpleName();
	private GPSManager mGPSManager;
	private GoogleMap mGoogleMap;
	private Marker mMyLocationMarker;
	private Marker mCustomerLocationMarker;
	private String mBundleBatchCode;
	private Button mBtnMenu;
	private Button mBtnBack;
	  
	private ArrayList<String> arrRedAddress = new ArrayList<String>();
	private ArrayList<String> arrBlueAddress = new ArrayList<String>();
	private ArrayList<String> arrYellowAddress = new ArrayList<String>();
	private ArrayList<OrderInfo> mRedRouteList = new ArrayList<OrderInfo>();
	private ArrayList<OrderInfo> mBlueRouteList = new ArrayList<OrderInfo>();
	private ArrayList<OrderInfo> mYellowRouteList = new ArrayList<OrderInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_google_map);
		
		setUpEventScreen();
		
		Intent i = getIntent();
		mBundleBatchCode = (String) i.getStringExtra(Def.TAG_BUNDLE_BATCH_CODE);
		initCustomerMarkerOnMap();
	}
	
	private void initCustomerMarkerOnMap() {
		mRedRouteList = DatabaseManager.getInstance().getDataAssignedToDriver(mBundleBatchCode);
		ArrayList<String> status = new ArrayList<String>();
		status.add("030");
		mBlueRouteList = DatabaseManager.getInstance().getDeliveryOrderForRouteWithStatus(status);
		
		arrRedAddress = getAddress(removeDuplicate(mRedRouteList));
		arrBlueAddress = getAddress(removeDuplicate(removeRedInBlueList(mBlueRouteList,mRedRouteList)));
		
		ArrayList<String> status2 = new ArrayList<String>();
		status2.add("010");
		mYellowRouteList = DatabaseManager.getInstance().getDeliveryOrderForRouteWithStatus(status2);
		arrYellowAddress = getAddress(removeDuplicate(mYellowRouteList));
		
		setUpGoogleMapScreen();
	}
	
	private ArrayList<OrderInfo> removeRedInBlueList(ArrayList<OrderInfo> arrBlueList, ArrayList<OrderInfo> arrRedList) {
		
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		
		try {
			
			for (int i = 0; i < arrBlueList.size(); i++) {
				
				boolean colorFlg = true;
				
				for (int j = 0; j < arrRedList.size(); j++) {
					
					String add1 = arrBlueList.get(i).getCustomerAddress();
					String add2 = arrRedList.get(j).getCustomerAddress();
					
					if (add1.equalsIgnoreCase(add2)) {
						
						colorFlg = false;
						break;
					}
					
				}
				
				if (colorFlg) {
					list.add(arrBlueList.get(i));
				}
			}
		} catch (Exception e) {
					
		}

	    return list;
	}

	private ArrayList<OrderInfo> removeDuplicate(ArrayList<OrderInfo> arrList) {
		
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		
		try {
			
			for (int i = 0; i < arrList.size(); i++) {
				
				boolean colorFlg = true;
				
				for (int j = i + 1; j < arrList.size(); j++) {
					
					String add1 = arrList.get(i).getCustomerAddress();
					String add2 = arrList.get(j).getCustomerAddress();
					
					if (add1.equalsIgnoreCase(add2)) {
						
						colorFlg = false;
						break;
					}
					
				}
				
				if (colorFlg) {
					list.add(arrList.get(i));
				}
			}
		} catch (Exception e) {
					
		}

	    return list;
	}
	
	public ArrayList<String> getAddress(ArrayList<OrderInfo> aOrderInfos) {
		if (aOrderInfos == null || aOrderInfos.size() == 0) {
			return null;
		}
		ArrayList<String> address = new ArrayList<String>();
		for (int i = 0; i < aOrderInfos.size(); i++) {
			address.add(aOrderInfos.get(i).getCustomerAddress());
			Log.d(TAG, "address" + i + ": " + aOrderInfos.get(i).getCustomerAddress());
		}
		return address;
	}
	
	private void setUpEventScreen(){
		mBtnMenu = (Button)findViewById(R.id.btn_menu);
		mBtnBack = (Button)findViewById(R.id.btn_back);
		
		mBtnMenu.setOnClickListener(new OnClickListener() {
			@SuppressLint("InlinedApi")
			@Override
			public void onClick(View arg0) {
				Intent newIntent = new Intent(GoogleMapActivity.this, MenuActivity.class);
				newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(newIntent);
			}
		});
		
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void setUpGoogleMapScreen() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getGoogleMap();
				showSpecificAddressOnMap(arrRedAddress, arrBlueAddress, arrYellowAddress);
			}
		}).start();
		setUpGPSManager();
		if (!NetworkUtils.checkNetworkAvailable(getBaseContext())){
			new ConfirmEnableNetworkDialog(this).show();;
		}
	}
	
	private void setUpGPSManager() {
		mGPSManager = new GPSManager(this, new LocationListener() {
			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}

			@Override
			public void onProviderEnabled(String arg0) {
			}

			@Override
			public void onProviderDisabled(String arg0) {
			}

			@Override
			public void onLocationChanged(Location location) {
				updateUiWithLocation(location);
			}
		});
	}
	
	private void getGoogleMap() {
		if (mGoogleMap == null) {
			mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mGoogleMap != null && mGPSManager != null) {
                
				updateUiWithLocation(mGPSManager.getCurrentLocation());
			}
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void updateUiWithLocation(Location location) {
		if (location != null) {
			moveMapToLocation(location);
		}
	}

	private String getTitleForMaker(double aLatitude, double aLongitude) {
		String title = "";
		Geocoder coder = new Geocoder(getBaseContext());
		List<Address> lstAddress = null;
		try {
			lstAddress = coder.getFromLocation(aLatitude, aLongitude, 1);			
		} catch (IOException e) {
			Log.e(TAG, "" +  e.toString());
		}
		if (lstAddress != null && lstAddress.size() > 0) {
			
			Address address = lstAddress.get(0);
			String buf;
			for (int i = 0; (buf = address.getAddressLine(i)) != null; i++) {
				title += buf;
				if (i < 1) {
					title += ", ";
				}
			}
		}
		return title;
	}

	private void moveMapToLocation(Location location) {
		final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		final String title = getTitleForMaker(location.getLatitude(), location.getLongitude());
		if (mMyLocationMarker == null) {
			if (mGoogleMap != null) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mMyLocationMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
					}
				});
			}
		} else {
			mMyLocationMarker.setPosition(latLng);
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Def.ZOOM_LEVEL));
			}
		});
	}

	private void showSpecificAddressOnMap(ArrayList<String> arrRedAddr, ArrayList<String> arrBlueAddr, ArrayList<String> arrYellowAddr) {
		
		Geocoder coder = new Geocoder(getBaseContext());
		ArrayList<MarkerOptions> list = new ArrayList<MarkerOptions>();
		
		try {
			
			if (arrRedAddr != null) {

				for (int i = 0; i < arrRedAddr.size(); i++) {
					
					List<Address> address = null;
					try {
						address = coder.getFromLocationName(arrRedAddr.get(i), 5);					
					} catch (IOException e) {
						Log.e(TAG, "" + e.toString());
					}
					
					if (address != null && address.size() > 0) {
						final String title = getTitleForMaker(address.get(0).getLatitude(), address.get(0).getLongitude());
						final LatLng latLng = new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());
						
				
						if (mGoogleMap != null) {
							
							list.add( new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	
		
						}
					}
				}
			
			}
			
			if (arrBlueAddr != null) {
			
				for (int i = 0; i < arrBlueAddr.size(); i++) {
					
					List<Address> address = null;
					try {
						address = coder.getFromLocationName(arrBlueAddr.get(i), 5);					
					} catch (IOException e) {
						Log.e(TAG, "" + e.toString());
					}
					
					if (address != null && address.size() > 0) {
						final String title = getTitleForMaker(address.get(0).getLatitude(), address.get(0).getLongitude());
						final LatLng latLng = new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());
						
				
						if (mGoogleMap != null) {
							
							list.add( new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
	
		
						}
					}
				}
			
			}
			
			if (arrYellowAddr != null) {
			
				for (int i = 0; i < arrYellowAddr.size(); i++) {
					
					List<Address> address = null;
					try {
						address = coder.getFromLocationName(arrYellowAddr.get(i), 5);					
					} catch (IOException e) {
						Log.e(TAG, "" + e.toString());
					}
					
					if (address != null && address.size() > 0) {
						final String title = getTitleForMaker(address.get(0).getLatitude(), address.get(0).getLongitude());
						final LatLng latLng = new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());
						
				
						if (mGoogleMap != null) {
							
							list.add( new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
	
		
						}
					}
				}
			
			}
			
		} catch(Exception e) {
			e.getMessage();
		}
		
		final ArrayList<MarkerOptions> list2 = list;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				for(int i=0; i < list2.size(); i++) {
					mGoogleMap.addMarker(list2.get(i));
				}
			}
		});	
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}
