package jp.co.isb.tms.util;

import java.util.ArrayList;

import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.OrderStatusInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParserLatestUpdateTimeCustomerUtils {
	
	public static final String TAG_CUSTOMER_MASTER = "M_KOKYAKU";
	public static final String TAG_DATA = "data";
	public static final String TAG_CUSTOMER_ID= "KOKYAKU_CD";
	public static final String TAG_CONDITIONS_IN_FORNT_OF_HOUSE = "NOKISAKI";
	public static final String TAG_MESSAGE = "MESSAGE";
	private static final String TAG = JSONParserLatestUpdateTimeCustomerUtils.class.getSimpleName();
	
	public JSONParserLatestUpdateTimeCustomerUtils() {

	}
	
	public ArrayList<CustomerInfo> parseCustomer(String data){
		JSONArray orders = null;
		
		ArrayList<CustomerInfo> statusList = new ArrayList<CustomerInfo>();
		try{
			JSONObject json = new JSONObject(data);
			
			orders = json.getJSONArray(TAG_DATA);
			
			for(int i = 0 ; i < orders.length(); i++){
				JSONObject ob = orders.getJSONObject(i);
				String id = ob.getString(TAG_CUSTOMER_ID);
				String conditionsInForntOfHouse = ob.getString(TAG_CONDITIONS_IN_FORNT_OF_HOUSE);
				String message = ob.getString(TAG_MESSAGE);
				
				CustomerInfo item = new CustomerInfo();
				
				item.setCustomerId(id);
				item.setConditionsInForntOfHouse(conditionsInForntOfHouse);
				item.setMessage(message);
				
				statusList.add(item);
			}
		}catch(Exception e){
			Log.e(TAG, "" +  e.toString());
		}
		return statusList;
	}
}
