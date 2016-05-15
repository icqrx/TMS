package jp.co.isb.tms.common;

public class Def {
	
	public static final String TAG_CARRY_OUT = "carry_out";
	public static final String TAG_CARRY_OUT_RESULT = "carry_out_result";
	public static final String TAG_TOTAL = "total";
	public static final String TAG_ORDER_ITEM = "order_item";
	public static final String TAG_SUMMARY = "summary";
	public static final String TAG_ROUTE = "route";
	public static final String TAG_CUSTOMER_CD = "customer_cd";
	public static final String TAG_DELIVERY_ESTIMATED_DATE_TIME = "DeliveryEstimatedDateTime";
	public static final String TAG_DELIVERY_BATCH_CODE = "DeliveryBatchCode";
	public static final String TAG_POSITION = "Position";
	public static final String TAG_BUNDLE_BATCH_CODE = "BundleBatchCode";
	public static final String TAG_STATUS_ID = "status_id";
	public static final String TAG_PIC_IMAGE_ID = "pic";
	public static final String TAG_PIC1 = "pic1";
	public static final String TAG_PIC_RETURN = "pic_return";
	public static final String TAG_COMMENT_RESULT = "comment_result";
	public static final String TAG_THUMBNAIL_RESULT = "thumbnail_result";
	public static final String TAG_IMAGE_ID = "image_id";
	public static final String TAG_DELETE_ITEM = "delete_item";
	public static final String TAG_DELETE = "delete";
	public static final String TAG_ORDER_ID = "order_id";
	public static final String TAG_PULLDOWN = "pulldown_status";
	
	public static final int REQ_CODE_CARRY_OUT_DETAIL = 100;
	public static final int ZOOM_LEVEL = 14;
	
	public static final char BARCODE_START_CHAR = '%';
	public static final char BARCODE_LAST_CHAR = '$';
	
	public static final String NFC_DATA_TYPE = "application/jp.co.isb.tms.nfc";
	public static final String UFT_8 = "UTF-8";
	
	public final static String staff_code_regex = "[0-9]{1,10}"; // [0-9]{1,10}Staff code contains 10 digitals
	public final static String password_regex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,200})";
	
	public final static int PASSWORD_MAX_LENGTH = 200;
	public final static int STAFF_CODE_MAX_LENGTH = 10;
	
	public final static String PREF_PASSWORD = "password";
	public final static String PREF_USER_CODE = "user_code";
	public final static String USER_ID = "user_id";
	public final static String PREF_AUTO_LOGIN = "AUTO_LOGIN";
	public final static String PREF_ACCESS_TOKEN = "ACCESS_TOKEN";	
	public final static String SOSHIKI_CD = "SOSHIKI_CD";
	public final static String DRIVER_CD = "DRIVER_CD";
	public final static String ROOT_URL_API = "ROOT_URL_API";
	public final static String TIMEOUT = "TIMEOUT";
	public final static String START_TIME_GPS = "START_TIME_GPS";
	public final static String END_TIME_GPS = "END_TIME_GPS";
	public final static String IS_LOG_OUT = "LOGOUT";
	public final static String HMAC_SHA256 = "HmacSHA256";
	public final static String PASSWORD_SECRET = "kasei20131216kasei9";//kasei20131216kasei9
	public final static String HEX_STRING = "0123456789ABCDEF";
	public final static String EXECUTION_TIME_LIMIT = "EXECUTION_TIME_LIMIT";
	public static final String BARCODE_CHECK_FLAG = "BARCODE_CHECK_FLAG";
	public static final String AUTO_SYNC_TIME_DEVICE = "AUTO_SYNC_TIME_DEVICE";
	public static final String ALERT_FLAG = "ALERT_FLAG";
	public static final String AUTO_GPS_INTERVAL = "auto_gps_interval";
	public static final String AUTO_GPS_FLAG = "auto_gps_flag";
	public static final String LAST_SYNC_DATE_TIME = "LAST_SYNC_DATE_TIME";
	public static final String IMAGE_JSON = "IMAGE_JSON";
	public static final String INPUT_STREAM = "INPUT_STREAM";
	public static final String MOCHIDASHI_FLG = "MOCHIDASHI_FLG";
	public static final String AVERAGE_DELIVERY_INTERVAL = "AverageDeliveryInterval";
	public static final String SHUTSUGEN_KYORI = "SHUTSUGEN_KYORI";
	public static final String DISTANCE_FLAG = "Distance_flag";
	
	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_GET = "GET";
	public final static int HTTP_LOGIN = 0;
	public final static int HTTP_LOGOUT = 1;
	public final static int HTTP_TRANSFER = 2;
	public final static int HTTP_ANDROID_STATUS = 3;
	public final static int HTTP_UPLOAD_IMAGES = 4;
	public final static int HTTP_UPLOAD_COMMENT = 5;
	public final static int HTTP_GET_CIMAGES = 6;
	public final static int HTTP_GET_LATEST_COMMENT = 7;
	public final static int HTTP_GPS = 8;
	public final static int HTTP_DBATCH_UPDATE_ESTIMATED = 9;
	public final static int HTTP_DBATCH_UPDATE = 10;
	public final static int HTTP_DBATCH_UPDATE_AVEGARE_INTERVAL_TIME = 11;
	public final static int HTTP_REGISTER_DBATCH_STATUS = 12;
	public final static int HTTP_UPDATE_CUSTOMER_API = 13;
	public final static int HTTP_UPDATE_COMMENT_API = 14;
	public final static int HTTP_UPLOAD_IMAGE = 15;
	public final static int HTTP_DBATCH_UPDATE_DISPLAY = 16;
	public final static int HTTP_DBATCH_UPDATE_SERVER = 17;
	public static final int HTTP_UPLOAD_COMMENTS = 18;
	public static String JSON = "";
	
	public static final String GPS= "/api/gps";
	public static String SERVER_BASE_URI = "http://153.150.9.50";//http://192.168.9.142  http://153.150.9.50
	public static final String UPDATE_COMMENT_API = SERVER_BASE_URI + "/api/customer_images/update";
	public static final String UPDATE_DELIVERY_BATCHS_API = SERVER_BASE_URI + "/api/delivery_batchs/update";
	public static final String AVERAGE_INTERVAL_TIME_API = SERVER_BASE_URI + "/api/drivers/update";
	public static final String UPDATE_CUSTOMER_API = SERVER_BASE_URI + "/api/customers/update";
	public static final String UPDATE_REGISTER_BATCHS_STATUS = SERVER_BASE_URI + "/api/delivery_batchs/register_delivery_batch_status";
	public final static String BASE_LOGIN_URL = SERVER_BASE_URI + "/api/auth";
	public static final String BASE_LOGOUT_URL = SERVER_BASE_URI + "/api/auth/logout";
	public static final String BASE_DBATCHS_TRANSFER_URL = SERVER_BASE_URI + "/api/delivery_batchs/transfer";
	public static final String BASE_ANDROID_STATUS_URL = SERVER_BASE_URI + "/api/drivers/get_android_status";
	public static final String BASE_UPLOAD_IMAGES_URL = SERVER_BASE_URI + "/api/customer_images/upload";
	public static final String BASE_UPLOAD_COMMENT_URL = SERVER_BASE_URI + "/api/customer_images/update_comment";
	public static final String BASE_LATEST_UPDATE_TIME_URL = SERVER_BASE_URI + "/api/customer_images/get_latest_update_time";
	public static final String BASE_CIMAGES_URL = SERVER_BASE_URI + "/api/customer_images";
	public static final String BASE_GPS_ADD_URL = SERVER_BASE_URI + "/api/gps/add";
	public static final String CUSTOMER_LATEST_API = "/api/customers";
	public static final String CUSTOMER_IMAGES_API = "/api/customer_images";
	public static final String DRIVER_API ="/api/drivers";
	public static final String CARRY_OUT_STATUS = "/api/carry_out_status";
	public static final String STATUS_API= "/api/status";
	public static final String CUSTOMER_PIC_API= "/api/customer_images/get_latest_update_time";
	public static final String LATEST_UPDATE_TIME_API = "/api/delivery_batchs/get_latest_update_time";
	public static final String DELIVERY_BACTH_API= "/api/delivery_batchs";
	public static final String IMAGE_UPLOAD_API= SERVER_BASE_URI +"/api/customer_images/upload";
	public static final String IMAGE_UPDATE_API= SERVER_BASE_URI +"/api/customer_images/update";
	public static final String NULL = "null";
	public static final int TIME_OUT = 30000;
	public static final int MESSAGE_DELAY = 1;
	
	public final static int OK = 200;
	public final static int FAIL = 204;
	public final static int REQUEST_INVALID = 400;
	public final static int PASSWORD_INVALID = 401;
	public final static int URL_INVALID = 404;
	public final static int SERVER_ERROR = 500;	
	public static final int DEFAULT_VALUE = 8;
	public static final int DEFAULT_MINUTE = 60;
	
	public final static String STATUS = "status";
	public final static String DATA = "data";
	public final static String ACCESS_TOKEN = "access_token";
	public final static String IS_OPEN = "IS_OPEN";
	public final static String HAISO_SET_CD = "HAISO_SET_CD";
	
	public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public final static String FORMAT_TIME = "HH:mm";
	public final static String FORMAT_DATE = "yyyy-MM-dd";
	
	public final static String BROADCAST_ACTION = "jp.co.isb.tms.sync.finish";
	public static final int RED = 0;
	public static final int BLUE = 1;
	
	
	

	
}
