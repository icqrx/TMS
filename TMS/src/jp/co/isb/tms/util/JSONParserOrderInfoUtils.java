package jp.co.isb.tms.util;

import java.util.ArrayList;

import jp.co.isb.tms.model.OrderInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParserOrderInfoUtils {

	// JSON Node names
	private static final String TAG_DATA = "data";
	public static final String  TAG_HAISO_BATCH_CD = "HAISO_BATCH_CD";
	public static final String  TAG_HAITATSU_DATE = "HAITATSU_DATE";
	public static final String  TAG_KOKYAKU_NM = "KOKYAKU_NM";
	public static final String  TAG_BUSHO_NM = "BUSHO_NM";
	public static final String  TAG_ADDR_1 = "ADDR_1";
	public static final String  TAG_ADDR_2 = "ADDR_2";
	public static final String  TAG_ADDR_3 = "ADDR_3";
	public static final String  TAG_ADDR_4 = "ADDR_4";
	public static final String  TAG_KOGUCHI_NUM = "KOGUCHI_NUM";
	public static final String  TAG_MESSAGE = "MESSAGE";
	public static final String TAG_SOSHIKI_CD = "SOSHIKI_CD";
	public static final String TAG_HIKIATE_PRIORITY_ORDER = "HIKIATE_PRIORITY_ORDER";
	public static final String TAG_HIKIATE_KBN_NM = "HIKIATE_KBN_NM";
	public static final String TAG_PRESET_CD = "PRESET_CD";
	public static final String TAG_HAISO_SET_CD = "HAISO_SET_CD";
	public static final String TAG_ORDER_NUM = "ORDER_NUM";
	public static final String TAG_NINUSHI_CD = "NINUSHI_CD";
	public static final String TAG_KAISHA_NM_1 = "KAISHA_NM_1";
	public static final String TAG_NINUSHI_KUGIRI_CD = "NINUSHI_KUGIRI_CD";
	public static final String TAG_KUGIRI_NM = "KUGIRI_NM";
	public static final String TAG_CHUMON_NO = "CHUMON_NO";
	public static final String TAG_TOI_NO = "TOI_NO";
	public static final String TAG_KOKYAKU_CD = "KOKYAKU_CD";
	public static final String TAG_TANTO_NM = "TANTO_NM";
	public static final String TAG_JUSHO_CD = "JUSHO_CD";
	public static final String TAG_ZIP_CD = "ZIP_CD";
	public static final String TAG_TEL_1 = "TEL_1";
	public static final String TAG_TEL_2 = "TEL_2";
	public static final String TAG_TEL_3 = "TEL_3";
	public static final String TAG_TEL_4 = "TEL_4";
	public static final String TAG_TEL_5 = "TEL_5";
	public static final String TAG_KIBO_JIKAN_FROM = "KIBO_JIKAN_FROM";
	public static final String TAG_KIBO_JIKAN_TO = "KIBO_JIKAN_TO";
	public static final String TAG_SHUKKA_DATE = "SHUKKA_DATE";
	public static final String TAG_KONPO_YORYO_S= "KONPO_YORYO_S";
	public static final String TAG_KONPO_YORYO_M = "KONPO_YORYO_M";
	public static final String TAG_KONPO_JURYO = "KONPO_JURYO";
	public static final String TAG_DAIBIKI_FLG = "DAIBIKI_FLG";
	public static final String TAG_ZEINUKI_KIN = "ZEINUKI_KIN";
	public static final String TAG_SHOHIZEI_KIN = "SHOHIZEI_KIN";
	public static final String TAG_SEIKYU_KIN= "SEIKYU_KIN";
	public static final String TAG_DENPYO_NO = "DENPYO_NO";
	public static final String TAG_DAIBIKI_TESURYO_KIN = "DAIBIKI_TESURYO_KIN";
	public static final String TAG_MODIFIED_DRIVER_CD = "MODIFIED_DRIVER_CD";
	public static final String TAG_HAISO_YOTEI_TIME = "HAISO_YOTEI_TIME";
	public static final String TAG_HAISO_SHURYO_TIME = "HAISO_SHURYO_TIME";
	public static final String TAG_HAISO_KAISU = "HAISO_KAISU";
	public static final String TAG_STATUS_CD = "STATUS_CD";
	public static final String TAG_MTD_FLG = "MTD_FLG";
	public static final String TAG_MTD_STATUS_CD = "MTD_STATUS_CD";
	public static final String TAG_MTD_STATUS_NM = "MTD_STATUS_NM";
	public static final String TAG_BIKO = "BIKO";
	public static final String TAG_MODIFIED_DRIVER = "MODIFIED_DRIVER";
	
	public static final String TAG_KUKURI_BATCH_CD = "KUKURI_BATCH_CD";
	public static final String TAG_HAISO_DATE = "HAISO_DATE";
	public static final String TAG_SHOKAI_HAISO_DATE = "SHOKAI_HAISO_DATE";
	public static final String TAG_SHOKAI_HAITATSU_DATE = "SHOKAI_HAITATSU_DATE";
	public static final String TAG_ROUTE_CD = "ROUTE_CD";
	public static final String TAG_DRIVER_CD = "DRIVER_CD";
	public static final String TAG_DRIVER_KUGIRI_CD = "DRIVER_KUGIRI_CD";
	public static final String TAG_YOBI_1 = "YOBI_1";
	public static final String TAG_YOBI_2 = "YOBI_2";
	public static final String TAG_YOBI_3 = "YOBI_3";
	public static final String TAG_YOBI_4 = "YOBI_4";
	public static final String TAG_YOBI_5 = "YOBI_5";
	public static final String TAG_YOBI_6 = "YOBI_6";
	public static final String TAG_YOBI_7 = "YOBI_7";
	public static final String TAG_YOBI_8 = "YOBI_8";
	public static final String TAG_YOBI_9 = "YOBI_9";
	public static final String TAG_YOBI_10 = "YOBI_10";
	public static final String TAG_ALERT_FLG_1 = "ALERT_FLG_1";
	public static final String TAG_ALERT_FLG_2 = "ALERT_FLG_2";
	public static final String TAG_ALERT_FLG_3 = "ALERT_FLG_3";
	public static final String TAG_ALERT_FLG_4 = "ALERT_FLG_4";
	public static final String TAG_ALERT_FLG_5 = "ALERT_FLG_5";
	public static final String TAG_ALERT_FLG_6 = "ALERT_FLG_6";
	public static final String TAG_ALERT_FLG_7 = "ALERT_FLG_7";
	public static final String TAG_ALERT_FLG_8= "ALERT_FLG_8";
	public static final String TAG_ALERT_FLG_9 = "ALERT_FLG_9";
	public static final String TAG_ALERT_FLG_10 = "ALERT_FLG_10";
	public static final String TAG_OKURIJYO_FLG = "OKURIJYO_FLG";
	private static final String TAG = JSONParserOrderInfoUtils.class.getSimpleName();
	
	public JSONParserOrderInfoUtils() {

	}
	
	public ArrayList<OrderInfo> parseDeliveryOrder(String data ) {
		ArrayList<OrderInfo> mDeliveryOder = new ArrayList<OrderInfo>();

		try {
			
			JSONObject json = new JSONObject(data);

			// Getting Array of OrderInfo
			JSONArray orders = json.getJSONArray(TAG_DATA);

			for (int i = 0; i < orders.length(); i++) {
				JSONObject ob = orders.getJSONObject(i);

				// Storing each json item in variable
				String deliveryBatchCode = ob.getString(TAG_HAISO_BATCH_CD);
				String bundleBatchCode = ob.getString(TAG_KUKURI_BATCH_CD);
				String trackingID = ob.getString(TAG_TOI_NO);
				String orderID = ob.getString(TAG_CHUMON_NO);
				String shipperCode = ob.getString(TAG_NINUSHI_CD);
				String customerDepartmentName = ob.getString(TAG_BUSHO_NM);
				
				String firstDeliveryDate = ob.getString(TAG_SHOKAI_HAITATSU_DATE);
				String estimatedDeliveryTime = ob.getString(TAG_HAISO_YOTEI_TIME);
				String receiverAddress1 = ob.getString(TAG_ADDR_1);
				String receiverAddress2 = ob.getString(TAG_ADDR_2);
				String receiverAddress3 = ob.getString(TAG_ADDR_3);
				String receiverAddress4 = ob.getString(TAG_ADDR_4);
				String customerName = ob.getString(TAG_KOKYAKU_NM);
				String personInChargeOfReceiverSide = ob.getString(TAG_TANTO_NM);
				String packageQuantity = ob.getString(TAG_KOGUCHI_NUM);
				String message = ob.getString(TAG_MESSAGE);
				String tel1 = ob.getString(TAG_TEL_1);
				String tel2 = ob.getString(TAG_TEL_2);
				String tel3 = ob.getString(TAG_TEL_3);
				String tel4 = ob.getString(TAG_TEL_4);
				String tel5 = ob.getString(TAG_TEL_5);
				String notes = ob.getString(TAG_BIKO);
				//
				String deliveryDate = ob.getString(TAG_HAITATSU_DATE);
				String the1stDeliveryDate = ob.getString(TAG_SHOKAI_HAITATSU_DATE);
				String allocationTypeName = ob.getString(TAG_HIKIATE_KBN_NM);
				Integer allocationPriorityOrder = ob.getInt(TAG_HIKIATE_PRIORITY_ORDER);
				String presetCode = ob.getString(TAG_PRESET_CD);
				String deliverySetCode = ob.getString(TAG_HAISO_SET_CD);
				Integer displayingOrder = ob.getInt(TAG_ORDER_NUM);
				String shipperName = ob.getString(TAG_KAISHA_NM_1);
				String shipperTypeCode = ob.getString(TAG_NINUSHI_KUGIRI_CD);
				String shipperTypeName = ob.getString(TAG_KUGIRI_NM);
				String customerCD = ob.getString(TAG_KOKYAKU_CD);
				String personInChargeAtCustomerSide = ob.getString(TAG_TANTO_NM);
				String addressCode = ob.getString(TAG_JUSHO_CD);
				String postalCode = ob.getString(TAG_ZIP_CD);
				String customerAddress1 = ob.getString(TAG_ADDR_1);
				String customerAddress2 = ob.getString(TAG_ADDR_2);
				String customerAddress3 = ob.getString(TAG_ADDR_3);
				String customerAddress4 = ob.getString(TAG_ADDR_4);
				String customerTEL1 = ob.getString(TAG_TEL_1);
				String customerTEL2 = ob.getString(TAG_TEL_2);
				String customerTEL3 = ob.getString(TAG_TEL_3);
				String customerTEL4 = ob.getString(TAG_TEL_4);
				String customerTEL5 = ob.getString(TAG_TEL_5);
				String expectedTimeRangeFROM = ob.getString(TAG_KIBO_JIKAN_FROM);
				String expectedTimeRangeTO = ob.getString(TAG_KIBO_JIKAN_TO);
				String stockOutDate = ob.getString(TAG_SHUKKA_DATE);
				String squareMeterOfPackage = ob.getString(TAG_KONPO_YORYO_S);
				String cubicMeterOfPackage = ob.getString(TAG_KONPO_YORYO_M);
				String packageWeight = ob.getString(TAG_KONPO_JURYO);
				String cashOnDelivery = ob.getString(TAG_DAIBIKI_FLG);
				String subTotalBeforeTax = ob.getString(TAG_ZEINUKI_KIN);
				String comsumptionTax = ob.getString(TAG_SHOHIZEI_KIN);
				String billingAmount = ob.getString(TAG_SEIKYU_KIN);
				String stockOutPayslipNumber = ob.getString(TAG_DENPYO_NO);
				String cODCommission = ob.getString(TAG_DAIBIKI_TESURYO_KIN);
				String driverCode = ob.getString(TAG_MODIFIED_DRIVER_CD);
				String deliveryCompletedDateTime = ob.getString(TAG_HAISO_SHURYO_TIME);
				String deliveredTimes = ob.getString(TAG_HAISO_KAISU);
				String statusCode = ob.getString(TAG_STATUS_CD);
				String carryOutCheckFlag = ob.getString(TAG_MTD_FLG);
				String carryOutStatusCd = ob.getString(TAG_MTD_STATUS_CD);
				String carryOutStatusName = ob.getString(TAG_MTD_STATUS_NM);
				String driverUpdatedDateTime = ob.getString(TAG_MODIFIED_DRIVER);
				
				OrderInfo cur = new OrderInfo();
				
				cur.setDisplayingOrder(displayingOrder);
				cur.setAddressCode(addressCode); 
				cur.setCODCommission(cODCommission);
				cur.setExpectedTimeRangeFROM(expectedTimeRangeFROM);
				cur.setExpectedTimeRangeTO(expectedTimeRangeTO);
				cur.setStockOutDate(stockOutDate);
				cur.setSquareMeterOfPackage(squareMeterOfPackage);
				cur.setCubicMeterOfPackage(cubicMeterOfPackage);
				cur.setPackageWeight(packageWeight);
				cur.setCashOnDeliveryFlag(cashOnDelivery);
				cur.setSubTotalBeforeTax(subTotalBeforeTax);
				cur.setConsumptionTax(comsumptionTax);
				cur.setBillingAmount(billingAmount);
				cur.setStockOutPaySlipNumber(stockOutPayslipNumber);
				cur.setDriverCode(driverCode);
				cur.setDeliveryCompletedDateTime(deliveryCompletedDateTime);
				cur.setDeliveredTimes(deliveredTimes);
				cur.setCarryOutCheckFlag(carryOutCheckFlag);
				cur.setCarryOutStatusCd(carryOutStatusCd);
				cur.mCarryOutStatusName = carryOutStatusName;
				
				cur.setDriverUpdatedDateTime(driverUpdatedDateTime);
				cur.setPostalCode(postalCode);
				cur.setCustomerTEL1(customerTEL1);
				cur.setCustomerTEL2(customerTEL2);
				cur.setCustomerTEL3(customerTEL3);
				cur.setCustomerTEL4(customerTEL4);
				cur.setCustomerTEL5(customerTEL5);
				cur.setCustomerAddress1(customerAddress1);
				cur.setCustomerAddress2(customerAddress2);
				cur.setCustomerAddress3(customerAddress3);
				cur.setCustomerAddress4(customerAddress4);
				cur.setCustomerAddress(customerAddress1 + customerAddress2 + customerAddress3 + customerAddress4);
				
				cur.setShipperName(shipperName);
				cur.setPersonInChargeOfReceiverSide(personInChargeOfReceiverSide);
				cur.setShipperCode(shipperCode);
				cur.setCustomerCD(customerCD);
				cur.setPersonInChargeAtCustomerSide(personInChargeAtCustomerSide);
				cur.setCustomerName(customerName);
				cur.setShipperTypeCode(shipperTypeCode);
				cur.setShipperTypeName(shipperTypeName);
				cur.setBundleBatchCode(bundleBatchCode);
				cur.setAllocationTypeName(allocationTypeName);
				cur.setPresetCode(presetCode);
				cur.setDeliverySetCode(deliverySetCode);
				cur.setAllocationPriorityOrder(allocationPriorityOrder);
				cur.setDeliveryDate(deliveryDate);
				cur.setFirstDeliveryDate(the1stDeliveryDate);
				cur.setDeliveryBatchCode(deliveryBatchCode);
				cur.setDepartmentName(customerDepartmentName);
				cur.setPackageQuantity(packageQuantity);
				cur.setmMessage(message);
				cur.setReceiverAddress(receiverAddress1 + receiverAddress2 + receiverAddress3 + receiverAddress4); 
				cur.setAddress1(receiverAddress1);
				cur.setAddress2(receiverAddress2);
				cur.setAddress3(receiverAddress3);
				cur.setAddress4(receiverAddress4);
				cur.setTrackingId(trackingID);
				cur.setOrderId(orderID);
				cur.setShipperCode(shipperCode);
				cur.setFirstDeliveryDate(firstDeliveryDate);
				cur.setEstimatedDeliveryTime(estimatedDeliveryTime);
				cur.setReceiverName(customerName);
				cur.setPersonInChargeOfReceiverSide(personInChargeOfReceiverSide);
				cur.setTel1(tel1);
				cur.setTel2(tel2);
				cur.setTel3(tel3);
				cur.setTel4(tel4);
				cur.setTel5(tel5);
				cur.setNote(notes);
				cur.mStatusId = statusCode;
				
				mDeliveryOder.add(cur);
			}
		} catch (JSONException e) {
			Log.e(TAG, "" +  e.toString());
		}

		return mDeliveryOder;
		
	}
}
