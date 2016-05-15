package jp.co.isb.tms.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.IGetAndroidStatus;
import jp.co.isb.tms.common.INotifyChange;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.common.UserInfoPref;
import jp.co.isb.tms.model.CarryOutStatusMaster;
import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.CustomerPicInfo;
import jp.co.isb.tms.model.DriverInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.model.OrderStatusInfo;
import jp.co.isb.tms.util.HttpUtils;
import jp.co.isb.tms.util.JSONParserCarryOutStatusMasterUtils;
import jp.co.isb.tms.util.JSONParserCustomerImageUtils;
import jp.co.isb.tms.util.JSONParserCustomerUtils;
import jp.co.isb.tms.util.JSONParserDriverUtils;
import jp.co.isb.tms.util.JSONParserLatestUpdateTime;
import jp.co.isb.tms.util.JSONParserLatestUpdateTimeCustomerUtils;
import jp.co.isb.tms.util.JSONParserOrderInfoUtils;
import jp.co.isb.tms.util.JSONParserStatusUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Receive {
	public static String TAG = Receive.class.getSimpleName();
	private static Context mContext;	
	private static BufferedReader mReader;
	
	public Receive(Context context) {
		this.mContext = context;
	}
	/*
	 * Load JSON from Asset
	 */
	public  String loadJSONFromAsset(Context context, String fileName) {
	    String json = null;
	    try {

	        InputStream is = context.getAssets().open(fileName);

	        int size = is.available();

	        byte[] buffer = new byte[size];

	        is.read(buffer);

	        is.close();

	        json = new String(buffer, "UTF-8");


	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    return json;

	}
	
	/*
	 * Load JSON from server
	 */
	public static String requestToServer(String tableAPI) {
		if (TMSConstrans.iSync && TMSConstrans.isTimeout) {
			return "";
		}
		BufferedReader mReader;
		StringBuffer response = null;
		try {
			//URL url = new URL(Def.SERVER_BASE_URI + "/animals?haiso_date=" + time;
			URL url = new URL(tableAPI);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(Def.HTTP_METHOD_GET);
			connection.setReadTimeout(20000);
			connection.setDoInput(true);
			String httpHeader = "user_cd=" + UserInfoPref.getPrefUserCode(mContext)+ "&user_type=driver&access_token=" + UserInfoPref.getPrefAccessToken(mContext);
//			Log.d(TAG, "RequestHeader = " + httpHeader);
			connection.setRequestProperty("Authentication", httpHeader);
			InputStream input = connection.getInputStream();
			mReader = new BufferedReader(new InputStreamReader(input));
			response = new StringBuffer();
			String line;
			while ((line = mReader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
		} catch (Exception e) {
			TMSConstrans.isTimeout = true;
			e.printStackTrace();
		}
		if (response == null) {
			return null;
		}
		
		return response.toString();
	}
	
//	/*
//	 * get list CustomerInfo
//	 */
//	public ArrayList<CustomerInfo> getCustomerInfo(String customerCode) {
//		String customerJson = requestToServer(Def.SERVER_BASE_URI + Def.CUSTOMER_API +"?soshiki_cd=0000000001&kokyaku_cd=" + customerCode);
//		
//		ArrayList<CustomerInfo> list= new ArrayList<CustomerInfo>();
//		JSONParserCustomerUtils jsonParse = new JSONParserCustomerUtils();
//
//		list = jsonParse.parseCustomer(customerJson);
//		
//		return list;
//	}
	/*
	 * get list CustomerInfo
	 */
	public CustomerInfo  getLatestUpdateTimeCustomerInfo(String customerCode) {
		String js = requestToServer(Def.SERVER_BASE_URI + Def.CUSTOMER_LATEST_API + "?soshiki_cd=" + TMSConstrans.mSoshiki_cd + "&kokyaku_cd=" + customerCode);
		if(js == null){
			return null;
		}
		CustomerInfo customerInfo = new CustomerInfo();
		JSONParserCustomerUtils parser = new JSONParserCustomerUtils();
		ArrayList<CustomerInfo> list = parser.parseCustomer(js);
		if (list.size() > 0) {
			customerInfo = list.get(0);
		}
		return customerInfo;
	}
	public CustomerPicInfo getLatestUpdateTimeCustomerPicInfo(String customerCode, String imageCode) {
		CustomerPicInfo customerPicInfo = new CustomerPicInfo();
		String js = requestToServer(Def.SERVER_BASE_URI + Def.CUSTOMER_PIC_API + "?soshiki_cd=" + TMSConstrans.mSoshiki_cd + "&kokyaku_cd=" + customerCode +"&image_cd=" +imageCode);
		try {
			JSONObject json = new JSONObject(js);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customerPicInfo;
	}
	
	/*
	 * Get list CustomerPicInfo
	 */
	public ArrayList<CustomerPicInfo> getCustomerPicInfo(String customerCD) {
		String customerPicJson = requestToServer(Def.SERVER_BASE_URI + 
				Def.CUSTOMER_IMAGES_API + "?soshiki_cd=" +  TMSConstrans.mSoshiki_cd + "&kokyaku_cd=" + customerCD);
		// todo ptlinh
		ArrayList<CustomerPicInfo> list= new ArrayList<CustomerPicInfo>();
		JSONParserCustomerImageUtils jsonParse = new JSONParserCustomerImageUtils();

		list = jsonParse.parseCustomerPic(customerPicJson);
		
		return list;
	}
	
	/*
	 * 
	 */
	public ArrayList<DriverInfo> getDriverMaster(String organizationMaster) {
		String driverMasterJson = requestToServer(Def.SERVER_BASE_URI + Def.DRIVER_API + "?soshiki_cd=" + organizationMaster);
		
		ArrayList<DriverInfo> listDriverInfo= new ArrayList<DriverInfo>();
		JSONParserDriverUtils jsonParse = new JSONParserDriverUtils();

		listDriverInfo = jsonParse.parseDriverMaster(driverMasterJson);
		
		return listDriverInfo;
	}
	
	/*
	 * 
	 */
	public ArrayList<CarryOutStatusMaster> getCarryOutMaster(String organizationMaster) {
		ArrayList<CarryOutStatusMaster> listCarryOut= new ArrayList<CarryOutStatusMaster>();
		String carryOutJson = requestToServer(Def.SERVER_BASE_URI + Def.CARRY_OUT_STATUS + "?soshiki_cd=" + organizationMaster);
		if (carryOutJson == null) {
			return null;
		}
		
		JSONParserCarryOutStatusMasterUtils jsonParse = new JSONParserCarryOutStatusMasterUtils();

		listCarryOut = jsonParse.parseCarryOut(carryOutJson);
		
		return listCarryOut;
	}
	
	/*
	 * 
	 */
	public ArrayList<OrderStatusInfo> getStatusMaster(String organizationMaster) {
		ArrayList<OrderStatusInfo> listStatusInfo= new ArrayList<OrderStatusInfo>();
		
		String json = requestToServer(Def.SERVER_BASE_URI +Def.STATUS_API +"?soshiki_cd=" + organizationMaster);
		
		if (json == null) {
			return null;
		}

		JSONParserStatusUtils jsonParse = new JSONParserStatusUtils();
		listStatusInfo = jsonParse.parseStatusInfo(json);
		
		return listStatusInfo;
	}
	
	/*
	 * 
	 */
	public OrderInfo getOrderLastestUpdateTime(String organizationCode, String haisoBatchCode) {
		OrderInfo order = new OrderInfo();
		
		String deliveryOrder = requestToServer(Def.SERVER_BASE_URI + Def.LATEST_UPDATE_TIME_API +  "?soshiki_cd=" + organizationCode +"&haiso_batch_cd=" + haisoBatchCode);
		Log.d("TAG", deliveryOrder);
		JSONParserLatestUpdateTime jsonParse = new JSONParserLatestUpdateTime();
		order = jsonParse.parseLastUpdateTime(deliveryOrder);
		
		return order;
	}
	
	/*
	 * 
	 */
	public ArrayList<OrderInfo> getDeliveryBatchData(String currentDate) {
		ArrayList<OrderInfo> listDeliveryOrder = new ArrayList<OrderInfo>();
		
		String deliveryOrder = requestToServer(Def.SERVER_BASE_URI + Def.DELIVERY_BACTH_API + "?soshiki_cd=" + TMSConstrans.mSoshiki_cd + "&haiso_date=" + currentDate);

		JSONParserOrderInfoUtils jsonParse = new JSONParserOrderInfoUtils();

		listDeliveryOrder = jsonParse.parseDeliveryOrder(deliveryOrder);

		return listDeliveryOrder;
	}
	
	public static class ReceiveObject extends AsyncTask<Void, Void, Integer> {
		private int mWhatApi;
		private INotifyChange mNotifyChange;
		private String[] mParams;
		private IGetAndroidStatus mAndroidStatus;
		
		public ReceiveObject(int aWhatApi, INotifyChange iNotifyChange, String...params) {
			this.mWhatApi = aWhatApi;
			this.mParams = params;
			this.mNotifyChange = iNotifyChange;
		}
		
		public ReceiveObject(int aWhatApi, INotifyChange iNotifyChange,
				IGetAndroidStatus iAndroidStatus, String... params) {
			this.mWhatApi = aWhatApi;
			this.mNotifyChange = iNotifyChange;
			this.mAndroidStatus = iAndroidStatus;
			this.mParams = params;
		}
		
		@Override
		protected void onPreExecute() {
			mNotifyChange.notify(0); 
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			if (mWhatApi == Def.HTTP_GET_CIMAGES) {
				return HttpUtils.getCustomImage(mParams);
			} else if (mWhatApi == Def.HTTP_ANDROID_STATUS) {
				return HttpUtils.getAndroidStatus(mParams);
			}
			return HttpUtils.getLatestUpdateTime(mParams);
		}
		@Override
		protected void onPostExecute(Integer result) {
			Log.d(TAG, "onPostExecute: " + result);
			if (mWhatApi == Def.HTTP_ANDROID_STATUS) {
				mAndroidStatus.sendAndroidStatus(HttpUtils.isAndroidStatus());
				mAndroidStatus.sendDeliverySetCode(HttpUtils.getDeliverySetCode());
			}
			mNotifyChange.notify(result);
			
		}
	}

	public String getSystemTime() {
		// TODO Auto-generated method stub
//		return "2013-11-22";
		String time ="";
		String json = requestToServer(Def.SERVER_BASE_URI + "/api/systems/get_datetime");
		try {
			JSONObject ob = new JSONObject(json);
			time = ob.getString("data");
//			String array[] = time.split(" ");
//			time = array[0];
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
//	public ArrayList<CustomerPicInfo> getCustomerPic() {
//		ArrayList<CustomerPicInfo> list = new ArrayList<CustomerPicInfo>();
//
//		String json = requestToServer(Def.SERVER_BASE_URI + Def.DELIVERY_BACTH_API + "?soshiki_cd=0000000001&haiso_date=" + currentDate);
//
//		JSONParserOrderInfoUtils jsonParse = new JSONParserOrderInfoUtils();
//
//		listDeliveryOrder = jsonParse.parseDeliveryOrder(deliveryOrder);
//
//		return listDeliveryOrder;
//	}
}
