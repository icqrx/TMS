package jp.co.isb.tms.util;

import java.util.ArrayList;

import jp.co.isb.tms.model.CustomerPicInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class JSONParserCustomerImageUtils {
	
	public static final String TAG_CUSTOMER_MASTER = "M_KOKYAKU_IMG";
	public static final String TAG_DATA = "data";
	public static final String TAG_CUSTOMER_ID= "KOKYAKU_CD";
	public static final String TAG_DSP_SORT= "DSP_SORT";
	public static final String TAG_IMAGE_ID = "IMAGE_CD";
	public static final String TAG_IMAGE_THUMB_DATA = "IMAGE_THUMB_DATA";
	public static final String TAG_COMMENT = "COMMENT";
	public static final String TAG_UPDATED_DATE_TIME= "MODIFIED";
	public static final String TAG_UPDATED_STAFF = "MODIFIED_STAFF";
	public static final String TAG_MODIFIED_DRIVER = "MODIFIED_DRIVER";
	private static final String TAG = JSONParserCustomerImageUtils.class.getSimpleName();
	
	public JSONParserCustomerImageUtils() {

	}
	
	public ArrayList<CustomerPicInfo> parseCustomerPic(String data) {
		if (data == null) {
			return new ArrayList<CustomerPicInfo>();
		}
 
		ArrayList<CustomerPicInfo> statusList = new ArrayList<CustomerPicInfo>();
		
		try{
			JSONObject json = new JSONObject(data);
			
			JSONArray orders = json.getJSONArray(TAG_DATA);
			
			for(int i = 0 ; i < orders.length(); i++){
				JSONObject ob = orders.getJSONObject(i);
				String id = ob.getString(TAG_CUSTOMER_ID);
				String dspSort = ob.getString(TAG_DSP_SORT);
				String imageId = ob.getString(TAG_IMAGE_ID);
				String thumnailData = ob.getString(TAG_IMAGE_THUMB_DATA);
				String comment = ob.getString(TAG_COMMENT);
				String updatedDateTime = ob.getString(TAG_UPDATED_DATE_TIME);
				String updatedStaff = ob.getString(TAG_UPDATED_STAFF);
				String driverUpdateTime = ob.getString(TAG_MODIFIED_DRIVER);
				
				CustomerPicInfo item = new CustomerPicInfo();
				
				item.setCustomerId(id);
				item.setImageId(imageId);
				item.setDspSort(dspSort);
				item.setmThumnailData(thumnailData);
				item.setImageComment(comment);
				item.setUpdatedDateTime(updatedDateTime);
				item.setUpdatedStaff(updatedStaff);
				item.setmDriverUpdateDateTime(driverUpdateTime);
				
				statusList.add(item);
			}
		}catch(Exception e){
			Log.e(TAG, "" +  e.toString());
		}
		return statusList;
	}
}
