package jp.co.isb.tms.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.TMSConstrans;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

public class HttpUtils {

	private static final String TAG = HttpUtils.class.getSimpleName();
	final protected static char[] hexArray = Def.HEX_STRING.toCharArray();
	private static String accessToken;
	private static boolean androidStatus;
	private static String deliverySetCode;
	private static BufferedReader mReader;
	private static String receiveData;;
	private static boolean isTransfered;

	public static int getStatus(String aJson) {
		try {
			JSONObject jObject = new JSONObject(aJson);
			String status = jObject.getString(Def.STATUS);
			return Integer.valueOf(status);
		} catch (JSONException e) {
			Log.e(TAG, "" + e.toString());
		}
		return Def.REQUEST_INVALID;
	}

	public static String getAccessToken(String aJson) {
		if (getStatus(aJson) != HttpURLConnection.HTTP_OK){
			return "";
		}
		try {
			JSONObject jObject = new JSONObject(aJson);
			if (jObject.has(Def.DATA)) {
				String data = jObject.getString(Def.DATA);
				if (data != null) {
					JSONObject jObj = new JSONObject(data);
					if (jObj.has(Def.ACCESS_TOKEN)) {
						return jObj.getString(Def.ACCESS_TOKEN);
					}
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, "" + e.toString());
		}

		return "";
	}
	public static String getLoginData(String aJson) {
		if (getStatus(aJson) != HttpURLConnection.HTTP_OK){
			return "";
		}
		try {
			JSONObject jObject = new JSONObject(aJson);
			if (jObject.has(Def.DATA)) {
				String data = jObject.getString(Def.DATA);
				if (data != null) {
					JSONObject jObj = new JSONObject(data);
					if (jObj.has("soshiki_cd")){
						TMSConstrans.mSoshiki_cd = jObj.getString("soshiki_cd");	
						TMSConstrans.mDriver_code = jObj.getString("user_cd");
					}
					//String access_token = jObject.getString("access_token");
					//JSONObject ob = new JSONArray("api_config");
					String api = jObj.getString("api_config");
					JSONObject ob = new JSONObject(api);
					
					int execution_time_limit = ob.getInt("execution_time_limit");
					boolean barcode_check_flag = ob.getBoolean("barcode_check_flag");
					int auto_sync_time_device = ob.getInt("auto_sync_time_device");
					int auto_gps_interval = ob.getInt("auto_gps_interval");
					boolean auto_gps_flag = ob.getBoolean("auto_gps_flag");
					boolean alert_flag = ob.getBoolean("alert_flag");
					String root_url_api = ob.getString("root_url_api");
					int timeout = ob.getInt("timeout");
					String start_time_gps = ob.getString("start_time_gps");
					String end_time_gps = ob.getString("end_time_gps");
					boolean distance_flag = ob.getBoolean("distance_flag");
					
					int mochidashi_flg = jObj.getInt("mochidashi_flg");
					int shutsugen_kyori = jObj.getInt("shutsugen_kyori");
					
					TMSConstrans.mExecution_time_limit = execution_time_limit;
					TMSConstrans.mBarcode_check_flag = barcode_check_flag;
					TMSConstrans.mAuto_sync_time_device = auto_sync_time_device;
					TMSConstrans.mAlert_flag = alert_flag;
					TMSConstrans.mAuto_gps_interval = auto_gps_interval;
					TMSConstrans.mAuto_gps_flag = auto_gps_flag;
					TMSConstrans.mTimeout = timeout;
					TMSConstrans.mRoot_url_api = root_url_api;
					TMSConstrans.mEnd_time_gps = end_time_gps;
					TMSConstrans.mStart_time_gps = start_time_gps;
					TMSConstrans.MOCHIDASHI_FLG = mochidashi_flg;
					TMSConstrans.SHUTSUGEN_KYORI = shutsugen_kyori;
					TMSConstrans.mDistance_flag = distance_flag;
				}
			}
			
		} catch (JSONException e) {
			Log.e(TAG, "" + e.toString());
		}
		
		return "";
	}

	public static String convertinputStreamToString(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			Log.e(TAG, "" + e.toString());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Log.e(TAG, "" + e.toString());
				}
			}
		}
		return sb.toString();
	}
	
	public static boolean getDriverIsOpened(String aJson) {
		try {
			JSONObject jObject = new JSONObject(aJson);
			String data = jObject.getString(Def.DATA);
			if (data != null) {
				JSONObject jObj = new JSONObject(data);
				return jObj.getBoolean(Def.IS_OPEN);
			}
		} catch (JSONException e) {
			Log.e(TAG, "" + e.toString());
		}
		return false;
	}
	
	public static String getDeliverySetCode(String aJson) {
		try {
			JSONObject jObject = new JSONObject(aJson);
			String data = jObject.getString(Def.DATA);
			if (data != null) {
				JSONObject jObj = new JSONObject(data);
				if (jObj.has(Def.IS_OPEN) && jObj.getBoolean(Def.IS_OPEN)) {
					return jObj.getString(Def.HAISO_SET_CD);
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, "" + e.toString());
		}
		return "";
	}
	
	public static Boolean getTransferResult(String aJson) {
		try {
			JSONObject jObject = new JSONObject(aJson);
			if (jObject.has(Def.DATA)) {
				return Boolean.valueOf(jObject.getString(Def.DATA));
			}
		} catch (JSONException e) {
			Log.e(TAG, "" + e.toString());
		}
		return false;
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	@SuppressLint("DefaultLocale")
	public static String hmacSHA256(String key, String data) throws Exception {
		Mac sha256_HMAC = Mac.getInstance(Def.HMAC_SHA256);
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), Def.HMAC_SHA256);
		sha256_HMAC.init(secret_key);
		
		return bytesToHex(sha256_HMAC.doFinal(data.getBytes())).toLowerCase();
	}
	
	public static StringBuilder createStringBuilderForUpload(byte[] imageData, String... params){
		StringBuilder builder = new StringBuilder();
		builder.append("soshiki_cd=").append(params[0]);
		builder.append("&kokyaku_cd=").append(params[1]);
		builder.append("&image_upload=").append(imageData);
		builder.append("&comment=").append(params[2]);
		return builder;
	}
	
	public static StringBuilder createStringBuilder(int option, String... params){
		StringBuilder builder = new StringBuilder();
		switch(option) {
		case Def.HTTP_LOGIN:
			builder.append("user_cd=").append(params[0]);
	    	builder.append("&password=").append(params[1]);
			break;
		case Def.HTTP_LOGOUT:
			builder.append("user_cd=").append(params[0]);
	    	builder.append("&user_type=").append("driver");
			break;
		case Def.HTTP_TRANSFER:
			builder.append("soshiki_cd=").append(params[0]);
			builder.append("&haiso_batch_cd=").append(params[1]);
			builder.append("&haiso_set_cd=").append(params[2]);
			break;
		case Def.HTTP_ANDROID_STATUS:
			builder.append("soshiki_cd=").append(params[0]);
			builder.append("&driver_cd=").append(params[1]);
			break;
		case Def.HTTP_UPLOAD_COMMENT:
			builder.append("soshiki_cd=").append(params[0]);
			builder.append("&kokyaku_cd=").append(params[1]);
			builder.append("&image_cd=").append(params[2]);
			builder.append("&comment=").append(params[3]);
			break;
		case Def.HTTP_GPS:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&lat=").append(params[3]);
			builder.append("&log=").append(params[4]);
			builder.append("&ip_address=").append(params[5]);
			builder.append("&belieabability=").append(params[6]);
			builder.append("&haiso_date=").append(params[7]);
			builder.append("&preset_cd=").append(params[8]);
			builder.append("&preset_nm=").append(params[9]);
			builder.append("&haiso_set_cd=").append(params[10]);
			builder.append("&haiso_set_nm=").append(params[11]);
			builder.append("&route_cd=").append(params[12]);
			builder.append("&route_nm=").append(params[13]);
			builder.append("&created=").append(params[14]);
			builder.append("&created_driver=").append(params[15]);
			break;
		case Def.HTTP_DBATCH_UPDATE_ESTIMATED:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&haiso_batch_cd=").append(params[3]);
			builder.append("&mtd_flg=").append(params[4]);
			builder.append("&mtd_status_cd=").append(params[5]);
			builder.append("&haiso_yotei_time=").append(params[6]);
			builder.append("&haiso_shuryo_time=").append(params[7]);
			builder.append("&status_cd=").append(params[8]);
			builder.append("&haiso_kaisu=").append(params[9]);
			builder.append("&modified_driver=").append(params[10]);
			builder.append("&modified_driver_cd=").append(params[11]);
			break;
		case Def.HTTP_DBATCH_UPDATE:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&haiso_batch_cd=").append(params[3]);
			builder.append("&modified_driver=").append(params[4]);
			builder.append("&status=").append(params[5]);
			builder.append("&driverDateTime=").append(params[6]);
			break;
		case Def.HTTP_DBATCH_UPDATE_AVEGARE_INTERVAL_TIME:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&haiso_kankaku=").append(params[3]);
			break;
		case Def.HTTP_REGISTER_DBATCH_STATUS:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&haiso_batch_cd=").append(params[3]);
			builder.append("&kukuri_batch_cd=").append(params[4]);
			
			builder.append("&mtd_flg=").append(params[5]);
			builder.append("&mtd_status_cd=").append(params[6]);
			
			builder.append("&haiso_date=").append(params[7]);
			builder.append("&modified=").append(params[8]);
			builder.append("&modified_driver=").append(params[9]);
			builder.append("&preset_cd=").append(params[10]);
			builder.append("&preset_nm=").append(params[11]);
			builder.append("&haiso_set_cd=").append(params[12]);
			builder.append("&haiso_set_nm=").append(params[13]);
			builder.append("&route_cd=").append(params[14]);
			builder.append("&route_nm=").append(params[15]);
			builder.append("&ninushi_cd=").append(params[16]);
			builder.append("&ninushi_kugiri_cd=").append(params[17]);
			builder.append("&toi_no=").append(params[18]);
			builder.append("&kokyaku_cd=").append(params[19]);
			builder.append("&zip_cd=").append(params[20]);
			builder.append("&koguchi_num=").append(params[21]);
			builder.append("&shukka_date=").append(params[22]);
			builder.append("&konpo_yoryo_s=").append(params[23]);
			builder.append("&konpo_yoryo_m=").append(params[24]);
			builder.append("&daibiki_flg=").append(params[25]);
			
			//builder.append("&konpo_juryo=").append(params[24]);
			
			builder.append("&zeinuki_kin=").append(params[26]);
			builder.append("&shohizei_kin=").append(params[27]);
			builder.append("&seikyu_kin=").append(params[28]);
			builder.append("&denpyo_no=").append(params[29]);
			
			builder.append("&daibiki_tesuryo_kin=").append(params[30]);
			builder.append("&status_cd=").append(params[31]);
			
			builder.append("&haiso_yotei_time=").append(params[32]);
			builder.append("&haiso_shuryo_time=").append(params[33]);
			builder.append("&haiso_kaisu=").append(params[34]);
			
//			StringBuilder builder = new StringBuilder();
//			
//			builder.append("soshiki_cd=").append(TMSConstrans.mSoshiki_cd);
//			builder.append("&haiso_batch_cd=").append(deliveryBatchCode);
//			builder.append("&kukuri_batch_cd=").append(bundleBatchCode);
//			builder.append("&haiso_date=").append(deliveryDate);
//			builder.append("&modified=").append(updatedDateTime);
//			builder.append("&modified_driver=").append(modifiedDriverDatetime);
//			builder.append("&preset_cd=").append(presetCode);
//			builder.append("&preset_nm=").append(presetName);
//			builder.append("&haiso_set_cd=").append(deliverySetCode);
//			builder.append("&haiso_set_nm=").append(deliverySetName);
//			builder.append("&route_cd=").append(routeCode);
//			builder.append("&route_nm=").append(routeName);
//			builder.append("&ninushi_cd=").append(shipperCode);
//			builder.append("&ninushi_kugiri_cd=").append(shipperTypeCode);
//			builder.append("&toi_no=").append(trackingID);
//			builder.append("&kokyaku_cd=").append(customerCD);
//			builder.append("&zip_cd=").append(customerPostalCode);
//			builder.append("&koguchi_num=").append(packageQuantity);
//			builder.append("&shukka_date=").append(StockOutDate);
//			builder.append("&konpo_yoryo_s=").append(squareMeterOfPackage);
//			builder.append("&konpo_yoryo_m=").append(cubicMeterOfPackage);
//			builder.append("&daibiki_flg=").append(cashOnDeliveryFlag);
//			
//			//builder.append("&konpo_juryo=").append(params[24]);
//			
//			builder.append("&zeinuki_kin=").append(subTotalBeforeTax);
//			builder.append("&shohizei_kin=").append(consumptionTax);
//			builder.append("&seikyu_kin=").append(billingAmount);
//			builder.append("&denpyo_no=").append(StockOutPaySlipNumber);
//			builder.append("&daibiki_tesuryo_kin").append(CODCommission);
//			builder.append("&status_cd").append(statusCode);
//			
//			builder.append("&haiso_yotei_time=").append(deliveryEstimatedDateTime);
//			builder.append("&haiso_shuryo_time=").append(deliveryCompletedDateTime);
//			builder.append("&haiso_kaisu=").append(deliveryTimes);
			break;
		case Def.HTTP_UPDATE_CUSTOMER_API:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&kokyaku_cd=").append(params[3]);
			builder.append("&nokisaki=").append(params[4]);
			builder.append("&modified_driver=").append(params[5]);
			break;
		case Def.HTTP_UPDATE_COMMENT_API:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&kokyaku_cd=").append(params[3]);
			builder.append("&image_cd=").append(params[4]);
			builder.append("&comment=").append(params[5]);
			break;
		case Def.HTTP_UPLOAD_IMAGE:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&kokyaku_cd=").append(params[3]);
			builder.append("&modified_driver=").append(params[4]);
			builder.append("&comment=").append(params[5]);
			break;
		case Def.HTTP_DBATCH_UPDATE_DISPLAY:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&haiso_batch_cd=").append(params[3]);
			builder.append("&order_num=").append(params[4]);
			builder.append("&modified_driver=").append(params[5]);
			builder.append("&kukuri_batch_cd=").append(params[6]);
			break;
		case Def.HTTP_DBATCH_UPDATE_SERVER:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&haiso_batch_cd=").append(params[3]);
			builder.append("&mtd_status_cd=").append(params[4]);
			builder.append("&modified_driver=").append(params[5]);
			break;
		case Def.HTTP_UPLOAD_COMMENTS:
			builder.append("soshiki_cd=").append(params[2]);
			builder.append("&kokyaku_cd=").append(params[3]);
			builder.append("&image_cd=").append(params[4]);
			builder.append("&modified_driver=").append(params[5]);
			builder.append("&comment=").append(params[6]);
			break;
		}
		
		return builder;
	}
	
	public static int loginRequest(String... params){	
    	HttpURLConnection conn = null;    
    	StringBuilder builder = HttpUtils.createStringBuilder(Def.HTTP_LOGIN, params[0], params[1]);
        int result = Def.REQUEST_INVALID;
		try {			
        	URL url = new URL(Def.BASE_LOGIN_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(Def.HTTP_METHOD_POST);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");       
            conn.setRequestProperty("Content-Length", "" + Integer.toString(builder.toString().length()));
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
			wr.writeBytes(builder.toString());			
			wr.flush();
			wr.close();
			Log.d(TAG, "ResponseCode: " + conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                String resultstring = HttpUtils.convertinputStreamToString(in);
                Log.d(TAG, " Resultstring: " + resultstring);
                result = HttpUtils.getStatus(resultstring);
                setAccessToken(HttpUtils.getAccessToken(resultstring));
                HttpUtils.getLoginData(resultstring);
            } 
			builder = null;
			if (conn != null) {
			    conn.disconnect();
			}
	    } catch (MalformedURLException ex) {
	        Log.e(TAG, "error: " + ex.getMessage(), ex);
	    } catch (IOException ioe) {
	        Log.e(TAG, "error: " + ioe.getMessage(), ioe);
	    } catch (Exception ioe) {
	        Log.e(TAG, "error: " + ioe.getMessage(), ioe);
	    } finally {
	    }
        return result;
    }
	
	public static int logoutRequest(String...params){	
    	HttpURLConnection conn = null;    
    	StringBuilder builder = HttpUtils.createStringBuilder(Def.HTTP_LOGOUT, params[0]);
        int result = Def.REQUEST_INVALID;
		try {			
        	conn = setupConnection(Def.BASE_LOGOUT_URL, builder, params);
			Log.d(TAG, "ResponseCode: " + conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                String resultstring = HttpUtils.convertinputStreamToString(in);
                Log.d(TAG, "Resultstring: " + resultstring);
                result = HttpUtils.getStatus(resultstring);
            } 
			builder = null;
			if (conn != null) {
			    conn.disconnect();
			}
	    } catch (MalformedURLException ex) {
	        Log.e(TAG, "error: " + ex.getMessage(), ex);
	    } catch (IOException ioe) {
	        Log.e(TAG, "error: " + ioe.getMessage(), ioe);
	    } catch (Exception ioe) {
	        Log.e(TAG, "error: " + ioe.getMessage(), ioe);
	    } finally {
	    }
        return result;
    }
	
	public static int transferRequest(String...params){	
    	HttpURLConnection conn = null;    
    	StringBuilder builder = HttpUtils.createStringBuilder(Def.HTTP_TRANSFER, params[2], params[3], params[4]);
        int result = Def.REQUEST_INVALID;
		try {			
        	conn = setupConnection(Def.BASE_DBATCHS_TRANSFER_URL, builder, params[0], params[1]);
			Log.d(TAG, "ResponseCode: " + conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                String resultstring = HttpUtils.convertinputStreamToString(in);
                Log.d(TAG, "Resultstring: " + resultstring);
                result = HttpUtils.getStatus(resultstring);
                setTransfered(getTransferResult(resultstring));
            } 
			builder = null;
			if (conn != null) {
			    conn.disconnect();
			}
	    } catch (MalformedURLException ex) {
	        Log.e(TAG, "error: " + ex.getMessage(), ex);
	    } catch (IOException ioe) {
	        Log.e(TAG, "error: " + ioe.getMessage(), ioe);
	    } catch (Exception ioe) {
	        Log.e(TAG, "error: " + ioe.getMessage(), ioe);
	    } finally {
	    }
        return result;
    }
	
	public static int request(int typeRequest, String LinkAPI, String...params){	
		if (TMSConstrans.iSync && TMSConstrans.isTimeout) {
			return Def.REQUEST_INVALID;
		}
    	HttpURLConnection conn = null;    
    	StringBuilder builder = HttpUtils.createStringBuilder(typeRequest, params);
    	Log.d(TAG, "Request:" + builder.toString());
        int result = Def.REQUEST_INVALID;
		try {			
        	conn = setupConnection(LinkAPI, builder, params[0], params[1]);
			Log.d(TAG, "ResponseCode: " + conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                String resultstring = HttpUtils.convertinputStreamToString(in);
                Def.JSON = resultstring;
                Log.d(TAG, "Resultstring: " + resultstring);
                result = HttpUtils.getStatus(resultstring);
            } 

			builder = null;
			if (conn != null) {
			    conn.disconnect();
			}
	    } catch (Exception e) {
	    	TMSConstrans.isTimeout = true;
	    	e.printStackTrace();
	    }
        return result;
    }
	public static int request1(int typeRequest, String LinkAPI, FileInputStream fileInputStream,String...params){	
		if (TMSConstrans.iSync && TMSConstrans.isTimeout) {
			return Def.REQUEST_INVALID;
		}
		
    	HttpURLConnection conn = null;    
    	StringBuilder builder = HttpUtils.createStringBuilder(typeRequest, params);
    	Log.d(TAG, "Request:" + builder.toString());
        int result = Def.REQUEST_INVALID;
		try {			

			conn = setupConnection1(fileInputStream,LinkAPI, builder, params);
			Log.d(TAG, "ResponseCode: " + conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                String resultstring = HttpUtils.convertinputStreamToString(in);
                Def.JSON = resultstring;
                Log.d(TAG, "Resultstring: " + resultstring);
                result = HttpUtils.getStatus(resultstring);
            } 

			builder = null;
			if (conn != null) {
			    conn.disconnect();
			}
	    } catch (Exception e) {
	    	TMSConstrans.isTimeout = true;
	    	e.printStackTrace();
	    }
        return result;
    }
	
	public static int uploadImagesRequest(byte[] imageData, String...params){	
		if (TMSConstrans.iSync && TMSConstrans.isTimeout) {
			return Def.REQUEST_INVALID;
		}
    	HttpURLConnection conn = null;    
    	StringBuilder builder = HttpUtils.createStringBuilderForUpload(imageData, params[2], params[3], params[4]);
        int result = Def.REQUEST_INVALID;
		try {			
        	conn = setupConnection(Def.BASE_UPLOAD_IMAGES_URL, builder, params[0], params[1]);
			Log.d(TAG, "ResponseCode: " + conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                String resultstring = HttpUtils.convertinputStreamToString(in);
                Log.d(TAG, "Resultstring: " + resultstring);
                result = getStatus(resultstring);
            } 
			builder = null;
			if (conn != null) {
			    conn.disconnect();
			}
	    }  catch (Exception e) {
	    	TMSConstrans.isTimeout = true;
	    	e.printStackTrace();
	    }
        return result;
    }
	
	public static int updateCommentRequest(String...params){	
		if (TMSConstrans.iSync && TMSConstrans.isTimeout) {
			return Def.REQUEST_INVALID;
		}
    	HttpURLConnection conn = null;    
    	StringBuilder builder = HttpUtils.createStringBuilder(Def.HTTP_UPLOAD_COMMENT, params[2], params[3], params[4]);
        int result = Def.REQUEST_INVALID;
		try {			
        	conn = setupConnection(Def.BASE_UPLOAD_COMMENT_URL, builder, params[0], params[1]);
			Log.d(TAG, "ResponseCode: " + conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                String resultstring = HttpUtils.convertinputStreamToString(in);
                Log.d(TAG, "Resultstring: " + resultstring);
                result = getStatus(resultstring);
            } 
			builder = null;
			if (conn != null) {
			    conn.disconnect();
			}
	    }  catch (Exception e) {
	    	TMSConstrans.isTimeout = true;
	    	e.printStackTrace();
	    }
        return result;
    }

	private static HttpURLConnection setupConnection(String aUrl,
			StringBuilder builder, String... params)
			throws MalformedURLException, IOException, ProtocolException {
		HttpURLConnection conn;
		URL url = new URL(aUrl);
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod(Def.HTTP_METHOD_POST);
		conn.setRequestProperty("Connection", "Keep-Alive");
		//conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		conn.setRequestProperty("Accept-Language", "jp"); 
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");       
		//conn.setRequestProperty("Content-Length", "" + Integer.toString(builder.toString().length()));
		String httpHeader = "user_cd=" + params[0]+ "&user_type=driver&access_token=" + params[1];
		Log.d(TAG, "RequestHeader = " + httpHeader);
		conn.setRequestProperty("Authentication", httpHeader);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
		
		byte[] bytes = builder.toString().getBytes();
		
		for (int i=0;i<bytes.length;i++){
			wr.writeByte(bytes[i]);
		}
				
		wr.flush();
		wr.close();
		return conn;
	}
	
	private static HttpURLConnection setupConnection1(FileInputStream fileInputStream,String aUrl,
			StringBuilder builder, String... params)
			throws MalformedURLException, IOException, ProtocolException {
		final String serverFileName = "test"+ (int) Math.round(Math.random()*1000) + ".jpg";
		final String boundary = "*****";
		final String lineEnd = "\r\n";
		final String twoHyphens = "--";
		HttpURLConnection conn;
		URL url = new URL(aUrl);
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod(Def.HTTP_METHOD_POST);
		conn.setRequestProperty("Connection", "Keep-Alive");
		//conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary+";charset=UTF-8");     
		//conn.setRequestProperty("Content-Length", "" + Integer.toString(builder.toString().length()));
		String httpHeader = "user_cd=" + params[0]+ "&user_type=driver&access_token=" + params[1];
		Log.d(TAG, "RequestHeader = " + httpHeader);
		conn.setRequestProperty("Authentication", httpHeader);
		
		DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
		
		dos.writeBytes(twoHyphens + boundary + lineEnd);
		dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + serverFileName +"\"" + lineEnd);
		dos.writeBytes(lineEnd);

		// create a buffer of maximum size
		int bytesAvailable = fileInputStream.available();
		int maxBufferSize = 1024;
		int bufferSize = Math.min(bytesAvailable, maxBufferSize);
		byte[] buffer = new byte[bufferSize];
		
		// read file and write it into form...
		int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		
		while (bytesRead > 0)
		{
			dos.write(buffer, 0, bufferSize);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		}
				
		dos.writeBytes(lineEnd);
		dos.writeBytes(twoHyphens + boundary + lineEnd);
		
		// Passing soshiki_cd
		dos.writeBytes("Content-Disposition: form-data; name=\"soshiki_cd\"" + lineEnd);
		dos.writeBytes(lineEnd);
		dos.writeBytes(params[2] + lineEnd);
		dos.writeBytes(twoHyphens + boundary + lineEnd);
		
		// Passing kokyaku_cd
		dos.writeBytes("Content-Disposition: form-data; name=\"kokyaku_cd\"" + lineEnd);
		dos.writeBytes(lineEnd);
		dos.writeBytes(params[3] + lineEnd);
		dos.writeBytes(twoHyphens + boundary + lineEnd);
		// Passing modified_driver
		dos.writeBytes("Content-Disposition: form-data; name=\"modified_driver\"" + lineEnd);
		dos.writeBytes(lineEnd);
		dos.writeBytes(params[4] + lineEnd);
		dos.writeBytes(twoHyphens + boundary + lineEnd);
		
		// Passing comment
		dos.writeBytes("Content-Disposition: form-data; name=\"comment\"" + lineEnd);
		dos.writeBytes(lineEnd);
		dos.write(params[5].getBytes("UTF-8"));
		dos.writeBytes(lineEnd);
//		dos.writeUTF(params[5] + lineEnd);
//		String tmp = URLEncoder.encode(params[5] + lineEnd, "Shift_JIS");
		Log.d("COMMENT: ", params[5]);
		dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		//dos.writeBytes(builder.toString());
		fileInputStream.close();
	
		dos.flush();
		dos.close();
		return conn;
	}
	
	public static int getLatestUpdateTime(String...params) {
		int relCode = Def.REQUEST_INVALID;
		String result = null;
		try {
			URL url = new URL(Def.BASE_LATEST_UPDATE_TIME_URL + "?soshiki_cd=" + params[0] + "&kokyaku_cd="
					+ params[1] + "&image_cd=" + params[2]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(Def.HTTP_METHOD_GET);
			connection.setReadTimeout(20000);
			connection.setDoInput(true);
			InputStream input = connection.getInputStream();
			mReader = new BufferedReader(new InputStreamReader(input));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = mReader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			result = response.substring(1, response.length() - 2);
			setReceiveData(result);
			relCode = connection.getResponseCode();
			response = null;
			input.close();
		} catch (Exception e) {
			Log.e(TAG, "" +  e.toString());
		}
		return relCode;
	}
	
	public static int getCustomImage(String...params) {
		int relCode = Def.REQUEST_INVALID;
		String result = null;
		try {
			URL url = new URL(Def.BASE_LATEST_UPDATE_TIME_URL + "?soshiki_cd=" + params[0] + "&kokyaku_cd="	+ params[1]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(Def.HTTP_METHOD_GET);
			connection.setReadTimeout(20000);
			connection.setDoInput(true);
			InputStream input = connection.getInputStream();
			mReader = new BufferedReader(new InputStreamReader(input));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = mReader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			result = response.substring(1, response.length() - 2);
			setReceiveData(result);
			relCode = connection.getResponseCode();
			response = null;
			input.close();
		} catch (Exception e) {
			Log.e(TAG, "" +  e.toString());
		}
		return relCode;
	}
	
	public static int getAndroidStatus(String...params){
		int relCode = Def.REQUEST_INVALID;
		String result = null;
		try {
			URL url = new URL(Def.BASE_ANDROID_STATUS_URL + "?soshiki_cd=" + params[2] + "&driver_cd="	+ params[3]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(Def.HTTP_METHOD_GET);
			connection.setReadTimeout(20000);
			connection.setDoInput(true);
			String httpHeader = "user_cd=" + params[0]+ "&user_type=driver&access_token=" + params[1];
			Log.d(TAG, "RequestHeader = " + httpHeader);
			connection.setRequestProperty("Authentication", httpHeader);
			InputStream input = connection.getInputStream();
			mReader = new BufferedReader(new InputStreamReader(input));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = mReader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			result = response.substring(1, response.length() - 1);
			setAndroidStatus(getDriverIsOpened(result));
            setDeliverySetCode(getDeliverySetCode(result));
			relCode = connection.getResponseCode();
			response = null;
			input.close();
		} catch (Exception e) {
			Log.e(TAG, "" +  e.toString());
		}
		return relCode;    	
    }

	public static String getAccessToken() {
		return accessToken;
	}

	public static void setAccessToken(String accessToken) {
		HttpUtils.accessToken = accessToken;
	}

	public static boolean isAndroidStatus() {
		return androidStatus;
	}

	public static void setAndroidStatus(boolean androidStatus) {
		HttpUtils.androidStatus = androidStatus;
	}

	public static String getReceiveData() {
		return receiveData;
	}

	public static void setReceiveData(String receiveData) {
		HttpUtils.receiveData = receiveData;
	}

	public static String getDeliverySetCode() {
		return deliverySetCode;
	}

	private static void setDeliverySetCode(String driverSetCode) {
		HttpUtils.deliverySetCode = driverSetCode;
	}

	public static boolean isTransfered() {
		return isTransfered;
	}

	public static void setTransfered(boolean isTransfered) {
		HttpUtils.isTransfered = isTransfered;
	}
}
