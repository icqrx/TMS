package jp.co.isb.tms.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StoragePref {

	public static void saveLoginData(Context context) {
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putString(Def.SOSHIKI_CD, TMSConstrans.mSoshiki_cd);
		editor.putString(Def.DRIVER_CD, TMSConstrans.mDriver_code);		
		editor.putString(Def.ROOT_URL_API,TMSConstrans.mRoot_url_api);
		editor.putInt(Def.TIMEOUT, TMSConstrans.mTimeout);
		editor.putString(Def.START_TIME_GPS,TMSConstrans.mStart_time_gps);
		editor.putInt(Def.AUTO_GPS_INTERVAL,TMSConstrans.mAuto_gps_interval);
		editor.putBoolean(Def.AUTO_GPS_FLAG,TMSConstrans.mAuto_gps_flag);
		editor.putString(Def.END_TIME_GPS,TMSConstrans.mEnd_time_gps);
		
		editor.putInt(Def.EXECUTION_TIME_LIMIT, TMSConstrans.mExecution_time_limit);
		editor.putBoolean(Def.BARCODE_CHECK_FLAG,TMSConstrans.mBarcode_check_flag);
		editor.putInt(Def.AUTO_SYNC_TIME_DEVICE, TMSConstrans.mAuto_sync_time_device);
		editor.putBoolean(Def.ALERT_FLAG,TMSConstrans.mAlert_flag);
		editor.putString(Def.LAST_SYNC_DATE_TIME,TMSConstrans.mLastSyncDateTime);
		editor.putInt(Def.MOCHIDASHI_FLG,TMSConstrans.MOCHIDASHI_FLG);
		editor.putInt(Def.AVERAGE_DELIVERY_INTERVAL,TMSConstrans.mAverageDeliveryInterval);
		editor.putInt(Def.SHUTSUGEN_KYORI,TMSConstrans.SHUTSUGEN_KYORI);
		editor.putBoolean(Def.DISTANCE_FLAG,TMSConstrans.mDistance_flag);
		
		editor.commit();
	}
	
	public static void saveLastSyncDateTime(Context context) {
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putString(Def.LAST_SYNC_DATE_TIME, TMSConstrans.mLastSyncDateTime);
		
		editor.commit();
	}
	
	public static void saveAverageDeliveryInterval(Context context) {
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.putInt(Def.AVERAGE_DELIVERY_INTERVAL,TMSConstrans.mAverageDeliveryInterval);
		
		editor.commit();
	}
	public static void loadConfig(Context context) {
		SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		TMSConstrans.mSoshiki_cd = app_preferences.getString(Def.SOSHIKI_CD, "");
		TMSConstrans.mDriver_code = app_preferences.getString(Def.DRIVER_CD, "");
		TMSConstrans.mRoot_url_api = app_preferences.getString(Def.ROOT_URL_API, "");
		TMSConstrans.mTimeout = app_preferences.getInt(Def.TIMEOUT, 0);
		TMSConstrans.mStart_time_gps = app_preferences.getString(Def.START_TIME_GPS, "");
		TMSConstrans.mEnd_time_gps = app_preferences.getString(Def.END_TIME_GPS, "");
		
		TMSConstrans.mExecution_time_limit = app_preferences.getInt(Def.EXECUTION_TIME_LIMIT, 0);
		TMSConstrans.mBarcode_check_flag = app_preferences.getBoolean(Def.BARCODE_CHECK_FLAG, false);
		TMSConstrans.mAuto_sync_time_device = app_preferences.getInt(Def.AUTO_SYNC_TIME_DEVICE, 0);
		TMSConstrans.mAlert_flag = app_preferences.getBoolean(Def.ALERT_FLAG, false);
		TMSConstrans.mAuto_gps_interval = app_preferences.getInt(Def.AUTO_GPS_INTERVAL, 0);
		TMSConstrans.mAuto_gps_flag = app_preferences.getBoolean(Def.AUTO_GPS_FLAG, false);
		TMSConstrans.mLastSyncDateTime = app_preferences.getString(Def.LAST_SYNC_DATE_TIME, "");
		TMSConstrans.MOCHIDASHI_FLG = app_preferences.getInt(Def.MOCHIDASHI_FLG, 0);
		TMSConstrans.mAverageDeliveryInterval = app_preferences.getInt(Def.AVERAGE_DELIVERY_INTERVAL, 8);
		TMSConstrans.SHUTSUGEN_KYORI = app_preferences.getInt(Def.SHUTSUGEN_KYORI, 0);
		TMSConstrans.mDistance_flag = app_preferences.getBoolean(Def.DISTANCE_FLAG, false);
	}
}
