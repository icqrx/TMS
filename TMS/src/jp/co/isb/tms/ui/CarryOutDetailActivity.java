package jp.co.isb.tms.ui;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.co.isb.tms.ECarryOutStatus;
import jp.co.isb.tms.R;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.model.CarryOutStatusMaster;
import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.util.DateTimeUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CarryOutDetailActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = CarryOutDetailActivity.class.getSimpleName();
	private Button mBtnCarryOut;
	private Button mBtnOthers;
	private String mDeliveryBatchCode;
	private int mTotal;
	private int selectedStatusPostion = 0;
	private int mSelectedCarryoutPostion = 0;
	private ArrayList<CarryOutStatusMaster> mCarryOutStatusInfos;
	private OrderInfo mOrderInfo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carry_out_detail);

		setupHeaderLayoutView();
		// set title screen
		((TextView) findViewById(R.id.title_screen))
				.setText(R.string.title_carry_out);
		
		mBtnCarryOut = (Button) findViewById(R.id.btn_carry_out);
		mBtnOthers = (Button) findViewById(R.id.btn_others);
		mBtnCarryOut.setOnClickListener(this);
		mBtnOthers.setOnClickListener(this);


		loadData();
		displayInfomation();
	}
	
	private void loadData() {
		Intent i = getIntent();
		if (i != null) {
			mTotal = (int) i.getIntExtra(Def.TAG_TOTAL, 0);
			mDeliveryBatchCode = (String) i.getStringExtra(Def.TAG_DELIVERY_BATCH_CODE);
			mOrderInfo = DatabaseManager.getInstance().getCarrOutDetail(mDeliveryBatchCode);
			ArrayList<CustomerInfo> cusList = DatabaseManager.getInstance().getCustomerInfoById(mOrderInfo.getCustomerCD());
			if (cusList.size() > 0) {
				mOrderInfo.setConditionInFrontOfHouse(cusList.get(0).getConditionsInForntOfHouse());
				
				((TextView) findViewById(R.id.tv_hikiate_kbn))
				.setText(mOrderInfo.getAllocationTypeName());
			}
			if (cusList.size() > 0) {
				mOrderInfo.setmMessage(cusList.get(0).getMessage());
			}

		}
	}
	
	@Override
	protected void notifiyChange() {
		super.notifiyChange();
		
		loadData();
		displayInfomation();
	}
	
	private void displayInfomation() {
		
		// display total item
		((TextView) findViewById(R.id.tv_total_item)).setText(getResources()
				.getString(R.string.total_item,
						String.valueOf(mTotal)));
		
		if (mDeliveryBatchCode != null) {
			((TextView) findViewById(R.id.tv_tracking_id)).setText(mOrderInfo.getTrackingId());
			((TextView) findViewById(R.id.tv_order_id)).setText(mOrderInfo.getOrderId());
			((TextView) findViewById(R.id.tv_carry_out_flag)).setText(mOrderInfo.getCarryOutCheckFlag().contains(ECarryOutStatus.YES.getValue()) ? getString(R.string.have): getString(R.string.not_have));
			((TextView) findViewById(R.id.tv_carry_out_status_name)).setText(DatabaseManager.getInstance().getCarryOutCheckName(mOrderInfo.getCarryOutStatusCd()));
			((TextView) findViewById(R.id.tv_shipper)).setText(mOrderInfo.getShipperName());
			((TextView) findViewById(R.id.tv_shipper_type_name)).setText(mOrderInfo.getShipperTypeName());
			// Using japanese for year, month, day
			Date normalDate = null;
			try {
				normalDate = new SimpleDateFormat(Def.FORMAT_DATE).parse(mOrderInfo.getFirstDeliveryDate());
			} catch (ParseException e) {
				Log.e(TAG, "" +  e.toString());
			}
			SimpleDateFormat japanDate = new SimpleDateFormat("yyyy" + getString(R.string.year) + "MM" + getString(R.string.month) + "dd" + getString(R.string.day));
			((TextView) findViewById(R.id.tv_first_delivery_date)).setText(japanDate.format(normalDate));
			((TextView) findViewById(R.id.tv_estimated_delivery_time)).setText(DateTimeUtils.getTime(mOrderInfo.getEstimatedDeliveryTime()));
			((TextView) findViewById(R.id.tv_receiver_address)).setText(mOrderInfo.getReceiverAdress());
			((TextView) findViewById(R.id.tv_receiver_name)).setText(mOrderInfo.getReceiverName());
			((TextView) findViewById(R.id.tv_receiver_department_name)).setText(mOrderInfo.getDepartmentName());
			((TextView) findViewById(R.id.tv_person_in_charge_of_receiver_side)).setText(mOrderInfo.getPersonInChargeOfReceiverSide());
			((TextView) findViewById(R.id.tv_package)).setText(mOrderInfo.getPackageQuantity());
			((TextView) findViewById(R.id.tv_note)).setText(mOrderInfo.getNote());
			((TextView) findViewById(R.id.tv_tel)).setText(mOrderInfo.getCustomerTEL1());
			((TextView) findViewById(R.id.tv_memo)).setText(mOrderInfo.getmMessage());
			((TextView) findViewById(R.id.tv_condition_in_front_of_house)).setText(mOrderInfo.getConditionInFrontOfHouse());

		}
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_carry_out:
			
			DatabaseManager.getInstance().updateCarryoutFlag(mDeliveryBatchCode, ECarryOutStatus.YES);
			// set result 
			Intent i = new Intent();
			setResult(RESULT_OK, i);
			
			finish();
			break;

		case R.id.btn_others:
				mCarryOutStatusInfos = DatabaseManager.getInstance().getAllCarryOutStatusInfo();
				int checkedPos = getPos(mOrderInfo.getStatusId());
				
				new AlertDialog.Builder(CarryOutDetailActivity.this)
				.setIconAttribute(android.R.attr.alertDialogIcon)
				.setTitle(R.string.alert_dialog_single_choice)
				.setSingleChoiceItems(convertToArrayString(mCarryOutStatusInfos), checkedPos,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								selectedStatusPostion = which;
							}
						})
				.setPositiveButton(R.string.alert_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mOrderInfo.setCarryOutStatusCd(mCarryOutStatusInfos.get(selectedStatusPostion).getStaffCode());
								
								DatabaseManager.getInstance().updateDeliveryOrderByCarryOutStatusCd(mDeliveryBatchCode, 
										mOrderInfo.getCarryOutStatusCd());
								new CarryOutTypeDialog(
										CarryOutDetailActivity.this).show();
							}
						})
				.setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						}).create().show();

			break;
		default:
			break;
		}

	}
	
	private int getPos(String statusCd) {
		for (int i = 0; i < mCarryOutStatusInfos.size(); i++) {
			if (statusCd.equalsIgnoreCase(mCarryOutStatusInfos.get(i).getStaffCode())) {
				return i;
			}
		}
		return 0;
	}
	
	/*
	 * convert driver list to array string
	 */
	private CharSequence[] convertToArrayString(
			ArrayList<CarryOutStatusMaster> deliveryStatusInfos) {
		CharSequence[] deliveryStatus = new CharSequence[deliveryStatusInfos
				.size()];
		for (int i = 0; i < deliveryStatusInfos.size(); i++) {
			deliveryStatus[i] = deliveryStatusInfos.get(i)
					.getStatusName();
		}

		return deliveryStatus;
	}

	private class CarryOutTypeDialog {
		private AlertDialog alert;

		public CarryOutTypeDialog(Context context) {
			alert = new AlertDialog.Builder(context)
					.setIconAttribute(android.R.attr.alertDialogIcon)
					.setTitle(R.string.alert_dialog_single_choice)
					.setSingleChoiceItems(
							R.array.select_dialog_carray_out_type, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichOption) {

									mSelectedCarryoutPostion = whichOption;
								}
							})
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (mSelectedCarryoutPostion == 0) {
										DatabaseManager.getInstance().updateCarryoutFlagWithOthers(mDeliveryBatchCode, ECarryOutStatus.YES);
									} else {
										DatabaseManager.getInstance().updateCarryoutFlagWithOthers(mDeliveryBatchCode, ECarryOutStatus.NO);
									}
									
									finish();
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).create();
			;
		}

		public void show() {
			alert.show();
		}

	}

}
