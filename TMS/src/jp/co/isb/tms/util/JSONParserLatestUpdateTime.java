package jp.co.isb.tms.util;

import jp.co.isb.tms.model.OrderInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class JSONParserLatestUpdateTime {
	
	public static final String TAG_CUSTOMER_MASTER = "M_KOKYAKU";
	public static final String TAG_DATA = "data";
	public static final String TAG_MODIFIED= "MODIFIED";
	public static final String TAG_MODIFIED_DRIVER = "MODIFIED_DRIVER";
	private static final String TAG = null;
	
	public JSONParserLatestUpdateTime() {

	}
	
	public OrderInfo parseLastUpdateTime(String data){
		OrderInfo item = new OrderInfo();
		try{
			JSONObject json = new JSONObject(data);
			JSONArray orders = json.getJSONArray(TAG_DATA);
			for(int i = 0 ; i < orders.length(); i++){
				JSONObject ob = orders.getJSONObject(i);
				String mDriverUpdatedDateTime = ob.getString(TAG_MODIFIED);
				String mUpdatedDateTime = ob.getString(TAG_MODIFIED_DRIVER);
	
				item.setUpdatedDateTime(mUpdatedDateTime);
				item.setDriverUpdatedDateTime(mDriverUpdatedDateTime);
			}
		}catch(Exception e){
			Log.e(TAG, "" +  e.toString());
		}
		return item;
	}
}
