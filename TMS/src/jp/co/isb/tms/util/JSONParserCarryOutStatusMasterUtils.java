package jp.co.isb.tms.util;

import java.util.ArrayList;

import jp.co.isb.tms.model.CarryOutStatusMaster;
import jp.co.isb.tms.model.CustomerPicInfo;
import jp.co.isb.tms.model.OrderStatusInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParserCarryOutStatusMasterUtils {
	
	public static final String TAG_DATA= "data";
	public static final String TAG_MEI_CD= "MEI_CD";
	public static final String TAG_MEI_1 = "MEI_1";
	private static final String TAG = JSONParserCarryOutStatusMasterUtils.class.getSimpleName();
	
	public JSONParserCarryOutStatusMasterUtils() {

	}
	
	public ArrayList<CarryOutStatusMaster> parseCarryOut(String data) {
		
		JSONArray orders = null;
		ArrayList<CarryOutStatusMaster> list = new ArrayList<CarryOutStatusMaster>();
		try{
			JSONObject json = new JSONObject(data);
		
			orders = json.getJSONArray(TAG_DATA);
		
			for(int i = 0 ; i < orders.length(); i++){
				JSONObject ob = orders.getJSONObject(i);
				String code = ob.getString(TAG_MEI_CD);
				String name = ob.getString(TAG_MEI_1);
			
				CarryOutStatusMaster item = new CarryOutStatusMaster();
				
				item.setStaffCode(code);
				item.setStatusName(name);
				
				list.add(item);
			}
		}catch(Exception e){
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
}
