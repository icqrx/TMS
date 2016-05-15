package jp.co.isb.tms;

import java.util.ArrayList;

import jp.co.isb.tms.model.CustomerPicInfo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Adapter for listview
 * 
 */
public class PictureAdapter extends BaseAdapter {
	/** The parent context */
	private Context myContext;
	private ArrayList<CustomerPicInfo> mPicList;
	
	/** Simple Constructor saving the 'parent' context. */
	public PictureAdapter(Context c, ArrayList<CustomerPicInfo> picList) {
		this.myContext = c;
		this.mPicList = picList;
	}

	// inherited abstract methods - must be implemented
	// Returns count of images, and individual IDs
	public int getCount() {
		return this.mPicList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	// Returns a new ImageView to be displayed,
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		PictureItemView itemView = null;
		if (convertView == null) {
			itemView = new PictureItemView(myContext);
		} else {
			itemView = (PictureItemView) convertView;
		}
		if (mPicList == null) {
			return itemView;
		}
		CustomerPicInfo itemInfo = mPicList.get(position);
		itemView.setData(itemInfo);
		
		return itemView;
	}
	public void removeItem(CustomerPicInfo item) {
		mPicList.remove(item);
	}
	
	public void insertItem(CustomerPicInfo item, int pos) {
		mPicList.add(pos, item);
	}
}
