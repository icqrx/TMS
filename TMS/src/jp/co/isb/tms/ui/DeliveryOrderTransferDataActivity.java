package jp.co.isb.tms.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.co.isb.tms.R;
import jp.co.isb.tms.common.ConfirmEnableNetworkDialog;
import jp.co.isb.tms.common.ConfirmUnknownErrorDialog;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.IGetAndroidStatus;
import jp.co.isb.tms.common.IGetTransferResult;
import jp.co.isb.tms.common.INotifyChange;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.common.UserInfoPref;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.model.DeliveryOrderSummaryInfo;
import jp.co.isb.tms.model.DriverInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.server.Receive;
import jp.co.isb.tms.server.Send;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DeliveryOrderTransferDataActivity extends BaseActivity implements OnClickListener, INotifyChange {
	
	private static final String TAG = DeliveryOrderTransferDataActivity.class.getSimpleName();
	private Button mBtnCancel;
	private Button mBtnTransfer;
	private Spinner mSpin;
	private ArrayList<DriverInfo> mDriverInfos;
	private String mDeliveryBatchCode;
	
	private OrderInfo mOrderItemInfo;
	private DeliveryOrderSummaryInfo mSummaryInfo;
	private ProgressDialog progressDlg;
	private Handler mHandler;
	private boolean mAndroidStatus;
	private boolean mIsTranfered;
	private String mDeliverySetCode;
	private boolean isRequestAndroid = false;
	
	private Dialog dialog;
	private Button no, yes;
	
	private IGetAndroidStatus iAndroidStatus = new IGetAndroidStatus() {

		@Override
		public void sendAndroidStatus(boolean androidStatus) {
			mAndroidStatus = androidStatus;
		}

		@Override
		public void sendDeliverySetCode(String aDeliverySetCode) {
			mDeliverySetCode = aDeliverySetCode;
		}
	};
	
	private IGetTransferResult iGetTransferResult = new IGetTransferResult() {
		
		@Override
		public void sendTransferResult(boolean bResult) {
			mIsTranfered = bResult;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_order_transfer_data);
		
		Intent i = getIntent();
		mDeliveryBatchCode = (String) i.getStringExtra(Def.TAG_DELIVERY_BATCH_CODE);
		mOrderItemInfo = DatabaseManager.getInstance().getDeliveryBatchCode(mDeliveryBatchCode).get(0);
		mSummaryInfo = (DeliveryOrderSummaryInfo) getIntent().getParcelableExtra(Def.TAG_SUMMARY);
		
		setupHeaderLayoutView();
		
		// get driver list	from local database
		mDriverInfos = DatabaseManager.getInstance().getAllDriverInfo();
		for (int j = 0; j < mDriverInfos.size(); j++) {
			if (mDriverInfos.get(j).getDriverId().equals(UserInfoPref.getPrefUserCode(this))) {
				DatabaseManager.getInstance().deleteDriverItem(mDriverInfos.get(j));
				break;
			}
		}
		
		// set title screen
		((TextView) findViewById(R.id.title_screen)).setText(R.string.title_delivery_other_settings);

		mBtnCancel = (Button) findViewById(R.id.btn_cancel);
		mBtnTransfer = (Button) findViewById(R.id.btn_transfer);
		mSpin = (Spinner) findViewById(R.id.drivers_spinner);
		
		mBtnCancel.setOnClickListener(this);
		mBtnTransfer.setOnClickListener(this);

		// create adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, convertToArrayString(mDriverInfos));

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mSpin.setAdapter(adapter);

		
		displayInfomation();
		
		mHandler = new Handler() {
			private int mStatus;

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case Def.MESSAGE_DELAY: {
						showProgressDialg(false);
						if (mStatus == Def.SERVER_ERROR) {
							new ConfirmEnableNetworkDialog(DeliveryOrderTransferDataActivity.this).show();
						}
						break;
					}
					default: 
						break;
				}
				super.handleMessage(msg);
			}
		};	
	}
	
	private void displayInfomation() {
		
		((LinearLayout) findViewById(R.id.ll_note)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.ll_memo)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.ll_condition_in_front_of_house)).setVisibility(View.GONE);
		
		if (mSummaryInfo != null) {
			((TextView) findViewById(R.id.tv_houses)).setText(mSummaryInfo.getHouses());
			((TextView) findViewById(R.id.tv_packages)).setText(mSummaryInfo.getPackages());
			((TextView) findViewById(R.id.tv_weight)).setText(
					Math.round(Float.valueOf(mSummaryInfo.getWeight()) * 10) / 10.0 +  getString(R.string.unit));
			((TextView) findViewById(R.id.tv_capacity)).setText(
					Math.round(Float.valueOf(mSummaryInfo.getCapacity()) * 100) / 100.0 + getString(R.string.kg));

		}
		
		if (mOrderItemInfo != null) {
			((TextView) findViewById(R.id.tv_tracking_id)).setText(mOrderItemInfo.getTrackingId());
			((TextView) findViewById(R.id.tv_order_id)).setText(mOrderItemInfo.getOrderId());
			((TextView) findViewById(R.id.tv_shipper)).setText(mOrderItemInfo.getShipperCode());
			((TextView) findViewById(R.id.tv_shipper_name)).setText(mOrderItemInfo.getShipperName());
		/*	
		 * Edit by ptlinh
		 * @date 20131209
		 */
			// Using japanese for year, month, day
			Date normalDate = null;
			try {
				normalDate = new SimpleDateFormat(Def.FORMAT_DATE).parse(mOrderItemInfo.getFirstDeliveryDate());
			} catch (ParseException e) {
				Log.e(TAG, "" +  e.toString());
			}
			SimpleDateFormat japanDate = new SimpleDateFormat("yyyy" + getString(R.string.year) + "MM" + getString(R.string.month) + "dd" + getString(R.string.day));
			((TextView) findViewById(R.id.tv_first_delivery_date)).setText(japanDate.format(normalDate));
			// This text view is removed from layout_order_details
			// ((TextView) findViewById(R.id.tv_estimated_delivery_time)).setText(mOrderItemInfo.getEstimatedDeliveryTime());
		/*	
		 * End edit
		 */
			((TextView) findViewById(R.id.tv_receiver_address)).setText(mOrderItemInfo.getReceiverAdress());
			((TextView) findViewById(R.id.tv_receiver_name)).setText(mOrderItemInfo.getReceiverName());
			((TextView) findViewById(R.id.tv_receiver_department_name)).setText(mOrderItemInfo.getDepartmentName());
			((TextView) findViewById(R.id.tv_person_in_charge_of_receiver_side)).setText(mOrderItemInfo.getPersonInChargeOfReceiverSide());
			((TextView) findViewById(R.id.tv_package)).setText(mOrderItemInfo.getPackageQuantity());
//			((TextView) findViewById(R.id.tv_note)).setText(mOrderItemInfo.getNote());
			((TextView) findViewById(R.id.tv_tel)).setText(mOrderItemInfo.getCustomerTEL1());
//			((TextView) findViewById(R.id.tv_memo)).setText(mOrderItemInfo.getMemo());
//			((TextView) findViewById(R.id.tv_condition_in_front_of_house)).setText(mOrderItemInfo.getConditionInFrontOfHouse());
		}
	}
	
	/*
	 * convert driver list to array string
	 */
	private ArrayList<String> convertToArrayString(ArrayList<DriverInfo> drivers) {
		ArrayList<String> driver = new ArrayList<String>();
		for (int i = 0; i < drivers.size(); i++) {
			driver.add(drivers.get(i).getDriverFullName());
		}
		
		return driver;
	}
	
	private void postExecute(int status) {
		if (status != Def.SERVER_ERROR && !isRequestAndroid) {
			showProgressDialg(false);
			if (mHandler != null) {
				mHandler.removeMessages(Def.MESSAGE_DELAY);
			}
		}
		switch (status) {
			case Def.OK: 	
				if (isRequestAndroid) {
					if (mAndroidStatus) {
						Log.d(TAG, "Get Android status OK");
						tranfer();						
					} else {
						showProgressDialg(false);
						confirmRetryDialog();						
					}
					isRequestAndroid = false;
				} else {
					Log.d(TAG, "Transfer OK");
					finish();
					if (mIsTranfered) {
						int result = DatabaseManager.getInstance().deleteOrderItem(mOrderItemInfo);
						Log.d(TAG, "delete object result = " + result);
						Intent i = new Intent(DeliveryOrderTransferDataActivity.this, DeliveryOrderListActivity.class);
						startActivity(i);
					} else {
						showProgressDialg(false);
						confirmRetryDialog();	
					}
					mAndroidStatus = false;
				}
				break;
			
			case Def.FAIL: 
				new ConfirmEnableNetworkDialog(this).show();
				break;
				
			default:
				showProgressDialg(false);
				new ConfirmUnknownErrorDialog(this).show();
				break;
		}
	}
	
	private void getAndroidStatus() {
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(2*Def.MESSAGE_DELAY, Def.TIME_OUT);
		}
		isRequestAndroid = true;
		String driverId = mDriverInfos.get(mSpin.getSelectedItemPosition()).getDriverId();
		Log.d(TAG, "driverId = " + driverId);
		new Receive.ReceiveObject(Def.HTTP_ANDROID_STATUS,
				DeliveryOrderTransferDataActivity.this, iAndroidStatus,
				UserInfoPref.getPrefUserCode(this), UserInfoPref.getPrefAccessToken(this),
				TMSConstrans.mSoshiki_cd, driverId).execute();
	}
	
	private void tranfer() {
		new Send.SendObject(Def.HTTP_TRANSFER, DeliveryOrderTransferDataActivity.this,
				iGetTransferResult,
				UserInfoPref.getPrefUserCode(this),
				UserInfoPref.getPrefAccessToken(this),
				TMSConstrans.mSoshiki_cd,
				mOrderItemInfo.getDeliveryBatchCode(),
				mDeliverySetCode).execute();
	}
	
	private void confirmRetryDialog(){
		dialog = new Dialog(this, R.style.FullHeightDialog);
		 //this is a reference to the style above
		dialog.setContentView(R.layout.custom_dialog); 
		dialog.setCancelable(true);
		
		//to set the message
		TextView message =(TextView) dialog.findViewById(R.id.tvmessagedialogtext);
		message.setText(getResources().getString(R.string.text_retry_android_status));
		 
		//add some action to the buttons
        yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
        yes.setText(getResources().getString(R.string.alert_dialog_yes));
        yes.setOnClickListener(new OnClickListener() {
             
            public void onClick(View v) {
                 dialog.dismiss();
                 getAndroidStatus();
            }
        });
         
        no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
        no.setOnClickListener(new OnClickListener() {
             
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;			
		case R.id.btn_transfer:
			getAndroidStatus();
			break;
		default:
			break;
		}
	}

	@Override
	public void notify(int status) {
		
		switch (status) {
			case 0: 
				if (!mAndroidStatus) {
					showProgressDialg(true);
				}
				break;
			default: 
				postExecute(status);
				break;
		}
	}
	
	private void showProgressDialg(boolean show) {
		if (progressDlg != null) {
			progressDlg.dismiss();
			progressDlg = null;
		}
		if (progressDlg == null) {
			progressDlg = new ProgressDialog(this);
			progressDlg.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
			progressDlg.setMessage(getResources().getString(R.string.alert_dbatchs_transfer));
		}
		if (show) {
			progressDlg.show();
		} else {
			progressDlg.dismiss();
			progressDlg = null;
		}
	}
}
