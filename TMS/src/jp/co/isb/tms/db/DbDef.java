package jp.co.isb.tms.db;

public class DbDef {
	//Database info
	public static final String DATABASE_NAME = "TMS.db";
	public static final String DATABASE_PATH = "";
	public static final int DATABASE_VERSION = 1;
	
	public class SaveAnimalTable{
		/**
		 * Table TABLE_DELIVERY_BATCH_DATA
		 */
		public static final String TABLE_DELIVERY_BATCH_DATA = "DeliveryBatchData";
		public static final String SOSHIKI_CD = "OrganizationMaster";
		public static final String HAISO_BATCH_CD = "DeliveryBatchCode";
		public static final String KUKURI_BATCH_CD = "BundleBatchCode";
		public static final String HAISO_DATE = "DeliveryDate";
		public static final String SHOKAI_HAISO_DATE = "FirstDeliveryDate";
		public static final String HAISO_SET_CD = "DeliverySetCode";
		public static final String ORDER_NUM = "DisplayingOrder";
		public static final String NINUSHI_CD = "ShipperCode";
		public static final String NINUSHI_KUGIRI_CD = "ShipperTypeOrder";
		public static final String CHUMON_NO = "OrderID";
		public static final String TOI_NO = "TrackingID";
		public static final String KOKYAKU_CD = "CustomerCD";
		public static final String KOKYAKU_NM = "CustomerName";
		public static final String KOKYAKU_BUSHO_NM = "CustomerDepartmentName";
		public static final String KOKYAKU_TANTO_NM = "PersonInChargeAtCustomerSide";
		public static final String ZIP_CD = "PostalCode";
		public static final String ADDR_1 = "CustomerAddress1";
		public static final String ADDR_2 = "CustomerAddress2";
		public static final String ADDR_3 = "CustomerAddress3";
		public static final String ADDR_4 = "CustomerAddress4";
		public static final String ADDR_5 = "CustomerAddress5";
		public static final String TEL_1 = "CustomerTEL1";
		public static final String TEL_2 = "CustomerTEL2";
		public static final String TEL_3 = "CustomerTEL3";
		public static final String TEL_4 = "CustomerTEL4";
		public static final String TEL_5 = "CustomerTEL5";
		public static final String ROUTE_CD = "RouteCode";
		public static final String KIBO_JIKAN_FROM = "FROMExpectedTimeRangeFROM";
		public static final String KIBO_JIKAN_TO = "TOExpectedTimeRangeTO";
		public static final String KOGUCHI_NUM = "PackageQuantity";
		public static final String SHUKKA_DATE = "StockOutDate";
		public static final String KONPO_YORYO_S = "SquareMeterOfPackage";
		public static final String KONPO_YORYO_M = "CubicMeterOfPackage";
		public static final String KONPO_JURYO = "PackageWeight";
		public static final String DAIBIKI_FLG = "CashOnDeliveryFlag";
		public static final String ZEINUKI_KIN = "SubTotalBeforeTax";
		public static final String SHOHIZEI_KIN = "ConsumptionTax";
		public static final String SEIKYU_KIN = "BillingAmount";
		public static final String DENPYO_NO = "StockOutSlipID";
		public static final String DAIBIKI_TESURYO_KIN = "CODCommission";
		public static final String DRIVER_CD = "DriverCode";
		public static final String DRIVER_KUGIRI_CD = "DriverTypeCode";
		public static final String HAISO_YOTEI_TIME = "DeliveryEstimatedTime";
		public static final String HAISO_SHURYO_TIME = "DeliveryCompletedTime";
		public static final String HAISO_KAISU = "DeliveryQuantity";
		public static final String STATUS_CD = "StatusCode";
		public static final String MTD_FLG = "CarryOutCheckFlag";
		public static final String BIKO = "Notes";
		public static final String YOBI_1 = "Preparation1";
		public static final String YOBI_2 = "Preparation2";
		public static final String YOBI_3 = "Preparation3";
		public static final String YOBI_4 = "Preparation4";
		public static final String YOBI_5 = "Preparation5";
		public static final String YOBI_6 = "Preparation6";
		public static final String YOBI_7 = "Preparation7";
		public static final String YOBI_8 = "Preparation8";
		public static final String YOBI_9 = "Preparation9";
		public static final String YOBI_10 = "Preparation10";
		public static final String ALERT_FLG_1 = "AlertFlag1";
		public static final String ALERT_FLG_2 = "AlertFlag2";
		public static final String ALERT_FLG_3 = "AlertFlag3";
		public static final String ALERT_FLG_4 = "AlertFlag4";
		public static final String ALERT_FLG_5 = "AlertFlag5";
		public static final String ALERT_FLG_6 = "AlertFlag6";
		public static final String ALERT_FLG_7 = "AlertFlag7";
		public static final String ALERT_FLG_8 = "AlertFlag8";
		public static final String ALERT_FLG_9 = "AlertFlag9";
		public static final String ALERT_FLG_10 = "AlertFlag10";
		public static final String OKURIJYO_FLG = "InvoiceFlag";
		
		public static final String TABLE_CUSTOMER_MASTER = "CustomerMaster";
		public static final String SHUBETU = "Type";
		public static final String BUSHO = "CustomerDepartment";
		public static final String TANTOSHA = "PersonInHargeAtCustomerSide";
		public static final String TEL_KEY_1 = "TEL_Key1";
		public static final String TEL_KEY_2 = "TEL_Key2";
		public static final String TEL_KEY_3 = "TEL_Key3";
		public static final String TEL_KEY_4 = "TEL_Key4";
		public static final String TEL_KEY_5 = "TEL_Key5";
		public static final String NOKISAKI = "ConditionInFrontOfHouse";
		public static final String MESSAGE = "Message";
		
		public static final String TABLE_CUSTOMER_IMAGE_MASTER = "CustomerImageMaster";
		public static final String IMAGE_CD = "ScreenID";
		public static final String IMAGE_DATA = "ImageData";
		public static final String DSP_SORT = "DisplayingOrder";
		public static final String COMMENT = "Comment";
		
		public static final String TABLE_DRIVER_MASTER = "DriverMaster";
		public static final String SHARYO_CD = "VehicleCode";
		public static final String GYOSHA_CD = "DeliveryCompanyCode";
		public static final String DRIVER_SEI = "DriverLastName";
		public static final String DRIVER_MEI = "DriverFirstName";
		public static final String DRIVER_SEI_KANA = "DriverLastNameKatakana";
		public static final String DRIVER_MEI_KANA = "DriverFirstNameKatakana";
		public static final String PASSWORD = "Password";
		public static final String BIRTHDAY = "DateOfBirth";
		public static final String POSITION = "Position";
		public static final String GENDER = "Gender";
		public static final String BLOOD_TYPE = "BloodType";
		public static final String TEL = "TEL";
		public static final String MOBILE_TEL = "Cellphone";
		public static final String FAX = "FaxNumber";
		public static final String SAIDAI_HAISO = "DeliverableQuantity";
		public static final String SHUTSUGEN_KYORI = "ArrivalDistance";
		public static final String HAISO_KANKAKU = "DeliveryAverageInterval";
		public static final String WARIFURI_FLG = "AutoAssignFlag";
		public static final String DEL_FLG = "DeleteFlag";
		public static final String CREATED = "RegisteredDataTime";
		public static final String CREATED_STAFF = "RegisteredStaffCode";
		public static final String MODIFIED = "UpdatedDateTime";
		public static final String MODIFIED_STAFF = "UpdatedStaffCode";
		
		public static final String TABLE_STATUS_MASTER = "StatusMaster";
		public static final String STATUS_NM = "StatusName";
		public static final String SAIHAISO_FLG = "ReDeliverableFlag";
		public static final String MODORI_FLG = "RedeliveryReturnableFlag";
		public static final String SAIHAISO_STATUS_CD = "RedeliveryStatusCode";
		public static final String MODORI_STATUS_CD = "ReturnStatusCode";
		public static final String ETC_FLG = "OthersFlag";
		
		/**
		 * Table contains data for TABLE_DELIVERY_BATCH_DATA
		 */
		public static final String SCRIPT_CREATE_TABLE_DELIVERY_BATCH_DATA = "create table " + TABLE_DELIVERY_BATCH_DATA 
		 + " ( " 
				 + SOSHIKI_CD + " text, "
				 + HAISO_BATCH_CD + " text, " 
				 + KUKURI_BATCH_CD + " text, "
				 + HAISO_DATE + " numeric, "
				 + SHOKAI_HAISO_DATE + " numeric, "
				 + HAISO_SET_CD + " text, "
				 + ORDER_NUM + " integer, "
				 + NINUSHI_CD + " text "
				  + NINUSHI_KUGIRI_CD + " text, "
				 + CHUMON_NO + " text, "
				 + TOI_NO + " text, "
				 + KOKYAKU_CD + " text, "
				 + KOKYAKU_NM + " text, "
				 + KOKYAKU_BUSHO_NM + " text, "
				 + KOKYAKU_TANTO_NM + " text, "
				 + ZIP_CD + " text, "
				 + ADDR_1 + " text, "
				 + ADDR_2 + " text, "
				 + ADDR_3 + " text, "
				 + ADDR_4 + " text, "
				 + TEL_1 + " text, "
				 + TEL_2 + " text, "
				 + TEL_3 + " text, "
				 + TEL_4 + " text, "
				 + TEL_5 + " text, "
				 + ROUTE_CD + " text, "
				 + KIBO_JIKAN_FROM + " numeric, "
				 + KIBO_JIKAN_TO + " numeric, "
				 + KOGUCHI_NUM + " integer, "
				 + SHUKKA_DATE + " numeric, "
				 + KONPO_YORYO_S + " numeric, "
				 + KONPO_YORYO_M + " numeric, "
				 + KONPO_JURYO + " numeric, "
				 + DAIBIKI_FLG + " integer, "
				 + ZEINUKI_KIN + " numeric, "
				 + SHOHIZEI_KIN + " numeric, "
				 + SEIKYU_KIN + " numeric, "
				 + DENPYO_NO + " text, "
				 + DAIBIKI_TESURYO_KIN + " numeric, "
				 + DRIVER_CD + " text, "
				 + DRIVER_KUGIRI_CD + " text, "
				 + HAISO_YOTEI_TIME + " numeric, "
				 + HAISO_SHURYO_TIME + " numeric, "
				 + HAISO_KAISU + " integer, "
				 + STATUS_CD + " text, "
				 + MTD_FLG + " integer, "
				 + BIKO + " text, "
				 + YOBI_1 + " text, "
				 + YOBI_2 + " text, "
				 + YOBI_3 + " text, "
				 + YOBI_4 + " text, "
				 + YOBI_5 + " text, "
				 + YOBI_6 + " text, "
				 + YOBI_7 + " text, "
				 + YOBI_8 + " text, "
				 + YOBI_9 + " text, "
				 + YOBI_10 + " text, "
				 + ALERT_FLG_1 + " integer, "
				 + ALERT_FLG_2 + " integer, "
				 + ALERT_FLG_3 + " integer, "
				 + ALERT_FLG_4 + " integer, "
				 + ALERT_FLG_5 + " integer, "
				 + ALERT_FLG_6 + " integer, "
				 + ALERT_FLG_7 + " integer, "
				 + ALERT_FLG_8 + " integer, "
				 + ALERT_FLG_9 + " integer, "
				 + ALERT_FLG_10 + " integer, "
				 + OKURIJYO_FLG + " integer, "
				 + DEL_FLG + " integer, "
				 + CREATED + " numeric, "
				 + CREATED_STAFF + " text, "
				 + MODIFIED + "numeric, "
				 + MODIFIED_STAFF + " text," + "primary key" + "(" + SOSHIKI_CD + "," +  HAISO_BATCH_CD + ")"
		 + ")";
		
		/**
		 * Table contains data for TABLE_CUSTOMER_MASTER 
		 */
		public static final String SCRIPT_CREATE_TABLE_CUSTOMER_MASTER = "create table " + TABLE_CUSTOMER_MASTER 
				 + " ( " 
						 + SOSHIKI_CD + " text , "
						 + KOKYAKU_CD + " text , " 
						 + SHUBETU + " integer , " 
						 + KOKYAKU_NM + " text, "
						 + BUSHO + " text, "
						 + TANTOSHA + " text, "
						 + ZIP_CD + " text, "
						 + ADDR_1 + " text "
						 + ADDR_2 + " text, "
						 + ADDR_3 + " text, "
						 + TEL_1 + " text, "
						 + TEL_2 + " text, "
						 + TEL_3 + " text, "
						 + TEL_4 + " text, "
						 + TEL_5 + " text, "
						 + TEL_KEY_1 + " text, "
						 + TEL_KEY_2 + " text, "
						 + TEL_KEY_3 + " text, "
						 + TEL_KEY_4 + " text, "
						 + TEL_KEY_5 + " text, "
						 + MOBILE_TEL + " text, "
						 + FAX + " text, "
						 + NOKISAKI + " text, "
						 + MESSAGE + " text, "
						 + DEL_FLG + " integer, "
						 + CREATED + " numeric, "
						 + CREATED_STAFF + " text, "
						 + MODIFIED + " numeric, "
						 + MODIFIED_STAFF + " text," + "primary key" + "(" + SOSHIKI_CD + "," +  KOKYAKU_CD +  ")"
						             
				 + " ) ";
		/**
		 * Table contains data for TABLE_CUSTOMER_IMAGE_MASTER 
		 */
		public static final String SCRIPT_CREATE_TABLE_CUSTOMER_IMAGE_MASTER = "create table " + TABLE_CUSTOMER_IMAGE_MASTER 
				 + " ( " 
						 + SOSHIKI_CD + " text , "
						 + KOKYAKU_CD + " text , "
						 + IMAGE_CD + " text , " 
						 + IMAGE_DATA + " none, "
						 + DSP_SORT + " integer, "
						 + COMMENT + " text, "
						 + CREATED + " numeric, "
						 + CREATED_STAFF + " text "
						 + MODIFIED + " numeric, "
						 + MODIFIED_STAFF + " text, " + "primary key" + "(" + SOSHIKI_CD + "," +  KOKYAKU_CD + "," + IMAGE_CD + ")" 
				 + " )";
		
		/**
		 * Table contains data for TABLE_DRIVER_MASTER 
		 */
		public static final String SCRIPT_CREATE_TABLE_DRIVER_MASTER = "create table " + TABLE_DRIVER_MASTER 
				 + " ( " 
						 + SOSHIKI_CD + " text , "
						 + DRIVER_CD + " text , "
						 + SHARYO_CD + " text, "
						 + GYOSHA_CD + " text, "
						 + DRIVER_SEI + " text, "
						 + DRIVER_MEI + " text, "
						 + DRIVER_SEI_KANA + " text, "
						 + DRIVER_MEI_KANA + " text, "
						 + PASSWORD + " text, "
						 + BIRTHDAY + " numeric, "
						 + POSITION + " text, "
						 + GENDER + " text, "
						 + BLOOD_TYPE + " text, "
						 + ZIP_CD + " text, "
						 + ADDR_1 + " text, "
						 + ADDR_2 + " text, "
						 + ADDR_3 + " text, "
						 + TEL + " text, "
						 + MOBILE_TEL + " text, "
						 + FAX + " text, "
						 + SAIDAI_HAISO + " integer, "
						 + SHUTSUGEN_KYORI + " integer, "
						 + HAISO_KANKAKU + " integer, "
						 + WARIFURI_FLG + " integer, "
						 + DEL_FLG + " integer, "
						 + CREATED + " numeric, "
						 + CREATED_STAFF + " text, "
						 + MODIFIED + " numeric, "
						 + MODIFIED_STAFF + " text," + "primary key" + "(" + SOSHIKI_CD + "," +  DRIVER_CD +  ")" 
				 + " ) ";
		/**
		 * Table contains data for STATUS_MASTER 
		 */
		public static final String SCRIPT_CREATE_STATUS_MASTER = "create table " + TABLE_STATUS_MASTER 
				 + " ( " 
						 + SOSHIKI_CD + " text , "
						 + STATUS_CD + " text  , " 
						 + STATUS_NM + " text, "
						 + SAIHAISO_FLG + " integer, "
						 + MODORI_FLG + " integer, "
						 + SAIHAISO_STATUS_CD + " text, "
						 + MODORI_STATUS_CD + " text, "
						 + ETC_FLG + " integer "
						 + CREATED + " numeric, "
						 + CREATED_STAFF + " text, "
						 + MODIFIED + " numeric, "
						 + MODIFIED_STAFF + " text, " + "primary key" + "(" + SOSHIKI_CD + "," +  STATUS_CD +  ")" 
				 + " ) ";
	}
}
