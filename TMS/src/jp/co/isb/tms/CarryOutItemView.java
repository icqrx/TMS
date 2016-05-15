package jp.co.isb.tms;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.co.isb.tms.R;
import jp.co.isb.tms.model.OrderInfo;

public class CarryOutItemView extends LinearLayout {
	private TextView mtvNo;
	private TextView mtvTrackingId;
	private TextView mtvPackage;
	private TextView mtvAddress;
	private TextView mtvCustomerName;
//	private TextView mtvDepartmentName;
//	private TextView mtvPersonalInCharge;
	
	private OrderInfo mItemInfo;

	public CarryOutItemView(Context context) {
		super(context);
		LayoutInflater inflate = LayoutInflater.from(getContext());
		inflate.inflate(R.layout.carry_out_item, this, true);
		if (mtvNo == null) {
			mtvNo = (TextView) findViewById(R.id.tv_no);
		}
		if (mtvTrackingId == null) {
			mtvTrackingId = (TextView) findViewById(R.id.tv_tracking_id);
		}
		
		if (mtvPackage == null) {
			mtvPackage = (TextView) findViewById(R.id.tv_package);
		}
		
		if (mtvAddress == null) {
			mtvAddress = (TextView) findViewById(R.id.tv_receiver_address);
		}
		
		if (mtvCustomerName == null) {
			mtvCustomerName = (TextView) findViewById(R.id.tv_customer_name);
		}
//		if (mtvDepartmentName == null) {
//			mtvDepartmentName = (TextView) findViewById(R.id.tv_department_name);
//		}
//
//		if (mtvPersonalInCharge == null) {
//			mtvPersonalInCharge = (TextView) findViewById(R.id.tv_person_in_charge);
//		}
	}

	public void setData(OrderInfo itemInfo, int postion) {
		mItemInfo = itemInfo;
		if (mItemInfo == null) {
			return;
		}
		
		if (mtvNo != null) {
			mtvNo.setText(String.valueOf(postion + 1));
		}
		
		if (mtvTrackingId != null) {
			mtvTrackingId.setText(mItemInfo.getTrackingId());
		}
		if (mtvPackage != null) {
			mtvPackage.setText(mItemInfo.getPackageQuantity());
		}
		if (mtvAddress != null) {
			mtvAddress.setText(mItemInfo.getReceiverAdress());
		}
		if (mtvCustomerName != null) {
			mtvCustomerName.setText(mItemInfo.getCustomerName() + " " + mItemInfo.getDepartmentName()
					 + " " +  mItemInfo.getPersonInChargeAtCustomerSide());
		}
//		if (mtvDepartmentName != null) {
//			mtvDepartmentName.setText(mItemInfo.getDepartmentName());
//		}
//		if (mtvPersonalInCharge != null) {
//			mtvPersonalInCharge.setText(mItemInfo.getPersonInChargeAtCustomerSide());
//		}
	}

}
