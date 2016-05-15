package jp.co.isb.tms.util;

import java.util.ArrayList;

import jp.co.isb.tms.model.DriverInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParserDriverUtils {

	public static final String TAG_DRIVER_MASTER = "M_DRIVER";
	
	public static final String TAG_DATA= "data";
	public static final String TAG_DRIVER_ID= "DRIVER_CD";
	public static final String TAG_DRIVER_LAST_NAME = "DRIVER_SEI";
	public static final String TAG_DRIVER_FIRST_NAME = "DRIVER_MEI";
	public static final String TAG_DRIVER_LAST_NAME_IN_KATA = "DRIVER_SEI_KANA";
	public static final String TAG_DRIVER_FIRST_NAME_IN_KATA = "DRIVER_MEI_KANA";

	private static final String TAG = JSONParserDriverUtils.class.getSimpleName();
	
	public JSONParserDriverUtils() {

	}
	
	public ArrayList<DriverInfo> parseDriverMaster(String data ) {
		ArrayList<DriverInfo> driverList = new ArrayList<DriverInfo>();
		try {
			JSONObject json = new JSONObject(data);
			JSONArray orders = json.getJSONArray(TAG_DATA);
			for(int i = 0 ; i < orders.length(); i++){
				JSONObject ob = orders.getJSONObject(i);
				String id = ob.getString(TAG_DRIVER_ID);
				String driverLastName = ob.getString(TAG_DRIVER_LAST_NAME);
				String driverFirstName = ob.getString(TAG_DRIVER_FIRST_NAME);
				String driverLastNameInKata = ob.getString(TAG_DRIVER_LAST_NAME_IN_KATA);
				String driverFirstNameInKata = ob.getString(TAG_DRIVER_FIRST_NAME_IN_KATA);
				
				DriverInfo cur = new DriverInfo();
				
				cur.setDriverId(id);
				cur.setDriverLastName(driverLastName);
				cur.setDriverFirstName(driverFirstName);
				cur.setDriverLastNameInKatakana(driverLastNameInKata);
				cur.setDriverFirstNameInKatakana(driverFirstNameInKata);
				
				driverList.add(cur);
			}
		} catch (JSONException e) {
			Log.e(TAG, "" +  e.toString());
		}
		
		
		
		return driverList;
	}
	
}
