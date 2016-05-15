package jp.co.isb.tms.ui;

import java.util.ArrayList;

import jp.co.isb.tms.ECarryOutStatus;
import jp.co.isb.tms.EStatus_CD;
import jp.co.isb.tms.R;
import jp.co.isb.tms.common.ConfirmEnableNetworkDialog;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.dialog.ConfirmDialog;
import jp.co.isb.tms.dialog.OnConfirmListener;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.server.Send;
import jp.co.isb.tms.util.DateTimeUtils;
import jp.co.isb.tms.util.NetworkUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class RouteUpdateEstimatedTimeActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = RouteUpdateEstimatedTimeActivity.class.getSimpleName();
	private Button  mUpdateEstimatedTimeButton;
	private TextView mtvEstimatedDeliveryTime;
	private TimePicker mtpEstimatedDeliveryTime;
	
	private String mDeliveryEstimatedDateTime = "";
	private String mDeliveryBatchCode = "";
	private int mPostionSelected = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_update_estimate_time);
		
		mDeliveryBatchCode = getIntent().getStringExtra(Def.TAG_DELIVERY_BATCH_CODE);
		mDeliveryEstimatedDateTime = getIntent().getStringExtra(Def.TAG_DELIVERY_ESTIMATED_DATE_TIME);
		mPostionSelected = getIntent().getIntExtra(Def.TAG_POSITION, 0);
		
		setupHeaderLayoutView();
		
		// set title screen
		((TextView) findViewById(R.id.title_screen)).setText(R.string.title_detail_display);

		mUpdateEstimatedTimeButton = (Button) findViewById(R.id.btn_update_estimated_time);
		mtvEstimatedDeliveryTime = (TextView) findViewById(R.id.tv_estimated_delivery_time);
		mtpEstimatedDeliveryTime = (TimePicker) findViewById(R.id.tp_estimated_delivery_time);
		
		mUpdateEstimatedTimeButton.setOnClickListener(this);
		String dTime = DateTimeUtils.getTime(mDeliveryEstimatedDateTime);
		mtvEstimatedDeliveryTime.setText(dTime);
	}
	
	private OnConfirmListener onConfirm = new OnConfirmListener() {
		
		@Override
		public void onYes() {
			updateDeliverEstimatedTime(mtpEstimatedDeliveryTime);
			
			finish();
		}
		
		@Override
		public void onNo() {
			finish();
		}
	};
	
	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_update_estimated_time:
			String title = getResources().getString(R.string.alert_dialog_confirm_update_time);
			
			ConfirmDialog dialog = new ConfirmDialog(this, title, onConfirm);
			dialog.show();
			
			break;
		default:
			break;
		}

	}
	
	/*
	 * Update Delivery Estimated Time
	 */
	private void updateDeliverEstimatedTime(TimePicker tp){
		
		if (!NetworkUtils.checkNetworkAvailable(getBaseContext())){
			new ConfirmEnableNetworkDialog(this).show();
			return;
		}
		
		ArrayList<OrderInfo> mListOrderInfos = null;
		
		ArrayList<String> listStatusCds = new ArrayList<String>();
		listStatusCds.add(EStatus_CD.On_Delivery.getValue()); // Delivering
		
		mListOrderInfos = DatabaseManager.getInstance().getDeliveryOrderWithStatus(listStatusCds);
		int hours = tp.getCurrentHour();
		int minutes  = tp.getCurrentMinute() - TMSConstrans.mAverageDeliveryInterval;
		String tmpBundledCode = "";
		
		for(int i = mPostionSelected; i < mListOrderInfos.size(); i++){
			
			OrderInfo orderInfo = mListOrderInfos.get(i);
			
			
			if (!tmpBundledCode.equals(mListOrderInfos.get(i).getBundleBatchCode())) {

				minutes += TMSConstrans.mAverageDeliveryInterval;
				
				hours += minutes / Def.DEFAULT_MINUTE;
				minutes = minutes % Def.DEFAULT_MINUTE;	
			}
			
			tmpBundledCode = mListOrderInfos.get(i).getBundleBatchCode();
			
			String time =  hours + ":" + minutes; 

			// Set delivery completed time = null
			String[] date = DateTimeUtils.getSystemTime().split(" ");
			orderInfo.setEstimatedDeliveryTime(date[0] + " " + time + ":00");
			orderInfo.setDeliveryCompletedDateTime("");
		
			// Update modified time and driver who updated
			orderInfo.setDriverUpdatedDateTime(DateTimeUtils.getSystemTime());
			orderInfo.setDriverCode(TMSConstrans.mDriver_code);
			
			Log.d(TAG, "time: " + orderInfo.getEstimatedDeliveryTime());
			// Update local
			DatabaseManager.getInstance().createOrUpdateOrderItem(orderInfo);
			final Send mSend = new Send(this);
			// Update via API
			final OrderInfo orderInfo2 = orderInfo;
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					mSend.requestToUpdateDelvieryEstimatedTime(orderInfo2);
				}
			}).start();
			
		}
	}

}
