package jp.co.isb.tms.util;

import java.util.ArrayList;

import jp.co.isb.tms.model.OrderStatusInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParserStatusUtils {
	
	private static final String TAG_STATUS_MASTER = "M_STATUS";
	
	private static final String TAG_DATA= "data";
	private static final String TAG_STATUS_ID= "STATUS_CD";
	private static final String TAG_STATUS_NAME = "STATUS_NM";
	private static final String TAG_RE_DELIVERABLE_FLAG = "SAIHAISO_FLG";
	private static final String TAG_OTHERS_FLAG = "ETC_FLG";

	private static final String TAG = JSONParserStatusUtils.class.getSimpleName();
	
	public JSONParserStatusUtils() {

	}
	
	public ArrayList<OrderStatusInfo> parseStatusInfo(String data) {
		JSONArray orders = null;
		ArrayList<OrderStatusInfo> statusList = new ArrayList<OrderStatusInfo>();
		try{
			JSONObject json = new JSONObject(data);
			
			orders = json.getJSONArray(TAG_DATA);
			
			for(int i = 0 ; i < orders.length(); i++){
				JSONObject ob = orders.getJSONObject(i);
				String id = ob.getString(TAG_STATUS_ID);
				String statusName = ob.getString(TAG_STATUS_NAME);
	//			int reDeliverableFlag = ob.getInt(TAG_RE_DELIVERABLE_FLAG);
				int othersFlag = ob.getInt(TAG_OTHERS_FLAG);
				
				OrderStatusInfo cur = new OrderStatusInfo();
				
				cur.setStatusId(id);
				cur.setStatusName(statusName);
	//			cur.setReDeliverableFlag(reDeliverableFlag);
				cur.setOthersFlag(othersFlag);
				
				statusList.add(cur);
			}
			
		}catch(Exception ex){
			Log.e(TAG, "" + ex.toString());
		}
		return statusList;
	}
}
