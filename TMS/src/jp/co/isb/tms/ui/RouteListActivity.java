package jp.co.isb.tms.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jp.co.isb.tms.EStatus_CD;
import jp.co.isb.tms.R;
import jp.co.isb.tms.RouteAdapter;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.common.UserInfoPref;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.gps.CurrentLocation;
import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.model.OrderStatusInfo;
import jp.co.isb.tms.server.Send;
import jp.co.isb.tms.util.DateTimeUtils;
import jp.co.isb.tms.util.GoogleMapUtils;
import jp.co.isb.tms.util.NetworkUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class RouteListActivity extends BaseActivity implements OnClickListener {
	private final String TAG = RouteListActivity.class.getSimpleName();
	private ListView mListView;
	private Spinner mDeliveryStatusSpinner;

	private RouteAdapter mAdapter;
	private ArrayList<OrderStatusInfo> mOrderStatusInfos;
	private ArrayList<OrderInfo> mRouteList = new ArrayList<OrderInfo>();
	private ArrayList<OrderInfo> mAll = new ArrayList<OrderInfo>();
	private int mSelectedPosition = 0;
	
	// get driver list from local database
	ArrayList<String> ids = new ArrayList<String>();
	ArrayList<String> idsOthers = new ArrayList<String>();
	ArrayList<String> idsAll = new ArrayList<String>();
	
	private Handler mHandler = new Handler();
	private CurrentLocation mMyLocation;
	private final static int TIMER_PERIOD = 60000;
	private Handler handler = new Handler();
	private Timer timer;
	private Timer timerGPS;
	AlertDialog alertDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_list);

		setupHeaderLayoutView();

		ids.add("030");
		ids.add("010"); 
		ids.add("020"); 
//		ids.add("021");
		ids.add("040"); 
		
		idsOthers.add("060"); 
		idsOthers.add("070"); 
		idsOthers.add("080"); 
		
		
//		idsAll.add("000");
		idsAll.add("010");
		idsAll.add("020");
		idsAll.add("021");
		idsAll.add("030");
		idsAll.add("060");
		idsAll.add("070");
		idsAll.add("080");
		
		mAll = DatabaseManager.getInstance().getDeliveryOrderForRouteWithStatus(idsAll);
		
		mOrderStatusInfos = DatabaseManager.getInstance().getStatusForWithIds(ids);
		
		// sort status.
		for (int i = 0; i < mOrderStatusInfos.size(); i++) {
			if (mOrderStatusInfos.get(i).getStatusId().equalsIgnoreCase(EStatus_CD.On_Delivery.getValue())) {
				OrderStatusInfo statusInfo = mOrderStatusInfos.remove(i);
				mOrderStatusInfos.add(0, statusInfo);
			}
		}
		
		OrderStatusInfo order1 = new OrderStatusInfo();
		OrderStatusInfo order2 = new OrderStatusInfo();
		
		order1.setStatusId(EStatus_CD.Others.getValue());
		order1.setStatusName(getString(R.string.status_id_other));
		order2.setStatusId(EStatus_CD.All.getValue());
		order2.setStatusName(getString(R.string.status_id_all));
		
		mOrderStatusInfos.add(order1);
		mOrderStatusInfos.add(order2);
		
		// set title screen
		((TextView) findViewById(R.id.title_screen)).setText(R.string.title_display_route);
		mListView = (ListView) findViewById(R.id.listview_display_route);
		mDeliveryStatusSpinner = (Spinner) findViewById(R.id.delivery_status_spinner);

		mAdapter = new RouteAdapter(this, mRouteList);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				openRouteDetail(position);
			}

		});
		mListView.setAdapter(mAdapter);

		// Create an ArrayAdapter using the string array and a default spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				convertToArrayString(mOrderStatusInfos));

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mDeliveryStatusSpinner.setAdapter(adapter);

		updateSummay();
		
		mDeliveryStatusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				
				mSelectedPosition = position;
				loadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		mMyLocation = new CurrentLocation(this);
        Runnable runnable = new Runnable() {
        	@Override
        	public void run() {
        		mHandler.post(new Runnable() {
					@Override
					public void run() {
						Location location = mMyLocation.getMyLocation();
						Log.i(TAG, "Your location is = {" + location.getLatitude() + ", " + location.getLongitude() + "}");
					}
				});
        	}
        };
        mMyLocation.runOnFirstFix(runnable);
        mMyLocation.runOnLocationUpdate(runnable);
        mMyLocation.enableMyLocation();
        
	}
	
	private void timerGPSMethod () {
		//GPS
		if (TMSConstrans.mAuto_gps_flag) {

			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat(Def.FORMAT_TIME);
				Date startDate = sdf.parse(TMSConstrans.mStart_time_gps);
				Date endDate = sdf.parse(TMSConstrans.mEnd_time_gps);
				Date currDate = sdf.parse(DateTimeUtils.getTime(DateTimeUtils.getSystemTime()));
				
				if (!currDate.before(startDate) && !currDate.after(endDate)) {
					ArrayList<OrderInfo> list = DatabaseManager.getInstance().getAllDeliveryOrder();
					double   longitude = 0;
					double   latitude = 0;
					if (list.size() == 0) {
						return;
					}
					 Location location =  mMyLocation.getMyLocation();
					 if (location != null) {
						 longitude = location.getLongitude();
						 latitude = location.getLatitude();
					 }
					    
					final String deviceIP= NetworkUtils.getIPAddress(true);
					final String organizationCode = TMSConstrans.mSoshiki_cd;
					final String informationConfidenceLevel = "1";
					final String deliveryDate = list.get(0).getDeliveryDate();
					final String presetCode = list.get(0).getPresetCode();
					final String presetName = "";
					final String deliverySetCode = list.get(0).getDeliverySetCode();
					final String deliverySetName = "";
					final String routeCode = "";
					final String routeName = "";
					final String driverRegisteredDateTime = DateTimeUtils.getSystemTime();
					final String registeredDriverCode = UserInfoPref.getPrefUserCode(this);
					
					final double latitude2 = latitude;
					final double longitude2 = longitude;

					final Send mSend = new Send(this);
					new Thread(new Runnable() {
						@Override
						public void run() {
							if (NetworkUtils.checkNetworkAvailable(getBaseContext())){
								mSend.requestToSendGPSData(organizationCode, latitude2, longitude2, deviceIP, informationConfidenceLevel, deliveryDate, presetCode, presetName, deliverySetCode, deliverySetName, routeCode, routeName, driverRegisteredDateTime, registeredDriverCode);
							}
						}
					}).start();
					
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void timerMethod () {

		String statusID = mOrderStatusInfos.get(mSelectedPosition).getStatusId();
		
		if (mRouteList.size() > 0 && statusID.equalsIgnoreCase(EStatus_CD.On_Delivery.getValue())) {
			
			//distance_flg
			if (TMSConstrans.mDistance_flag && TMSConstrans.SHUTSUGEN_KYORI != 0) {
				
				double distance = GoogleMapUtils.getDistance(this, mMyLocation.getMyLocation(), mRouteList.get(0).getCustomerAddress());
				Log.i(TAG, "distance = " + distance);
				if (TMSConstrans.SHUTSUGEN_KYORI > distance) {
					
					openRouteDetail(0);
				}
			}
			
			boolean alertFlg = false;
			
			//alert_flg
			for (int i=0; i < mRouteList.size(); i++) {
				
				String estimatedTime = mRouteList.get(i).getEstimatedDeliveryTime();
				
				if (estimatedTime != null) {
					
					try {
					
						Date dtEst = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.JAPANESE)).parse(estimatedTime);
						Date dtNow = new Date(System.currentTimeMillis() - 1000 * 60 * 30 );
						
						if (dtNow.compareTo(dtEst) > 0) {
							
							((OrderInfo)mAdapter.getItem(i)).setLateFlag(true);
							
							if (TMSConstrans.mAlert_flag) {
								alertFlg = true;
							}
							
						} else {
							
							((OrderInfo)mAdapter.getItem(i)).setLateFlag(false);
						}
					
					} catch (Exception e) {
						
					}
				}
					
				
			}
			
			if (alertFlg) {
				
				if (alertDialog == null || !alertDialog.isShowing()) {
				
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			        alertDialogBuilder.setTitle("åxçê");
			        alertDialogBuilder.setMessage("åªç›éûçèÇ™ÅAó\íËîzíBéûä‘Çí¥Ç¶ÇƒÇ¢Ç‹Ç∑ÅB");
			        alertDialogBuilder.setCancelable(true);
			        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int id) {
			            	alertDialog.dismiss();
			            }
			        });
			        alertDialog = alertDialogBuilder.create();
			        alertDialog.show();
				}
			}
			
			mAdapter.notifyDataSetChanged();
		}
		
	}
	
	private void openRouteDetail (int position) {
		Intent i = new Intent(RouteListActivity.this,
				RouteDetailActivity.class);
		i.putExtra(Def.TAG_BUNDLE_BATCH_CODE, mRouteList.get(position).getBundleBatchCode());
		i.putExtra(Def.TAG_DELIVERY_BATCH_CODE, mRouteList.get(position).getDeliveryBatchCode());
		i.putExtra(Def.TAG_STATUS_ID, mRouteList.get(position).getStatusId());
		i.putExtra(Def.TAG_POSITION, position);
		i.putExtra(Def.TAG_PULLDOWN, mOrderStatusInfos.get(mSelectedPosition).getStatusId());
		startActivity(i);
	}
	
	private boolean isExist(String statusID) {
		for (int i = 0; i < ids.size(); i++) {
			if (ids.get(i).equalsIgnoreCase(statusID)) {
				return true;
			}
		}
		return false;
	}
	
	private void loadData() {
		ArrayList<String> status = new ArrayList<String>();
		String statusID = mOrderStatusInfos.get(mSelectedPosition).getStatusId();
		status.add(statusID);
		
		if (statusID.equalsIgnoreCase(EStatus_CD.Under_Investigation.getValue())) {
			mRouteList = DatabaseManager.getInstance().getDeliveryOrderForRouteDontCarryOut(status);
		} else if (isExist(statusID)) {
			mRouteList = DatabaseManager.getInstance().getDeliveryOrderForRouteWithStatus(status);
		} else if (statusID.equalsIgnoreCase(EStatus_CD.Others.getValue())) {
			mRouteList = DatabaseManager.getInstance().getDeliveryOrderForRouteWithStatus(idsOthers);
		} else if (statusID.equalsIgnoreCase(EStatus_CD.All.getValue())) {
			mRouteList = DatabaseManager.getInstance().getDeliveryOrderForRouteWithStatus(idsAll);
		}
		
//		mRouteList = DatabaseManager.getInstance().getAllDeliveryOrder();
		
		
		for (int i = 0; i < mRouteList.size(); i++) {
			String customerCD = mRouteList.get(i).getCustomerCD();
			ArrayList<String> idList = new ArrayList<String>();
			idList.add(mRouteList.get(i).getStatusId());
			ArrayList<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
			
			if (statusID.equalsIgnoreCase(EStatus_CD.Under_Investigation.getValue())) {
				orderInfos = DatabaseManager.getInstance().getDataAssignedToDriverDontCarryOut(
						mRouteList.get(i).getBundleBatchCode());
			} else if (statusID.equalsIgnoreCase(EStatus_CD.All.getValue())) {
				orderInfos = DatabaseManager.getInstance().getDataAssignedToDriverAll(
						mRouteList.get(i).getBundleBatchCode());
			} else {
				orderInfos = DatabaseManager.getInstance().getDataAssignedToDriver(
						mRouteList.get(i).getBundleBatchCode());
			}
			
			mRouteList.get(i).setPackageQuantity(String.valueOf(countPackageQuantity(orderInfos)));
			
			ArrayList<OrderStatusInfo> statusList = DatabaseManager.getInstance().getStatusForWithIds(idList);
			ArrayList<CustomerInfo> cusList = DatabaseManager.getInstance().getCustomerInfoById(customerCD);
			
			if (cusList.size() > 0) {
				mRouteList.get(i).setmMessage(cusList.get(0).getMessage());
			}
			if (idList.size() > 0) {
				mRouteList.get(i).setStatusName(statusList.get(0).getStatusName());
			}
		}
		
		if (mRouteList.size() > 0) {
			((TextView)findViewById(R.id.tv_estimated_time)).setText(
					DateTimeUtils.getTime(mRouteList.get(mRouteList.size()-1).getEstimatedDeliveryTime()));
		}
		
		mAdapter.setmItemList(mRouteList);
		mAdapter.notifyDataSetChanged();
		
		updateSummay();

	}
	
	private int countPackageQuantity(ArrayList<OrderInfo> orderInfos) {
		int count = 0;
		for (int i = 0; i < orderInfos.size(); i++) {
			count += Integer.valueOf(orderInfos.get(i).getPackageQuantity());
		}
		
		return count;
	}
	
	@Override
	protected void notifiyChange() {
		super.notifiyChange();
		
		loadData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		notifiyChange();
		mMyLocation.enableMyLocation();
		
		timer = new Timer(false);

        timer.schedule(new TimerTask() {
        	@Override
        	public void run() {

        		handler.post(new Runnable() {
        			@Override
        			public void run() {
        				timerMethod();
        			}
        		});
        	}
        }, 1000, TIMER_PERIOD);
        
        if (TMSConstrans.mAuto_gps_flag) {
        	
        	timerGPS = new Timer(false);
        	
            timerGPS.schedule(new TimerTask() {
            	@Override
            	public void run() {

            		handler.post(new Runnable() {
            			@Override
            			public void run() {
            				timerGPSMethod();
            			}
            		});
            	}
            }, 1000, TMSConstrans.mAuto_gps_interval);
        }
	}
	
	private void updateSummay() {
		((TextView) findViewById(R.id.tv_total_remaining))
		.setText(getResources().getString(R.string.total_remaining,
//				countDeliveredRoutes(),
				String.valueOf(mRouteList.size())));
	}

	/*
	 * convert driver list to array string
	 */
	private ArrayList<String> convertToArrayString(
			ArrayList<OrderStatusInfo> orderStatusInfos) {
		ArrayList<String> statusInfos = new ArrayList<String>();
		for (int i = 0; i < orderStatusInfos.size(); i++) {
			statusInfos.add(orderStatusInfos.get(i).getStatusName());
		}

		return statusInfos;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	
	@Override
	protected void onPause() {
		timer.cancel();
		timer.purge();
		
		if (TMSConstrans.mAuto_gps_flag) {	
			timerGPS.cancel();
			timerGPS.purge();	
		}
		
		mMyLocation.disableMyLocation();
		super.onPause();
	}
}
