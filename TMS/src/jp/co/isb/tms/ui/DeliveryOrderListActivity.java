package jp.co.isb.tms.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.co.isb.tms.DeliveryOrderAdapter;
import jp.co.isb.tms.ECarryOutStatus;
import jp.co.isb.tms.R;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.DeliveryOrderSummaryInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.server.Send;
import jp.co.isb.tms.util.DateTimeUtils;
import jp.co.isb.tms.util.NetworkUtils;
import jp.co.isb.tms.util.NfcUtils;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortListView;

public class DeliveryOrderListActivity extends BaseActivity implements
		OnClickListener/*, INotifyChange */{
	protected static final String TAG = DeliveryOrderListActivity.class
			.getSimpleName();

	private LinearLayout mListModeLayout;
	private LinearLayout mEditModeLayout;

	private Button mBtnFinishEdit;
	private Button mBtnEditMode;
	private Button mBtnTransferToAnotherDriver;
	private boolean isEditMode = true;

	private DragSortListView mListView;
	private DeliveryOrderAdapter mAdapter;
	private ArrayList<OrderInfo> mDeliveryOrderList = new ArrayList<OrderInfo>();
	private DeliveryOrderSummaryInfo mSummaryInfo = new DeliveryOrderSummaryInfo();
	private Dialog dialog;
	private Button no, yes;
	private ArrayList<String> status = new ArrayList<String>();
	private CustomDialogClass cdd = null;
	private ProgressDialog progressDlg;

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			Log.d(TAG, "drop: from" + from + ", to: " + to);
			if (from != to) {

				OrderInfo item = (OrderInfo) mAdapter.getItem(from);
				mAdapter.removeItem(item);
				mAdapter.insertItem(item, to);

				Log.d(TAG, "from to: " + from + " : " + to);
			}
			mAdapter.setItemSelected(to);
			mAdapter.notifyDataSetChanged();
		}
	};

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_order_list);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setupHeaderLayoutView();
		// set title screen
		((TextView) findViewById(R.id.title_screen))
				.setText(R.string.title_delivery_other_settings);

		mBtnFinishEdit = (Button) findViewById(R.id.btn_finish_edit);
		mBtnEditMode = (Button) findViewById(R.id.btn_edit_mode);
		mBtnTransferToAnotherDriver = (Button) findViewById(R.id.btn_transfer_to_other_devices);

		mListModeLayout = (LinearLayout) findViewById(R.id.basic_mode_layout);
		mEditModeLayout = (LinearLayout) findViewById(R.id.edit_mode_layout);

		mBtnFinishEdit.setOnClickListener(this);
		mBtnEditMode.setOnClickListener(this);
		mBtnTransferToAnotherDriver.setOnClickListener(this);

		mListView = (DragSortListView) findViewById(android.R.id.list);

		status.add("000"); // un-delivered
		status.add("050"); // re-delivered
		status.add("030"); // delivering
		status.add("040"); 

		mDeliveryOrderList = DatabaseManager.getInstance()
				.getDeliveryOrderWithStatus(status);
		
		// get local data
		mSummaryInfo = calculateSummary(mDeliveryOrderList);

		displayDeliveryOrderSummaryInfo(mSummaryInfo);
		mAdapter = new DeliveryOrderAdapter(this, mDeliveryOrderList);

		mAdapter.setItemSelected(0);
		mListView.setAdapter(mAdapter);
		mListView.setDropListener(onDrop);
		
		switchToListMode();
		updateSortListInfo(isEditMode);
		
		//Set Message
		for (int i = 0; i < mDeliveryOrderList.size(); i++) {
			String customerCD = mDeliveryOrderList.get(i).getCustomerCD();
			ArrayList<String> idList = new ArrayList<String>();
			idList.add(mDeliveryOrderList.get(i).getStatusId());
			
			ArrayList<CustomerInfo> cusList = DatabaseManager.getInstance().getCustomerInfoById(customerCD);
			
			if (cusList.size() > 0) {
				mDeliveryOrderList.get(i).setmMessage(cusList.get(0).getMessage());
			}

		}

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int seletedPostion, long arg3) {
				Log.d(TAG, "onItemClick: " + seletedPostion);
				mAdapter.setItemSelected(seletedPostion);
				mAdapter.notifyDataSetChanged();

				if (!isEditMode) {
					OrderInfo mOrderInfo = mDeliveryOrderList
							.get(seletedPostion);

					ArrayList<CustomerInfo> cusList = DatabaseManager
							.getInstance().getCustomerInfoById(
									mOrderInfo.getCustomerCD());
					if (cusList.size() > 0) {
						mOrderInfo.setConditionInFrontOfHouse(cusList.get(0)
								.getConditionsInForntOfHouse());
					}

					cdd = new CustomDialogClass(
							DeliveryOrderListActivity.this, mOrderInfo);
					cdd.show();
				}
			}
		});
	}
	
	
	@Override
	protected void notifiyChange() {
		super.notifiyChange();
		
		mDeliveryOrderList = DatabaseManager.getInstance()
				.getDeliveryOrderWithStatus(status);
		
		mSummaryInfo = calculateSummary(mDeliveryOrderList);
		displayDeliveryOrderSummaryInfo(mSummaryInfo);
		
		Log.d(TAG, "isListMode: " + isEditMode);
		updateSortListInfo(isEditMode);
//		mAdapter.setmItemList(mDeliveryOrderList);
//		mAdapter.notifyDataSetChanged();
		
		if (cdd != null) {
			cdd.displayInfomation();
		}
	}

	/*
	 * Calculate Deliveries Statistic Info
	 * 
	 * @author: ptlinh
	 * 
	 * @date: 20131202
	 */
	private DeliveryOrderSummaryInfo calculateSummary(
			ArrayList<OrderInfo> mDeliveryOrderList) {
		DeliveryOrderSummaryInfo summary = new DeliveryOrderSummaryInfo();

		// Use this list to hold counted bundle batch code
		ArrayList<String> listBundleBatchCode = new ArrayList<String>();

		for (OrderInfo orderInfo : mDeliveryOrderList) {
			// Count unique Bundle batch code as houses
			if (!listBundleBatchCode.contains(orderInfo.getBundleBatchCode())) {
				summary.increaseHouses(1);
				listBundleBatchCode.add(orderInfo.getBundleBatchCode());
			}
			// Increase the others
			summary.increasePackages(Integer.parseInt(orderInfo
					.getPackageQuantity()));
			summary.increaseCapacity(Float.parseFloat(orderInfo
					.getSquareMeterOfPackage()));
			summary.increaseWeight(Float.parseFloat(orderInfo
					.getPackageWeight()));

		}

		return summary;
	}

	private void displayDeliveryOrderSummaryInfo(DeliveryOrderSummaryInfo info) {
		((TextView) findViewById(R.id.tv_houses)).setText(info.getHouses());
		((TextView) findViewById(R.id.tv_packages)).setText(info.getPackages());
		((TextView) findViewById(R.id.tv_weight)).setText(Math.round(Float
				.valueOf(info.getWeight()) * 10)
				/ 10.0
				+ getString(R.string.unit));
		((TextView) findViewById(R.id.tv_capacity)).setText(Math.round(Float
				.valueOf(info.getCapacity()) * 100)
				/ 100.0
				+ getString(R.string.kg));
	}

	private void startOrderTransferDataActivity() {
		Intent i = new Intent(DeliveryOrderListActivity.this,
				DeliveryOrderTransferDataActivity.class);
		int check = mAdapter.getItemSelected();
		if (check >= 0) {
			i.putExtra(Def.TAG_DELIVERY_BATCH_CODE,
					mDeliveryOrderList.get(mAdapter.getItemSelected())
							.getDeliveryBatchCode());
			i.putExtra(Def.TAG_SUMMARY, mSummaryInfo);
		}
		startActivity(i);
	}

	public void confirmConfigNFC() {
		dialog = new Dialog(this, R.style.FullHeightDialog);
		// this is a reference to the style above
		dialog.setContentView(R.layout.custom_dialog); // I saved the xml file
														// above as
														// yesnomessage.xml
		dialog.setCancelable(true);

		// to set the message
		TextView message = (TextView) dialog
				.findViewById(R.id.tvmessagedialogtext);
		message.setText(this.getResources().getString(R.string.text_nfc));

		// add some action to the buttons
		yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
		yes.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Enable NFC
				NetworkUtils
						.startNfcSettingsActivity(DeliveryOrderListActivity.this);
				dialog.dismiss();
			}
		});

		no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
		no.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
				startOrderTransferDataActivity();
			}
		});
		dialog.show();
	}

	public int getMaxDisplayOrder() {
		int maxDisplayOrder = 0;
		for (int i = 0; i < mDeliveryOrderList.size(); i++) {
			int temp = mDeliveryOrderList.get(i).getDisplayingOrder();
			if (temp > maxDisplayOrder) {
				maxDisplayOrder = temp;
			}
		}
		return maxDisplayOrder;
	}

	public void updateDisplay() {
		Send mSend = new Send(this);
		int maxDisplayOrder = getMaxDisplayOrder();
		String tmpCustomerName = "";
		String tmpBushoName = "";
		String tmpBatchCode = "";
		
		for (int i = 0; i < mDeliveryOrderList.size(); i++) {
			maxDisplayOrder++;
			mDeliveryOrderList.get(i).setDisplayingOrder(maxDisplayOrder);
			
			if (tmpCustomerName.equals(mDeliveryOrderList.get(i).getCustomerName()) &&
					tmpBushoName.equals(mDeliveryOrderList.get(i).getDepartmentName())) {
				mDeliveryOrderList.get(i).setBundleBatchCode(tmpBatchCode);
			} else {
				mDeliveryOrderList.get(i).setBundleBatchCode(mDeliveryOrderList.get(i).getDeliveryBatchCode());
				tmpBatchCode = mDeliveryOrderList.get(i).getDeliveryBatchCode();
			}
			
			tmpCustomerName = mDeliveryOrderList.get(i).getCustomerName();
			tmpBushoName = mDeliveryOrderList.get(i).getDepartmentName();
			
			DatabaseManager.getInstance().createOrUpdateOrderItem(mDeliveryOrderList.get(i));
			mSend.requestToUpdateDiplayingOrder(TMSConstrans.mSoshiki_cd,
					mDeliveryOrderList.get(i).getDeliveryBatchCode(),
					String.valueOf(mDeliveryOrderList.get(i).getDisplayingOrder()),
					DateTimeUtils.getSystemTime(),
					mDeliveryOrderList.get(i).getBundleBatchCode());
		}
	}

	private void showProgressDialg(boolean show) {
		if (progressDlg != null) {
			progressDlg.dismiss();
			progressDlg = null;
		}
		if (progressDlg == null) {
			progressDlg = new ProgressDialog(this);
			progressDlg.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.progress));
			progressDlg.setMessage(getResources().getString(
					R.string.alert_loading));
		}
		if (show) {
			progressDlg.show();
		} else {
			progressDlg.dismiss();
			progressDlg = null;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_finish_edit:
//			mAdapter.notifyDataSetChanged();

			showProgressDialg(true);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					updateDisplay();
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showProgressDialg(false);
							updateSortListInfo(false);
							switchToListMode();
							mSummaryInfo = calculateSummary(mDeliveryOrderList);
							displayDeliveryOrderSummaryInfo(mSummaryInfo);
						}
					});
				}
			}).start();;
			
			break;

		case R.id.btn_edit_mode:
			updateSortListInfo(true);
			switchToEditMode();
			break;

		case R.id.btn_transfer_to_other_devices:
			
			if (mAdapter.getCount() == 0) {
				return;
			}
			
			if (NfcUtils.hasNfcFeature(getBaseContext())) {
				if (!NfcUtils.isNfcEnabled(getBaseContext())) {
					confirmConfigNFC();
				} else {
					Intent i = new Intent(DeliveryOrderListActivity.this,
							SendDataActivity.class);
					i.putExtra(Def.TAG_ORDER_ITEM,
							(Parcelable) mDeliveryOrderList.get(mAdapter
									.getItemSelected()));
					i.putExtra(Def.TAG_SUMMARY, mSummaryInfo);
					startActivity(i);
				}
			} else {
				startOrderTransferDataActivity();
			}

			break;

		default:
			break;
		}
	}

	private void updateSortListInfo(boolean show) {
		for (int i = 0; i < mDeliveryOrderList.size(); i++) {
			mDeliveryOrderList.get(i).setIsOn(show);
		}
		mAdapter.updateItemList(mDeliveryOrderList);
		mAdapter.notifyDataSetChanged();
	}

	private void switchToListMode() {
		isEditMode = false;
		mListModeLayout.setVisibility(View.VISIBLE);
		mEditModeLayout.setVisibility(View.GONE);
	}

	private void switchToEditMode() {
		isEditMode = true;
		mListModeLayout.setVisibility(View.GONE);
		mEditModeLayout.setVisibility(View.VISIBLE);
	}

	private class CustomDialogClass extends Dialog implements
			android.view.View.OnClickListener {

		private OrderInfo mOrderItemInfo;

		public CustomDialogClass(Activity a, OrderInfo info) {
			super(a);
			this.mOrderItemInfo = info;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.layout_order_detail);

			displayInfomation();
		}

		public void displayInfomation() {
			if (mOrderItemInfo != null) {
				((TextView) findViewById(R.id.tv_tracking_id))
						.setText(mOrderItemInfo.getTrackingId());
				((TextView) findViewById(R.id.tv_order_id))
						.setText(mOrderItemInfo.getOrderId());
				
				((TextView) findViewById(R.id.tv_carry_out_flag)).setText(mOrderItemInfo.getCarryOutCheckFlag().contains(ECarryOutStatus.YES.getValue()) ? getString(R.string.have): getString(R.string.not_have));
			/*	
			 * Edit by ptlinh
			 * @date 20131209
			 */
				// Display shipper name instead of shipper code
				((TextView) findViewById(R.id.tv_shipper))
						.setText(mOrderItemInfo.getShipperName());
				// Display shipper type name instead of shipper name
				((TextView) findViewById(R.id.tv_shipper_name))
						.setText(mOrderItemInfo.getShipperTypeName());
				// Using japanese for year, month, day
				Date normalDate = null;
				try {
					normalDate = new SimpleDateFormat(Def.FORMAT_DATE).parse(mOrderItemInfo.getFirstDeliveryDate());
				} catch (ParseException e) {
					Log.e(TAG, "" +  e.toString());
				}
				SimpleDateFormat japanDate = new SimpleDateFormat("yyyy" + getString(R.string.year) + "MM" + getString(R.string.month) + "dd" + getString(R.string.day));
				((TextView) findViewById(R.id.tv_first_delivery_date))
						.setText(japanDate.format(normalDate));
				
				 // Estimated time is not necessary in Delivery Order Setting
//				 ((TextView) findViewById(R.id.tv_estimated_delivery_time))
//						.setText(mOrderItemInfo.getEstimatedDeliveryTime());
			 /*	
			 * End edit
			 */
				((TextView) findViewById(R.id.tv_carry_out_status_name))
				.setText(DatabaseManager.getInstance().getCarryOutCheckName(mOrderItemInfo.getCarryOutStatusCd()));
				((TextView) findViewById(R.id.tv_receiver_address))
						.setText(mOrderItemInfo.getReceiverAdress());
				((TextView) findViewById(R.id.tv_receiver_name))
						.setText(mOrderItemInfo.getReceiverName());
				((TextView) findViewById(R.id.tv_receiver_department_name))
						.setText(mOrderItemInfo.getDepartmentName());
				((TextView) findViewById(R.id.tv_person_in_charge_of_receiver_side))
						.setText(mOrderItemInfo
								.getPersonInChargeOfReceiverSide());
				((TextView) findViewById(R.id.tv_package))
						.setText(mOrderItemInfo.getPackageQuantity());
				((TextView) findViewById(R.id.tv_note)).setText(mOrderItemInfo.getNote());
				((TextView) findViewById(R.id.tv_memo)).setText(mOrderItemInfo.getmMessage());
				((TextView) findViewById(R.id.tv_tel)).setText(mOrderItemInfo
						.getCustomerTEL1());

				((TextView) findViewById(R.id.tv_condition_in_front_of_house))
						.setText(mOrderItemInfo.getConditionInFrontOfHouse());
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			default:
				break;
			}
			dismiss();
		}
	}
}
