package jp.co.isb.tms.ui;

import java.util.ArrayList;

import jp.co.isb.tms.ECarryOutStatus;
import jp.co.isb.tms.EStatus_CD;
import jp.co.isb.tms.R;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.StoragePref;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.dialog.ConfirmDialog;
import jp.co.isb.tms.dialog.OnConfirmListener;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.server.Send;
import jp.co.isb.tms.util.DateTimeUtils;
import jp.co.isb.tms.util.NetworkUtils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class CarryOutTimeSettingsActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = CarryOutTimeSettingsActivity.class.getSimpleName();
	private Spinner mAverSpin;
	private Button mRegister;
	private Send mSend;
	private TimePicker timepPicker;
	private ProgressDialog progressDlg;
	
	private int mAverageDeliveryInterval = Def.DEFAULT_VALUE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carry_out_time_settings);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		timepPicker  = (TimePicker)findViewById(R.id.timePicker1);
		mSend = new Send(getBaseContext());
		
		setupHeaderLayoutView();
		
		// set title screen
		((TextView) findViewById(R.id.title_screen))
				.setText(R.string.title_time_settings);

		mAverSpin = (Spinner) findViewById(R.id.average_delivery_interval_spinner);
		mRegister = (Button) findViewById(R.id.btn_register);

		// Create an ArrayAdapter using the string array and a default spinner
		// layout

		String[] minutes = new String[Def.DEFAULT_MINUTE];
		for (int i = 0; i < Def.DEFAULT_MINUTE; i++) {
			minutes[i] = String.valueOf(i);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, minutes);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mAverSpin.setAdapter(adapter);
		mAverSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int postion, long arg3) {
				mAverageDeliveryInterval = Integer.valueOf((String) mAverSpin.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		mAverSpin.setSelection(Def.DEFAULT_VALUE);
		mRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_register:
			registerTimeSetting();	
			break;

		default:
			break;
		}

	}

	/*
	 * register Time setting to server
	 */
	public void registerTimeSetting() {
		ConfirmDialog dialog = new ConfirmDialog(this, getResources().getString(R.string.alert_dialog_confirm_register),
				new OnConfirmListener() {
			
			@Override
			public void onYes() {
				// 2. Communication Check
				if (!NetworkUtils.checkNetworkAvailable(getBaseContext())){
					// show confirm dialog
						} else {
							// 3. Check whether the corresponding Delivery Batch
							// Code is updated or not via API
							showProgressDialg(true);
							
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									updateDeliverEstimatedTime(timepPicker);
//									// 5. Update Driver Master
									updateDriverDeliveryAverageInterval();
									
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											showProgressDialg(false);
										}
									});
									
									gotoMenuScreen();
								}
							}).start();
							
						}
					}

					@Override
					public void onNo() {

			}
		});
		
		dialog.show();
	}
	
	public void showProgressDialg(boolean show) {
		if (progressDlg != null) {
			progressDlg.dismiss();
			progressDlg = null;
		}
		if (progressDlg == null) {
			progressDlg = new ProgressDialog(this);
			progressDlg.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
			progressDlg.setMessage(getResources().getString(R.string.alert_loading));
		}
		if (show) {
			progressDlg.show();
		} else {
			progressDlg.dismiss();
			progressDlg = null;
		}
	}
	/*
	 * Update Delivery Estimated Time
	 */
	private void updateDeliverEstimatedTime(TimePicker tp){
		ArrayList<OrderInfo> mListOrderInfos = null;
		
		ArrayList<String> listStatusCds = new ArrayList<String>();
		listStatusCds.add("000");
		listStatusCds.add("030");
		listStatusCds.add("040");
		listStatusCds.add("050");

		mListOrderInfos = DatabaseManager.getInstance().getDeliveryOrderWithStatus(listStatusCds);
		int hours = tp.getCurrentHour();
		int minutes  = tp.getCurrentMinute();
		String tmpBundledCode = "";
		boolean firstFlag = true;
		
		for(int i = 0; i < mListOrderInfos.size(); i++){
			
			if (mListOrderInfos.get(i).getCarryOutCheckFlag().equals(ECarryOutStatus.YES.getValue())) {
				
				OrderInfo orderInfo = mListOrderInfos.get(i);
				
				if (!tmpBundledCode.equals(mListOrderInfos.get(i).getBundleBatchCode())) {
					if (firstFlag == false) {
						minutes += mAverageDeliveryInterval;
						
						hours += minutes / Def.DEFAULT_MINUTE;
						minutes = minutes % Def.DEFAULT_MINUTE;	
					}
					firstFlag = false;
				}
				
				tmpBundledCode = mListOrderInfos.get(i).getBundleBatchCode();
				
				String time =  hours + ":" + minutes; 

				// Set delivery completed time = null
				String[] date = DateTimeUtils.getSystemTime().split(" ");
				orderInfo.setEstimatedDeliveryTime(date[0] + " " + time + ":00");
				orderInfo.setDeliveryCompletedDateTime("");
				
				if (!orderInfo.mStatusId.equals(EStatus_CD.On_Delivery.getValue())) {
					int currentDeliveryTimes = Integer.parseInt(orderInfo.getDeliveredTimes());
					Log.d(TAG, currentDeliveryTimes + "");
					orderInfo.setDeliveredTimes(Integer.toString(currentDeliveryTimes + 1));
				}

				orderInfo.setCarryOutCheckFlag(ECarryOutStatus.YES.getValue());
				orderInfo.mStatusId = EStatus_CD.On_Delivery.getValue();
			
				// Update modified time and driver who updated
				orderInfo.setDriverUpdatedDateTime(DateTimeUtils.getSystemTime());
				orderInfo.setDriverCode(TMSConstrans.mDriver_code);
				
				// Update local
				DatabaseManager.getInstance().createOrUpdateOrderItem(orderInfo);
				
				// Update via API
				mSend.requestToUpdateDelvieryEstimatedTime(orderInfo);
				
			} else if (mListOrderInfos.get(i).getCarryOutStatusCd() != null) {
				
				OrderInfo orderInfo = mListOrderInfos.get(i);

				// Set delivery completed time = null
				orderInfo.setEstimatedDeliveryTime("");
				orderInfo.setDeliveryCompletedDateTime("");
				

				orderInfo.setCarryOutCheckFlag(ECarryOutStatus.NO.getValue());
				orderInfo.mStatusId = EStatus_CD.Under_Investigation.getValue();
			
				// Update modified time and driver who updated
				orderInfo.setDriverUpdatedDateTime(DateTimeUtils.getSystemTime());
				orderInfo.setDriverCode(TMSConstrans.mDriver_code);
				
				// Update local
				DatabaseManager.getInstance().createOrUpdateOrderItem(orderInfo);
				
				// Update via API
				mSend.requestToUpdateDelvieryEstimatedTime(orderInfo);
				
			} else {
				OrderInfo orderInfo = mListOrderInfos.get(i);
				orderInfo.setCarryOutCheckFlag(ECarryOutStatus.NO.getValue());
				orderInfo.setEstimatedDeliveryTime("");
				orderInfo.setDeliveryCompletedDateTime("");
				
				mSend.requestToUpdateDelvieryEstimatedTime(orderInfo);
			}
			
			
		}
		
	}
	
	/*
	 * Update Driver Delivery Average Interval
	 */
	private void updateDriverDeliveryAverageInterval(){
		String averTime = (String) mAverSpin.getSelectedItem();
		TMSConstrans.mAverageDeliveryInterval = Integer.valueOf(averTime);
		StoragePref.saveAverageDeliveryInterval(this);
		mSend.requestToUpdateDriverDeliveryAverageIntervalTime(TMSConstrans.mSoshiki_cd, averTime);
	}
}
