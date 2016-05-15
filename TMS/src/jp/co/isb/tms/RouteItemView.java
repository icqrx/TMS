package jp.co.isb.tms;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.util.DateTimeUtils;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RouteItemView extends LinearLayout {
    private TextView mtvNo;
	private TextView mtvAddress;
	private TextView mtvReceiverName;
//	private TextView mtvDepartmentName;
	private TextView mtvDeliveryEstimatedTime;
	private TextView mtvStatus;
	private TextView mtvNote;
	private TextView mtvPackage;

    
	private OrderInfo mItemInfo;

    public RouteItemView(Context context) {
        super(context);
        LayoutInflater inflate = LayoutInflater.from(getContext());
        inflate.inflate(R.layout.route_item, this, true);
		if(mtvNo == null) {
			mtvNo = (TextView)findViewById(R.id.tv_no);
		}	
		if(mtvAddress == null) {
			mtvAddress = (TextView)findViewById(R.id.tv_address);
		}
		if(mtvReceiverName == null) {
			mtvReceiverName = (TextView)findViewById(R.id.tv_receiver_name);
		}
//		if(mtvDepartmentName == null) {
//			mtvDepartmentName = (TextView)findViewById(R.id.tv_department_name);
//		}
		if(mtvDeliveryEstimatedTime == null) {
			mtvDeliveryEstimatedTime = (TextView)findViewById(R.id.tv_estimated_delivery_time);
		}
		if(mtvStatus == null) {
			mtvStatus = (TextView)findViewById(R.id.tv_status);
		}
		if(mtvNote == null) {
			mtvNote = (TextView)findViewById(R.id.tv_note);
		}
		if(mtvPackage == null) {
			mtvPackage = (TextView)findViewById(R.id.tv_package);
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
		
		if(mtvAddress != null) {
			mtvAddress.setText(mItemInfo.getReceiverAdress());
		}
		if(mtvReceiverName != null) {
			mtvReceiverName.setText(mItemInfo.getReceiverName() + " " + mItemInfo.getDepartmentName());
		}
//		if(mtvDepartmentName != null) {
//			mtvDepartmentName.setText(mItemInfo.getDepartmentName());
//		}
		if(mtvDeliveryEstimatedTime != null) {
			mtvDeliveryEstimatedTime.setText(DateTimeUtils.getTime(mItemInfo.getEstimatedDeliveryTime()));
		}
		if(mtvStatus != null) {
			mtvStatus.setText(mItemInfo.getStatusName());
		}
		if(mtvNote != null) {
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
		if(mtvPackage != null) {
			mtvPackage.setText(mItemInfo.getPackageQuantity());
		}
		
	}
    
}
