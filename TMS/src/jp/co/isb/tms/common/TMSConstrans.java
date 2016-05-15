package jp.co.isb.tms.common;

import java.io.FileInputStream;

import jp.co.isb.tms.ui.BaseActivity.LoadingData;

public class TMSConstrans {
	
	public static LoadingData mLoadData;

	public static String mSoshiki_cd = "";
	public static String mDriver_code = "";
	public static String mRoot_url_api = "";
	public static int mTimeout = 1000 * 30;
	public static String mStart_time_gps = "7:00";
	public static String mEnd_time_gps ="19:00";
	public static int mExecution_time_limit = 1000 * 30;
	public static boolean mBarcode_check_flag = false;
	public static int mAuto_sync_time_device = 1000 * 60 * 5;
	public static boolean mAlert_flag = false;
	public static int mAuto_gps_interval = 1000 * 30;
	
	public static int MOCHIDASHI_FLG = 0;
	
	public static String mLastSyncDateTime = "";
	
	public static boolean mAuto_gps_flag = false;
	public static int mAverageDeliveryInterval = 8;
	public static String pathImage ="";
	public static int SHUTSUGEN_KYORI = 0;
	public static String mPathImage = "";
	public static boolean mDistance_flag = false;
	
	public static boolean iSync = false;
	public static boolean isTimeout = false;

	public static FileInputStream fileInputStream;
}
