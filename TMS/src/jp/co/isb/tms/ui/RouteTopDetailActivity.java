package jp.co.isb.tms.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import jp.co.isb.tms.EStatus_CD;
import jp.co.isb.tms.R;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.model.OrderStatusInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class RouteTopDetailActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = RouteTopDetailActivity.class.getSimpleName();
	protected Button mBtnComplete;
	protected Button mBtnAbsence;
	protected Button mBtnOthers;
	protected int mSelectedPostion = 0;
	protected ArrayList<OrderInfo> mListRouteInfo = new ArrayList<OrderInfo>();	
	protected ArrayList<OrderStatusInfo> deliveryStatusInfos = new ArrayList<OrderStatusInfo>();
	protected String mBundleBatchCode = "";
	protected String mPulldownStatus = "";
	protected String mStatusId = "";
	protected int mEnableFlag = 0; //0:enable 1:disable 2:only 020 button is available.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		mBundleBatchCode = (String) i.getStringExtra(Def.TAG_BUNDLE_BATCH_CODE);
		mStatusId = (String) i.getStringExtra(Def.TAG_STATUS_ID);
		mPulldownStatus = (String) i.getStringExtra(Def.TAG_PULLDOWN);
		
		loadData();
		
		if (mListRouteInfo.size() > 0) {
			if (mPulldownStatus.equalsIgnoreCase(EStatus_CD.On_Delivery.getValue())) {
				mEnableFlag = 0;
			} else if (mPulldownStatus.equalsIgnoreCase(EStatus_CD.Absence.getValue())) {
				mEnableFlag = 2;
			} else {
				mEnableFlag = 1;
			}
		}
	}
	
	protected void loadData() {
		if (mPulldownStatus.equalsIgnoreCase(EStatus_CD.All.getValue())) {
			mListRouteInfo = DatabaseManager.getInstance().getDataAssignedToDriverAll(mBundleBatchCode);
			
		}if (mPulldownStatus.equalsIgnoreCase(EStatus_CD.Under_Investigation.getValue())) {
			
			mListRouteInfo = DatabaseManager.getInstance().getDataAssignedToDriverDontCarryOut(mBundleBatchCode);
		} else {
			mListRouteInfo = DatabaseManager.getInstance().getDataAssignedToDriver(mBundleBatchCode);
		}
	}
	
	private void enableButton(int flag) {
		
		if (flag == 0) {
			((Button) findViewById(R.id.btn_complete)).setEnabled(true);
			mBtnAbsence.setEnabled(true);
			mBtnOthers.setEnabled(true);
			((Button) findViewById(R.id.btn_complete)).setBackgroundResource(R.drawable.btn_complete);
			mBtnAbsence.setBackgroundResource(R.drawable.btn_absence);
			mBtnOthers.setBackgroundResource(R.drawable.btn_blue);
			enableUpdateTimeButton(true);
		} else if (flag == 1) {
			((Button) findViewById(R.id.btn_complete)).setEnabled(false);
			mBtnAbsence.setEnabled(false);
			mBtnOthers.setEnabled(false);
			((Button) findViewById(R.id.btn_complete)).setBackgroundResource(R.drawable.btn_complete_selected);
			mBtnAbsence.setBackgroundResource(R.drawable.btn_absence_selected);
			mBtnOthers.setBackgroundResource(R.drawable.btn_blue_selected);
			enableUpdateTimeButton(false);
		} else if (flag == 2)  {
			((Button) findViewById(R.id.btn_complete)).setEnabled(true);
			mBtnAbsence.setEnabled(false);
			mBtnOthers.setEnabled(false);
			((Button) findViewById(R.id.btn_complete)).setBackgroundResource(R.drawable.btn_complete);
			mBtnAbsence.setBackgroundResource(R.drawable.btn_absence_selected);
			mBtnOthers.setBackgroundResource(R.drawable.btn_blue_selected);
		}
		
	}
	
	protected void enableUpdateTimeButton(boolean flag) {
		
	}
	
	protected void setCommonLayout() {
		mBtnComplete = (Button) findViewById(R.id.btn_complete);
		mBtnAbsence = (Button) findViewById(R.id.btn_absence);
		mBtnOthers = (Button) findViewById(R.id.btn_others);

		mBtnComplete.setOnClickListener(this);
		mBtnAbsence.setOnClickListener(this);
		mBtnOthers.setOnClickListener(this);
		
		enableButton(mEnableFlag);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		Intent i;
		Toast toast;
		
		switch (v.getId()) {
		case R.id.btn_complete:
			toast = Toast.makeText(this, getString(R.string.toast_finish), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			DatabaseManager.getInstance().updateDeliveryOrderByBundle(mBundleBatchCode, 
					EStatus_CD.Finish.getValue());
			finish();
			break;

		case R.id.btn_absence:
			toast = Toast.makeText(this, getString(R.string.toast_absence), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			DatabaseManager.getInstance().updateDeliveryOrderByBundle(mBundleBatchCode, 
					EStatus_CD.Absence.getValue());
			finish();
			break;
		case R.id.btn_others:
			try {
				deliveryStatusInfos = DatabaseManager.getInstance().getStatusWithOtherFlag();
				
				DeliveryStatusDialog dialog = new DeliveryStatusDialog(this,
						convertToArrayString(deliveryStatusInfos));
				
				dialog.show();
			} catch (SQLException e) {
				Log.e(TAG, "" +  e.toString());
			}



			break;
		default:
			break;
		}

	}
	
	private int getPos(String statusCd) {
		for (int i = 0; i < deliveryStatusInfos.size(); i++) {
			if (statusCd.equalsIgnoreCase(deliveryStatusInfos.get(i).getStatusId())) {
				return i;
			}
		}
		return 0;
	}

	/*
	 * convert driver list to array string
	 */
	private CharSequence[] convertToArrayString(
			ArrayList<OrderStatusInfo> deliveryStatusInfos) {
		CharSequence[] deliveryStatus = new CharSequence[deliveryStatusInfos
				.size()];
		for (int i = 0; i < deliveryStatusInfos.size(); i++) {
			deliveryStatus[i] = deliveryStatusInfos.get(i)
					.getStatusName();
		}

		return deliveryStatus;
	}

	private class DeliveryStatusDialog {
		private AlertDialog alert;

		public DeliveryStatusDialog(Context context, CharSequence[] array) {
			
			int checkedPos = getPos(mListRouteInfo.get(mSelectedPostion).getStatusId());
			
			alert = new AlertDialog.Builder(context)
				.setIconAttribute(android.R.attr.alertDialogIcon)
				.setTitle(R.string.alert_dialog_single_choice)
				.setSingleChoiceItems(array, checkedPos,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int postion) {

							mSelectedPostion = postion;
						}
					})
				.setPositiveButton(R.string.alert_dialog_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Toast toast = Toast.makeText(RouteTopDetailActivity.this, 
									deliveryStatusInfos.get(mSelectedPostion).getStatusName() + getString(R.string.toast_others),
									Toast.LENGTH_SHORT);
									toast.setGravity(Gravity.CENTER, 0, 0);
									toast.show();
									
							DatabaseManager.getInstance().updateDeliveryOrderByBundle(mBundleBatchCode, 
									deliveryStatusInfos.get(mSelectedPostion).getStatusId());
							
							gotoRouteListScreen();
						}
					})
				.setNegativeButton(R.string.alert_dialog_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							/* User clicked No so do some stuff */
						}
					}).create();
		}

		public void show() {
			alert.show();
		}

	}

}
