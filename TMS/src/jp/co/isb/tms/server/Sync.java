package jp.co.isb.tms.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.co.isb.tms.ESync;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.StoragePref;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.model.CarryOutStatusMaster;
import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.CustomerPicInfo;
import jp.co.isb.tms.model.DriverInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.model.OrderStatusInfo;
import jp.co.isb.tms.util.DateTimeUtils;
import jp.co.isb.tms.util.NetworkUtils;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class Sync {

	private static final String TAG = Sync.class.getSimpleName();
	private Context mContext;
	private Receive mReceive;
	private Send mSend;
	private ESync result;
	
	public Sync(Context context) {
		this.mContext = context;

		mReceive = new Receive(mContext);
		mSend = new Send(mContext);
	}

	public void executeSync() {
		TMSConstrans.iSync = true;
		TMSConstrans.isTimeout = false;
		
		Log.d(TAG, "start executeSync");
		String systemTime = mReceive.getSystemTime();
		Log.d(TAG, "system time: " + systemTime);
		
		if (!systemTime.trim().isEmpty()) {
			if(!DateTimeUtils.equal(TMSConstrans.mLastSyncDateTime, systemTime)){
				DatabaseManager.getInstance().clearAllTable();
			}
			
			TMSConstrans.mLastSyncDateTime = systemTime;
			
			StoragePref.saveLastSyncDateTime(mContext);
		}
		
		syncDeliveryBatchData();
		syncDriverMaster();
		syncStatusMaster();
		syncCarryOutStatusMaster();
		syncCustomerMaster();
		syncCustomerImageMaster();
		
		sendBroadcastFinishSync();
		
		TMSConstrans.iSync = false;
		
		Log.d(TAG, "end executeSync");
	}
	
	 public void sendBroadcastFinishSync(){
	        Intent broadcast = new Intent();
	        broadcast.setAction(Def.BROADCAST_ACTION);
	        mContext.sendBroadcast(broadcast);
	    }
	private OrderInfo getOrderLastestUpdateTime(ArrayList<OrderInfo> orderListServer, String deliveryBatchCode) {
		for (int i = 0; i < orderListServer.size(); i++) {
			if (orderListServer.get(i).getDeliveryBatchCode().equalsIgnoreCase(deliveryBatchCode)) {
				return orderListServer.get(i);
			}
		}
		return new OrderInfo();
	}
	
	public void syncDeliveryBatchData() {
		Log.d(TAG, "start syncDeliveryBatchData");

		ArrayList<OrderInfo> orderListServer = new ArrayList<OrderInfo>();
		orderListServer = mReceive.getDeliveryBatchData(TMSConstrans.mLastSyncDateTime.split(" ")[0]);
		ArrayList<OrderInfo> orderListLocal = DatabaseManager.getInstance().getAllDeliveryOrder();

		if (orderListLocal.size() == 0) {
			saveDeliveryData(orderListServer);
		} else {
			// Step 3: Update Processing
			// Execute for each following data

			// A. Get the latest updated time of server side and current status

			for (int i = 0; i < orderListLocal.size(); i++) {
				OrderInfo orderInfo = new OrderInfo();
//				orderInfo = mReceive.getOrderLastestUpdateTime(TMSConstrans.mSoshiki_cd,orderListLocal.get(i).getDeliveryBatchCode());
				orderInfo = getOrderLastestUpdateTime(orderListServer, orderListLocal.get(i).getDeliveryBatchCode());
				String updatedDateTimeServer = orderInfo.getUpdatedDateTime();
				String updatedDateTimeLocal = orderListLocal.get(i).getUpdatedDateTime();
				
				String driverUpdatedDateTimeServer = orderInfo.getDriverUpdatedDateTime();
				String driverUpdatedDateTimeLocal = orderListLocal.get(i).getDriverUpdatedDateTime();
				
				result = checkServerSync(driverUpdatedDateTimeLocal, driverUpdatedDateTimeServer);
				
				boolean statusChangeFlag  = false;
				
				for (int m = 0; m < orderListServer.size(); m++) {
								
					if (orderListLocal.get(i).getDeliveryBatchCode().equals(orderListServer.get(m).getDeliveryBatchCode())) {
						
						if (!orderListLocal.get(i).getStatusId().equals(orderListServer.get(m).getStatusId())) {
						
							if (!orderListLocal.get(i).getStatusId().equals("030") &&
								!orderListLocal.get(i).getStatusId().equals("000") &&
								!orderListLocal.get(i).getStatusId().equals("050")) {
								statusChangeFlag = true;
							}		
						}
						break;	
					}
				}
				
				if (statusChangeFlag) {
					Log.w(TAG, "sync server, tracking id: " + orderInfo.getTrackingId());
					// E: In case Status Information of A and Local Status
					// Information are different, update the actual result.
					mSend.requestRegisterDBatchStatus(TMSConstrans.mSoshiki_cd,
							orderListLocal.get(i).getDeliveryBatchCode(),
							orderListLocal.get(i).getBundleBatchCode(),
							
							orderListLocal.get(i).getCarryOutCheckFlag(),
							orderListLocal.get(i).getCarryOutStatusCd(),

							
							orderListLocal.get(i).getDeliveryDate(),
							orderListLocal.get(i).getUpdatedDateTime(),
							orderListLocal.get(i).getDriverUpdatedDateTime(),
							orderListLocal.get(i).getPresetCode(),
							orderListLocal.get(i).getPresetName(),
							orderListLocal.get(i).getDeliverySetCode(),
							orderListLocal.get(i).getDeliverySetName(),
							orderListLocal.get(i).getRouteCode(),
							orderListLocal.get(i).getRouteName(),
							orderListLocal.get(i).getShipperCode(),
							orderListLocal.get(i).getShipperTypeCode(),
							orderListLocal.get(i).getTrackingId(),
							orderListLocal.get(i).getCustomerCD(),
							orderListLocal.get(i).getCustomerPostalCode(),
							orderListLocal.get(i).getPackageQuantity(),
							orderListLocal.get(i).getStockOutDate(),
							orderListLocal.get(i).getSquareMeterOfPackage(),
							orderListLocal.get(i).getCubicMeterOfPackage(),
							orderListLocal.get(i).getPackageWeight(),
							orderListLocal.get(i).getSubTotalBeforeTax(),
							orderListLocal.get(i).getCashOnDeliveryFlag(),
							orderListLocal.get(i).getConsumptionTax(),
							orderListLocal.get(i).getBillingAmount(),
							orderListLocal.get(i).getStockOutPaySlipNumber(),
							orderListLocal.get(i).getCODCommission(),
							orderListLocal.get(i).mStatusId,
							orderListLocal.get(i).getEstimatedDeliveryTime(),
							orderListLocal.get(i).getDeliveryCompletedDateTime(),
							orderListLocal.get(i).getDeliveredTimes());
				}
				
				result = checkLocalSync(updatedDateTimeLocal, updatedDateTimeServer);
				if (result == ESync.ESYNC_LOCAL) {
					// get the new data from server and update for local
					if (!orderInfo.getDeliveryBatchCode().trim().isEmpty() 
							|| orderInfo.getDeliveryBatchCode().equalsIgnoreCase(Def.NULL)) {
						Log.w(TAG, "sync local, tracking id: " + orderInfo.getTrackingId());
						DatabaseManager.getInstance().createOrUpdateOrderItem(orderInfo);
					}
				}

			}
			
			//save new
			ArrayList<OrderInfo> tempList = new ArrayList<OrderInfo>();
					
			for (int i = 0; i < orderListServer.size(); i++) {
				
				boolean foundFlg = false;
				for (int j = 0; j < orderListLocal.size(); j++) {
					
					if (orderListLocal.get(j).getDeliveryBatchCode().equals(orderListServer.get(i).getDeliveryBatchCode())) {
						foundFlg = true;
						break;
					}
				}

				if (!foundFlg) {
					tempList.add(orderListServer.get(i));
				}
			}
			
			if (tempList.size() > 0) {
				saveDeliveryData(tempList);
			}
		}
	}
	
	private ESync checkLocalSync(String updatedDateTimeLocal, String updatedDateTimeServer) {
		SimpleDateFormat sdf = new SimpleDateFormat(Def.FORMAT_DATE_TIME);
		Date strDateServer = null;
		Date strDateLocal = null;
		
		if (updatedDateTimeLocal == null && updatedDateTimeServer == null) {
			return ESync.NONE;
		}
		
		try {
			result = ESync.NONE;
			if (updatedDateTimeServer != null 
					&& !updatedDateTimeServer.equalsIgnoreCase(Def.NULL)
					&& !updatedDateTimeServer.trim().isEmpty()) {
				
				if (updatedDateTimeLocal == null 
					|| updatedDateTimeLocal.equalsIgnoreCase(Def.NULL)
					|| updatedDateTimeLocal.trim().isEmpty() ) {
					result = ESync.ESYNC_LOCAL;
				} else {
					strDateServer = sdf.parse(updatedDateTimeServer);
					strDateLocal = sdf.parse(updatedDateTimeLocal);
		
					if (strDateServer.after(strDateLocal)) {
						result = ESync.ESYNC_LOCAL;
					}
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private ESync checkServerSync(String driverUpdatedDateTimeLocal, String driverUpdatedDateTimeServer) {
		SimpleDateFormat sdf = new SimpleDateFormat(Def.FORMAT_DATE_TIME);
		Date strDateServer = null;
		Date strDateLocal = null;
		
		try {
			result = ESync.NONE;
			if (driverUpdatedDateTimeLocal != null 
					&& !driverUpdatedDateTimeLocal.equalsIgnoreCase(Def.NULL)
					&& !driverUpdatedDateTimeLocal.trim().isEmpty()) {
				
				if (driverUpdatedDateTimeServer == null 
						|| driverUpdatedDateTimeServer.equalsIgnoreCase(Def.NULL)
						|| driverUpdatedDateTimeServer.trim().isEmpty()) {
					result = ESync.ESYNC_SERVER;
				} else {
					strDateServer = sdf.parse(driverUpdatedDateTimeServer);
					strDateLocal = sdf.parse(driverUpdatedDateTimeLocal);
		
					if (strDateLocal.after(strDateServer)) {
						result = ESync.ESYNC_SERVER;
					}
				}
			} 
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private void deleteDuplicateKey(ArrayList<OrderInfo> arrayList) {
		for (int i = 0; i < arrayList.size() - 1; i++) {
			for (int j = i + 1; j < arrayList.size(); j++) {
				if (arrayList.get(i).getCustomerCD().equalsIgnoreCase(arrayList.get(j).getCustomerCD())) {
					arrayList.remove(j);
				}
			}
		}
	}

	/*
	 * sync customer master
	 */
	public void syncCustomerMaster() {
		Log.d(TAG, "start syncCustomerMaster");
		

		ArrayList<CustomerInfo> listCustomerInfo = DatabaseManager.getInstance().getAllCustomerInfoList();

		ArrayList<OrderInfo> customerIDList = DatabaseManager.getInstance()
				.getAllDeliveryOrder();
		deleteDuplicateKey(customerIDList);
		for (int i = 0; i < customerIDList.size(); i++) {
			CustomerInfo customerInfoServer = new CustomerInfo();
			CustomerInfo customerInfoLocal = new CustomerInfo();
			
			// 4.1.A Get the latest updated time of server side.
			customerInfoServer = mReceive
					.getLatestUpdateTimeCustomerInfo(customerIDList.get(i)
							.getCustomerCD());
			
			ArrayList<CustomerInfo> localList = DatabaseManager.getInstance().getCustomerInfoById(customerIDList.get(i).getCustomerCD());
			if (localList.size() > 0) {
			 customerInfoLocal = localList.get(0);
			}
			String updatedDateTimeServer = customerInfoServer.getmUpdateDateTime();
			String updatedDateTimeLocal = "";
			String driverUpdatedDateTimeServer = customerInfoServer.getmDriverUpdateDateTime();
			String driverUpdatedDateTimeLocal = "";
			
			if (listCustomerInfo.size() > 0) {
				updatedDateTimeLocal = customerInfoLocal.getmUpdateDateTime();
				driverUpdatedDateTimeLocal = customerInfoLocal.getmDriverUpdateDateTime();
			}
			
			result = checkLocalSync(updatedDateTimeLocal, updatedDateTimeServer);
			if (result == ESync.ESYNC_LOCAL) {
				if (!customerInfoServer.getCustomerId().trim().isEmpty() 
						|| customerInfoServer.getCustomerId().equalsIgnoreCase(Def.NULL)) {
					Log.w(TAG, "sync local, customer id: " + customerInfoServer.getCustomerId());
					DatabaseManager.getInstance().createOrUpdateCustomer(customerInfoServer);
				}
			}
			
			result = checkServerSync(driverUpdatedDateTimeLocal, driverUpdatedDateTimeServer);
			if (result == ESync.ESYNC_SERVER) {
				Log.w(TAG, "sync server, customer id: " + customerInfoLocal.getCustomerId());
				// 4.1.D Update customer master
				// get list customer in delivery batch data
				mSend.requestToUpdateCustomerMaster(TMSConstrans.mSoshiki_cd,
						customerInfoLocal.getCustomerId(), customerInfoLocal.getConditionsInForntOfHouse(),
						customerInfoLocal.getmDriverUpdateDateTime());
			}

		}
	}
	
	public void syncCustomerImageMaster() {
 		Log.d(TAG, "start syncCustomerImageMaster");
		// Execute for each following data.
		// 8.1.A Get the latest updated time of server side.

 	
 		
		ArrayList<CustomerInfo> customerIDLocalList = DatabaseManager
				.getInstance().getAllCustomerInfoList();
		for (int i = 0; i < customerIDLocalList.size(); i++) {
			ArrayList<CustomerPicInfo> customerPicServerList = new ArrayList<CustomerPicInfo>();
			
			customerPicServerList = mReceive.getCustomerPicInfo(customerIDLocalList.get(i).getCustomerId());
			
			for (int j = 0; j < customerPicServerList.size(); j++) {
				CustomerPicInfo customerPicLocal = new CustomerPicInfo();
				CustomerPicInfo picInfoServer = customerPicServerList.get(j);
				
				ArrayList<CustomerPicInfo> customerPicLocalList = DatabaseManager.getInstance().getListCustomerPicByImageID(picInfoServer.getImageId());
				 
				if(customerPicLocalList.size() > 0) {
					customerPicLocal = customerPicLocalList.get(0);
				} else {
					Log.w(TAG, "sync local, image id: " + picInfoServer.getImageId() + ", customer id:"  + picInfoServer.getCustomerId());
					DatabaseManager.getInstance().createCustomerPic(picInfoServer);
					
					continue;
				}
				
				String updatedDateTimeServer = picInfoServer.getUpdatedDateTime();
				String updatedDateTimeLocal = "";
				String driverUpdatedDateTimeServer = picInfoServer.getmDriverUpdateDateTime();
				String driverUpdatedDateTimeLocal = "";
				
				if (customerIDLocalList.size() > 0) {
					updatedDateTimeLocal = customerPicLocal.getUpdatedDateTime();
					driverUpdatedDateTimeLocal = customerPicLocal.getmDriverUpdateDateTime();
				}
				
				result = checkLocalSync(updatedDateTimeLocal, updatedDateTimeServer);
				if (result == ESync.ESYNC_LOCAL) {
					
					if (!picInfoServer.getCustomerId().trim().isEmpty() 
							|| picInfoServer.getCustomerId().equalsIgnoreCase(Def.NULL)) {
						Log.w(TAG, "sync local, image id: " + picInfoServer.getImageId() + ", customer id:"  + picInfoServer.getCustomerId());
						DatabaseManager.getInstance().createCustomerPic(picInfoServer);
					}
				}
				
				result = checkServerSync(driverUpdatedDateTimeLocal, driverUpdatedDateTimeServer);
				if (result == ESync.ESYNC_SERVER) {
					Log.w(TAG, "sync server, image id: " + picInfoServer.getImageId() + ", customer id:"  + picInfoServer.getCustomerId());
					// 4.1.D Update customer master
					// get list customer in delivery batch data
					mSend.requestUpdateComment(TMSConstrans.mSoshiki_cd, customerPicLocal.getCustomerId(),
							customerPicLocal.getImageId(), customerPicLocal.getImageComment());
				}

			}
		}
		
		DatabaseManager.getInstance().deletePicsWithWitNullImageId();
	}

	/*
	 * sync driver master
	 */
	public void syncDriverMaster() {
		Log.d(TAG, "start syncDriverMaster");
		ArrayList<DriverInfo> listDriverInfo = mReceive
				.getDriverMaster(TMSConstrans.mSoshiki_cd);

		for (int i = 0; i < listDriverInfo.size(); i++) {
			DatabaseManager.getInstance().createOrUpdateDriverInfo(
					listDriverInfo.get(i));
		}
	}

	/*
	 * sync status master
	 */
	public void syncStatusMaster() {
		Log.d(TAG, "start syncStatusMaster");
		ArrayList<OrderStatusInfo> listStatusInfo = mReceive
				.getStatusMaster(TMSConstrans.mSoshiki_cd);
		for (int i = 0; i < listStatusInfo.size(); i++) {
			DatabaseManager.getInstance().createOrUpdateStausInfo(
					listStatusInfo.get(i));
		}
	}

	/*
	 * sync carry out status master
	 */
	public void syncCarryOutStatusMaster() {
		Log.d(TAG, "start syncCarryOutStatusMaster");
		ArrayList<CarryOutStatusMaster> listCarryOut = mReceive
				.getCarryOutMaster(TMSConstrans.mSoshiki_cd);

		for (int i = 0; i < listCarryOut.size(); i++) {
			DatabaseManager.getInstance().createOrUpdateCarryOutStatusMaster(
					listCarryOut.get(i));
		}

	}

	/*
	 * saveDeliveryData
	 */
	public void saveDeliveryData(ArrayList<OrderInfo> listDeliveryOrder) {
		for (int i = 0; i < listDeliveryOrder.size(); i++) {
			Log.w(TAG, "sync local, tracking id: " + listDeliveryOrder.get(i).getTrackingId());
			DatabaseManager.getInstance().createOrUpdateOrderItem(
					listDeliveryOrder.get(i));
		}
	}

}
