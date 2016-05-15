package jp.co.isb.tms;

import java.util.ArrayList;

import jp.co.isb.tms.model.OrderInfo;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;

/**
 * Adapter for listview
 * 
 */
public class DeliveryOrderAdapter extends BaseAdapter {
	private ArrayList<OrderInfo> mItemList = null;
	private Context mContext;
	private int itemSelected = -1;

	public DeliveryOrderAdapter(Context context, ArrayList<OrderInfo> itemList) {
		mContext = context;
		if (itemList != null) {
			mItemList = itemList;
		} else {
			mItemList = new ArrayList<OrderInfo>();
		}

	}
	
	public void setmItemList(ArrayList<OrderInfo> mItemList) {
		this.mItemList = mItemList;
	}

	@Override
	public int getCount() {
		if (mItemList != null) {
			return mItemList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mItemList != null) {
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
		DeliveryOrderItemView itemView = null;
		if (convertView == null) {
			itemView = new DeliveryOrderItemView(mContext);
		} else {
			itemView = (DeliveryOrderItemView) convertView;
		}
		if (mItemList == null) {
			return itemView;
		}
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		itemView.setLayoutParams(params);
		OrderInfo itemInfo = mItemList.get(position);
		itemView.setData(itemInfo, position);

		if (itemSelected != -1 & itemSelected == position) {
			itemView.setBackgroundColor(Color.CYAN);
		} else {
			itemView.setBackgroundColor(Color.WHITE);
		}
		return itemView;
	}

	public void removeItem(OrderInfo item) {
		mItemList.remove(item);
	}

	public void insertItem(OrderInfo item, int pos) {
		mItemList.add(pos, item);
	}

	public void updateItemList(ArrayList<OrderInfo> itemList) {
		mItemList = itemList;
	}

	public int getItemSelected() {
		return itemSelected;
	}

	public OrderInfo setItemSelected(int itemSelected) {
		this.itemSelected = itemSelected;
		if (mItemList.size() != 0) {
			return mItemList.get(itemSelected);
		}
		return null;
	}

}
