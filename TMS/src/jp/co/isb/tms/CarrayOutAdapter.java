package jp.co.isb.tms;

import java.util.ArrayList;

import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.model.OrderInfo;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * Adapter for listview 
 *
 */
public class CarrayOutAdapter extends BaseAdapter{
	private ArrayList<OrderInfo> mItemList = null;
	private Context mContext;	
	private int mSelectedPostition = -1;
	private CarryOutItemView itemView = null;
	
	public CarrayOutAdapter(Context context, ArrayList<OrderInfo> itemList) {
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
		
		if(convertView == null) {
			itemView = new CarryOutItemView(mContext);
		} else {
			itemView = (CarryOutItemView) convertView;
		}
		if(mItemList == null) {
			return itemView;
		}
		OrderInfo itemInfo = mItemList.get(position);
		itemView.setData(itemInfo, position);
		
		/*
		 * Edit by ptlinh
		 * @date: 20131209
		 * Adjust item's color for carry-out list
		 */
		
		// If carry out status code is null or empty
		if(itemInfo.getCarryOutStatusCd().trim().isEmpty()){
			// If carry out flag is on
			if(itemInfo.getCarryOutCheckFlag().contains(ECarryOutStatus.YES.getValue())){
				itemView.setBackgroundColor(Color.GRAY);
			}
			// If carry out flag is off
			else {
				itemView.setBackgroundColor(Color.WHITE);
			}
		}
		// If carry out status code is not null
		else{
			// If carry out flag is on
			if(itemInfo.getCarryOutCheckFlag().contains(ECarryOutStatus.YES.getValue())){
				itemView.setBackgroundColor(Color.RED);
			}
			// If carry out flag is off
			else {
				itemView.setBackgroundColor(Color.RED);
			}
		}
		
		/*if (!itemInfo.getCarryOutStatusCd().equalsIgnoreCase(Def.NULL) ||
				!itemInfo.getCarryOutStatusCd().trim().isEmpty()) {
			// un-completely
			itemView.setBackgroundColor(Color.RED);
		} 
		if (itemInfo.getCarryOutCheckFlag().contains(ECarryOutStatus.YES.getValue())) {
			// completely
			itemView.setBackgroundColor(Color.GRAY);
		} else if (itemInfo.getCarryOutCheckFlag().contains(ECarryOutStatus.NO.getValue()))
			if (itemInfo.getCarryOutStatusCd().equalsIgnoreCase(Def.NULL) ||
					itemInfo.getCarryOutStatusCd().trim().isEmpty()){
			// none
			itemView.setBackgroundColor(Color.WHITE);
		}*/
		
		/*
		 * End Edit
		 */
		
		return itemView;
	}
	public void removeItem(OrderInfo item) {
		mItemList.remove(item);
	}
	
	public void insertItem(OrderInfo item, int pos) {
		mItemList.add(pos, item);
	}
	public int getSelectedPostition() {
		return mSelectedPostition;
	}
	public void setSelectedPostition(int mSelectedPostition) {
		this.mSelectedPostition = mSelectedPostition;
	}
}
