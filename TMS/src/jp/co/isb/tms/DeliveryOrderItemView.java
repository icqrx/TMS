package jp.co.isb.tms;

import jp.co.isb.tms.model.OrderInfo;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeliveryOrderItemView extends LinearLayout {
	private TextView mtvNo;
	private TextView mtvCustomerName;
//	private TextView mtvDepartmentName;
	private TextView mtvPackage;
	private TextView mtvAddress;
	private TextView mtvNote;
	private ImageView mImgDragHandle;
	
	private OrderInfo mItemInfo;

	public DeliveryOrderItemView(Context context) {
		super(context);
		LayoutInflater inflate = LayoutInflater.from(getContext());
		inflate.inflate(R.layout.order_item, this, true);
		mImgDragHandle = (ImageView) findViewById(R.id.drag_handle);
		if (mtvNo == null) {
			mtvNo = (TextView) findViewById(R.id.tv_no);
		}
		
		if (mtvCustomerName == null) {
			mtvCustomerName = (TextView) findViewById(R.id.tv_customer_name);
		}
		
//		if (mtvDepartmentName == null) {
//			mtvDepartmentName = (TextView) findViewById(R.id.tv_department_name);
//		}
		
		if (mtvPackage == null) {
			mtvPackage = (TextView) findViewById(R.id.tv_package);
		}
		
		if (mtvAddress == null) {
			mtvAddress = (TextView) findViewById(R.id.tv_address);
		}
		
		if (mtvNote == null) {
			mtvNote = (TextView) findViewById(R.id.tv_note);
		}
	}

	public void setData(OrderInfo itemInfo, int postion) {    	
		mItemInfo = itemInfo;
		if(mItemInfo == null) {
    		return;
    	}
		if(mtvNo != null) {
			mtvNo.setText(String.valueOf(postion + 1));
		}
		
		if (mtvCustomerName != null) {
			mtvCustomerName.setText(mItemInfo.getCustomerName() + " " + mItemInfo.getDepartmentName());
		}
//		if (mtvDepartmentName != null) {
//			mtvDepartmentName.setText(mItemInfo.getDepartmentName());
//		}
		if (mtvPackage != null) {
			mtvPackage.setText(mItemInfo.getPackageQuantity());
		}
		if (mtvAddress != null) {
			mtvAddress.setText(mItemInfo.getReceiverAdress());
		}
		if (mtvNote != null) {
			/*	
			 * Edit by ptlinh
			 * @date 20131209
			 * Combine note with customer's message before shown
			 */
			mtvNote.setText(mItemInfo.getNote() + " " + mItemInfo.getmMessage());
			if (!mtvNote.getText().toString().trim().isEmpty()) {
				mtvNote.setTextColor(Color.RED);
			}
			/*	
			 * End edit
			 */
		}
		if (itemInfo.isIsOn()) {
			mImgDragHandle.setVisibility(View.VISIBLE);
		} else {
			mImgDragHandle.setVisibility(View.GONE);
		}
		
	}
}
