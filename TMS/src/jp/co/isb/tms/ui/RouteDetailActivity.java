package jp.co.isb.tms.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.co.isb.tms.ECarryOutStatus;
import jp.co.isb.tms.R;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.dialog.ConfirmDialog;
import jp.co.isb.tms.dialog.OnConfirmListener;
import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.util.DateTimeUtils;
import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import jp.co.isb.tms.ui.DetectableKeyboardEventLayout;
import android.text.TextWatcher;
import android.text.Editable;


public class RouteDetailActivity extends RouteTopDetailActivity implements
		OnClickListener {
	private static final String TAG = RouteDetailActivity.class.getSimpleName();
	private Button mBtnImage;
	private Button mBtnMap;
	private Button mBtnUpdate;
	private Button mBtnNext;
	private Button mBtnPrev;
	private Button mBtnChangeEstimatedTime;
	private EditText mEditTextCondition;
	private int mFocusPosition = 0;
	protected String mDeliveryBatchCode = "";
	protected String mPulldownStatus = "";
	private int mPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);
		
		Intent i = getIntent();
		mDeliveryBatchCode = (String) i.getStringExtra(Def.TAG_DELIVERY_BATCH_CODE);
		mBtnChangeEstimatedTime = (Button) findViewById(R.id.btn_change_estimated_time);
		mPosition = (int) i.getIntExtra(Def.TAG_POSITION, 0);
		mPulldownStatus = (String) i.getStringExtra(Def.TAG_PULLDOWN);

		setCommonLayout();
		setupHeaderLayoutView();

		// set title screen
		((TextView) findViewById(R.id.title_screen))
				.setText(R.string.title_display_route);

		mBtnImage = (Button) findViewById(R.id.btn_image);
		mBtnMap = (Button) findViewById(R.id.btn_map);
		mBtnUpdate = (Button) findViewById(R.id.btn_update);
		mBtnNext = (Button) findViewById(R.id.btn_next);
		mBtnPrev = (Button) findViewById(R.id.btn_prev);
		mEditTextCondition = (EditText) findViewById(R.id.editText_condition);

		mBtnImage.setOnClickListener(this);
		mBtnMap.setOnClickListener(this);
		mBtnChangeEstimatedTime.setOnClickListener(this);
		mBtnUpdate.setOnClickListener(this);
		mBtnNext.setOnClickListener(this);
		mBtnPrev.setOnClickListener(this);

		updateData(mFocusPosition);
		displayInfomation(mFocusPosition);
		
		DetectableKeyboardEventLayout root = (DetectableKeyboardEventLayout)findViewById(R.id.root);
		root.setKeyboardListener( new DetectableKeyboardEventLayout.KeyboardListener() {
		 
		    @Override
		    public void onKeyboardShown() {
		    	((LinearLayout) findViewById(R.id.action_layout)).setVisibility(View.GONE);
		    }
		 
		    @Override
		    public void onKeyboardHidden() {
		    	((LinearLayout) findViewById(R.id.action_layout)).setVisibility(View.VISIBLE);
		    }
		});
		
	}
	
	@Override
	protected void enableUpdateTimeButton(boolean flag) {
		super.enableUpdateTimeButton(flag);
		
		mBtnChangeEstimatedTime.setEnabled(flag);
		
		if (flag) {
			mBtnChangeEstimatedTime.setBackgroundResource(R.drawable.btn_blue);
		} else {
			mBtnChangeEstimatedTime.setBackgroundResource(R.drawable.btn_blue_selected);
		}
			
	}
	
	@Override
	protected void notifiyChange() {
		super.notifiyChange();
		
		loadData();
		updateData(mFocusPosition);

		displayInfomation(mFocusPosition);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		notifiyChange();
	}

	private void displayInfomation(int postion) {
		if (mListRouteInfo.size() == 0) {
			return;
		}
		OrderInfo mRouteItemInfo = mListRouteInfo.get(postion);
		ArrayList<CustomerInfo> cusList = DatabaseManager.getInstance().getCustomerInfoById(mRouteItemInfo.getCustomerCD());
		if (cusList.size() > 0) {
			mRouteItemInfo.setConditionInFrontOfHouse(cusList.get(0).getConditionsInForntOfHouse());
		}
		if (mRouteItemInfo != null) {
			((TextView) findViewById(R.id.tv_tracking_id))
					.setText(mRouteItemInfo.getTrackingId());
			((TextView) findViewById(R.id.tv_order_id)).setText(mRouteItemInfo
					.getOrderId());
			((TextView) findViewById(R.id.tv_carry_out_flag)).setText(mRouteItemInfo.getCarryOutCheckFlag().contains(ECarryOutStatus.YES.getValue()) ? getString(R.string.have): getString(R.string.not_have));
			((TextView) findViewById(R.id.tv_carry_out_status_name)).setText(DatabaseManager.getInstance().getCarryOutCheckName(mRouteItemInfo.getCarryOutStatusCd()));
			((TextView) findViewById(R.id.tv_shipper)).setText(mRouteItemInfo
					.getShipperName());
			((TextView) findViewById(R.id.tv_shipper_type_name))
					.setText(mRouteItemInfo.getShipperTypeName());
			// Using japanese for year, month, day
			Date normalDate = null;
			try {
				normalDate = new SimpleDateFormat(Def.FORMAT_DATE).parse(mRouteItemInfo.getFirstDeliveryDate());
			} catch (ParseException e) {
				Log.e(TAG, "" +  e.toString());
			}
			SimpleDateFormat japanDate = new SimpleDateFormat("yyyy" + getString(R.string.year) + "MM" + getString(R.string.month) + "dd" + getString(R.string.day));
			((TextView) findViewById(R.id.tv_first_delivery_date))
					.setText(japanDate.format(normalDate));
			((TextView) findViewById(R.id.tv_estimated_delivery_time))
					.setText(DateTimeUtils.getTime(mRouteItemInfo.getEstimatedDeliveryTime()));
			((TextView) findViewById(R.id.tv_receiver_address))
					.setText(mRouteItemInfo.getReceiverAdress());
			((TextView) findViewById(R.id.tv_receiver_name))
					.setText(mRouteItemInfo.getReceiverName());
			((TextView) findViewById(R.id.tv_receiver_department_name))
					.setText(mRouteItemInfo.getDepartmentName());
			((TextView) findViewById(R.id.tv_person_in_charge_of_receiver_side))
					.setText(mRouteItemInfo.getPersonInChargeOfReceiverSide());
			((TextView) findViewById(R.id.tv_package)).setText(mRouteItemInfo
					.getPackageQuantity());
			((TextView) findViewById(R.id.tv_note)).setText(mRouteItemInfo
					.getNote());
			((TextView) findViewById(R.id.tv_tel)).setText(mRouteItemInfo
					.getCustomerTEL1());
			((TextView) findViewById(R.id.tv_memo)).setText(mRouteItemInfo
					.getmMessage());
			mEditTextCondition.setText(mRouteItemInfo.getConditionInFrontOfHouse());
			
		}
	}


	private void updateData(int position) {
		Log.d(TAG, "postion: " + position);
		if (mListRouteInfo.size() == 0) {
			return;
		}		
		((TextView) findViewById(R.id.tv_tracking_id)).setText(mListRouteInfo
				.get(position).getTrackingId());
		((TextView) findViewById(R.id.tv_index)).setText((position + 1) + "/"
				+ mListRouteInfo.size());

		if (position == 0) {
			((Button) findViewById(R.id.btn_prev)).setEnabled(false);
		} else {
			((Button) findViewById(R.id.btn_prev)).setEnabled(true);
		}
		if ((mListRouteInfo.size() - position) == 1) {
			((Button) findViewById(R.id.btn_next)).setEnabled(false);
		} else if ((mListRouteInfo.size() - position) > 1) {
			((Button) findViewById(R.id.btn_next)).setEnabled(true);
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);

		Intent i;
		
		switch (v.getId()) {
		case R.id.btn_image:			
			i = new Intent(RouteDetailActivity.this,
					RoutePictureActivity.class);
			
			i.putExtra(Def.TAG_DELIVERY_BATCH_CODE, mDeliveryBatchCode);
			i.putExtra(Def.TAG_BUNDLE_BATCH_CODE, mBundleBatchCode);
			i.putExtra(Def.TAG_CUSTOMER_CD, mListRouteInfo.get(mFocusPosition).getCustomerCD());
			i.putExtra(Def.TAG_DELIVERY_ESTIMATED_DATE_TIME, mListRouteInfo.get(mFocusPosition).getEstimatedDeliveryTime());
			i.putExtra(Def.TAG_PULLDOWN, mPulldownStatus);
			startActivity(i);
			finish();
			break;
		case R.id.btn_map:
			Intent intent = new Intent(RouteDetailActivity.this, GoogleMapActivity.class);
			intent.putExtra(Def.TAG_BUNDLE_BATCH_CODE, mBundleBatchCode);
			startActivity(intent);
			break;
		case R.id.btn_change_estimated_time:
			i = new Intent(RouteDetailActivity.this,
					RouteUpdateEstimatedTimeActivity.class);
			i.putExtra(Def.TAG_POSITION, mPosition );
			i.putExtra(Def.TAG_DELIVERY_BATCH_CODE, mDeliveryBatchCode);
			i.putExtra(Def.TAG_DELIVERY_ESTIMATED_DATE_TIME, mListRouteInfo.get(mFocusPosition).getEstimatedDeliveryTime());
			startActivity(i);
			break;
		case R.id.btn_update:
			new ConfirmDialog(this, getString(R.string.alert_dialog_confirm_update_route), 
					new OnConfirmListener() {
				
				@Override
				public void onYes() {
					DatabaseManager.getInstance().updateCustomerMaster(mListRouteInfo.get(mFocusPosition).getCustomerCD(), 
							mEditTextCondition.getText().toString());
					finish();
				}
				
				@Override
				public void onNo() {
					
				}
			}).show();
			break;
		case R.id.btn_next:
			Log.d(TAG, "mFocusPosition: " + mFocusPosition);
			mFocusPosition += 1;
			updateData(mFocusPosition);
			displayInfomation(mFocusPosition);
			break;
		case R.id.btn_prev:
			Log.d(TAG, "mFocusPosition: " + mFocusPosition);
			mFocusPosition -= 1;
			updateData(mFocusPosition);
			displayInfomation(mFocusPosition);
			break;
		default:
			break;
		}

	}
}
