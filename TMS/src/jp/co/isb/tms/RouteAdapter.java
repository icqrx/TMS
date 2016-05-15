package jp.co.isb.tms;

import java.util.ArrayList;

import jp.co.isb.tms.model.OrderInfo;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Adapter for listview 
 *
 */
public class RouteAdapter extends BaseAdapter{
	private ArrayList<OrderInfo> mItemList = null;
	private Context mContext;	

	public RouteAdapter(Context context, ArrayList<OrderInfo> itemList) {
		mContext = context;
		if(itemList != null) {
			mItemList = itemList;
		} else {
			mItemList = new ArrayList<OrderInfo>();
		}
		
	}
	@Override
	public int getCount() {
		if(mItemList != null) {
			return mItemList.size();
		}
		return 0;
	}
	
	public void setmItemList(ArrayList<OrderInfo> mItemList) {
		this.mItemList = mItemList;
	}

	@Override
	public Object getItem(int position) {	
		if(mItemList != null) {
			return mItemList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
		RouteItemView itemView = null;
		if(convertView == null) {
			itemView = new RouteItemView(mContext);
		} else {
			itemView = (RouteItemView) convertView;
		}
		if(mItemList == null) {
			return itemView;
		}
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		itemView.setLayoutParams(params );
		OrderInfo itemInfo = mItemList.get(position);
		itemView.setData(itemInfo, position);
		
//		
//		if(!itemInfo.getStatusId().equalsIgnoreCase(EStatus_CD.On_Delivery.getValue())){
//			itemView.setBackgroundColor(Color.GRAY);
//		}
		
		if (itemInfo.getLateFlag()) {
			TextView text =  (TextView)itemView.findViewById(R.id.tv_estimated_delivery_time);
			text.setTextColor(android.graphics.Color.RED);
		} else {
			TextView text =  (TextView)itemView.findViewById(R.id.tv_estimated_delivery_time);
			text.setTextColor(android.graphics.Color.BLACK);
		}
		
		return itemView;
	}
	public void removeItem(OrderInfo item) {
		mItemList.remove(item);
	}
	
	public void insertItem(OrderInfo item, int pos) {
		mItemList.add(pos, item);
	}
}
