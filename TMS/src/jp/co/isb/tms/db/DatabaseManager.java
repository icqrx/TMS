package jp.co.isb.tms.db;

import java.sql.SQLException;
import java.util.ArrayList;

import jp.co.isb.tms.ECarryOutStatus;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.model.CarryOutStatusMaster;
import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.CustomerPicInfo;
import jp.co.isb.tms.model.DriverInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.model.OrderStatusInfo;
import jp.co.isb.tms.util.DateTimeUtils;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

public class DatabaseManager {
	private static final String TAG = DatabaseManager.class.getSimpleName();
	private static DatabaseManager instance;

	static public void init(Context ctx) {
		DatabasesHelper.createFolder();
		if (null == instance) {
			instance = new DatabaseManager(ctx);
		}
	}

	static public DatabaseManager getInstance() {
		return instance;
	}

	private DatabasesHelper helper;

	private DatabaseManager(Context ctx) {
		helper = new DatabasesHelper(ctx);
	}

	private DatabasesHelper getHelper() {
		return helper;
	}
	
	/*
	 * create a new device item
	 */
	public void createOrUpdateOrderItem(OrderInfo item){
		try {
			getHelper().getOrderItemDao().createOrUpdate(item);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}
	/*
	 * create a new customer pic
	 */
	public void createOrUpdateCustomerPic(CustomerPicInfo item){
		try {
			getHelper().getCustomerPicDao().createOrUpdate(item);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}
	public void createCustomerPic(CustomerPicInfo item){
		try {
			getHelper().getCustomerPicDao().create(item);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}
	/*
	 * create a new customer
	 */
	public void createOrUpdateCustomer(CustomerInfo item){
		try {
			getHelper().getCustomerDao().createOrUpdate(item);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}
	public ArrayList<CustomerInfo> getAllCustomerInfoList(){
		ArrayList<CustomerInfo> list = new ArrayList<CustomerInfo>();
		try {
			list = (ArrayList<CustomerInfo>) getHelper().getCustomerDao().queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	public ArrayList<CustomerInfo> getCustomerInfoById(String customerID){
		ArrayList<CustomerInfo> list = new ArrayList<CustomerInfo>();
		if (customerID == null) {
			return list;
		}
		try {
			QueryBuilder<CustomerInfo, Integer> queryBuilder = getHelper().getCustomerDao().queryBuilder();
			list = (ArrayList<CustomerInfo>) queryBuilder.where().eq("mCustomerId", customerID).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	public ArrayList<CustomerPicInfo> getAllCustomerPicInfoList(){
		ArrayList<CustomerPicInfo> list = new ArrayList<CustomerPicInfo>();
		try {
			list = (ArrayList<CustomerPicInfo>) getHelper().getCustomerPicDao().queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	public ArrayList<CustomerPicInfo> getCustomerPicListByCustomerId(String customerID) {
		ArrayList<CustomerPicInfo> list = new ArrayList<CustomerPicInfo>();
		try {
			QueryBuilder<CustomerPicInfo, Integer> queryBuilder = getHelper().getCustomerPicDao().queryBuilder();
			list = (ArrayList<CustomerPicInfo>) queryBuilder.where().in("mCustomerId", customerID).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	public ArrayList<CustomerPicInfo> getListCustomerPicByImageID(String imageId) {
		ArrayList<CustomerPicInfo> list = new ArrayList<CustomerPicInfo>();
		try {
			QueryBuilder<CustomerPicInfo, Integer> queryBuilder = getHelper().getCustomerPicDao().queryBuilder();
			list = (ArrayList<CustomerPicInfo>) queryBuilder.where().in("mImageId", imageId).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	public CustomerPicInfo getCustomerPicByImageID(String imageId) {
		CustomerPicInfo list = new CustomerPicInfo();
		try {
			QueryBuilder<CustomerPicInfo, Integer> queryBuilder = getHelper().getCustomerPicDao().queryBuilder();
			list =  (CustomerPicInfo) queryBuilder.where().eq("mImageId", imageId).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	/*
	 * delete image by image id
	 */
	public void deletePicsWithImageId(String imageID) {
		try {
			DeleteBuilder<CustomerPicInfo, Integer> deleteBuilder = getHelper().getCustomerPicDao().deleteBuilder();
					deleteBuilder.where().eq("mImageId", imageID);
					deleteBuilder.delete();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		
	}
	
	public boolean deletePicsWithWitNullImageId() {
		try {
			DeleteBuilder<CustomerPicInfo, Integer> deleteBuilder = getHelper().getCustomerPicDao().deleteBuilder();
					deleteBuilder.where().in("mImageId", "");
					deleteBuilder.delete();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return true;
	}
	
	/*
	 * create a new driver master
	 */
	public void createOrUpdateDriverInfo(DriverInfo item){
		try {
			getHelper().getDriverInfoDao().createOrUpdate(item);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}
	/*
	 * create a new carry out
	 */
	public void createOrUpdateCarryOutStatusMaster(CarryOutStatusMaster item){
		try {
			getHelper().getCarryOutDao().createOrUpdate(item);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}
	/*
	 * create a new status info
	 */
	public void createOrUpdateStausInfo(OrderStatusInfo item) {
		try {
			getHelper().getStausInfoDao().createOrUpdate(item);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}
	/*
	 * get list all delivery order
	 */
	public ArrayList<OrderInfo> getAllDeliveryOrder()  {
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			list = (ArrayList<OrderInfo>) getHelper().getOrderItemDao().queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	public ArrayList<OrderInfo> getAllDeliveryOrderGroupByCustomerID()  {
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			QueryBuilder<OrderInfo, Integer> queryBuilder = getHelper().getOrderItemDao().queryBuilder();
			list = (ArrayList<OrderInfo>) queryBuilder.groupBy("mCustomerCD").query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	/*
	 * get list all delivery order for status
	 */
	public ArrayList<OrderInfo> getDeliveryOrderWithStatus(ArrayList<String> status)  {
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			QueryBuilder<OrderInfo, Integer> orderBuilder = getHelper().getOrderItemDao().queryBuilder();
		
			orderBuilder.where().in("mStatusId", status);
			orderBuilder.orderByRaw("CAST(mAllocationPriorityOrder AS INTEGER)");
			orderBuilder.orderByRaw("CAST(mDisplayingOrder AS INTEGER)");
			
			list = (ArrayList<OrderInfo>) orderBuilder.query();
			//list = (ArrayList<OrderInfo>)orderBuilder.where().in("mStatusId", status).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	/*
	 * get list all delivery order for status
	 */
	public ArrayList<OrderInfo> getDeliveryOrderForCarryOutWithStatus(ArrayList<String> status)  {
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			QueryBuilder<OrderInfo, Integer> orderBuilder = getHelper().getOrderItemDao().queryBuilder();
		
			orderBuilder.where().in("mStatusId", status);
			orderBuilder.orderByRaw("CAST(mAllocationPriorityOrder AS INTEGER)");
			orderBuilder.orderByRaw("CAST(mDisplayingOrder AS INTEGER) DESC");
			
			list = (ArrayList<OrderInfo>) orderBuilder.query();
			//list = (ArrayList<OrderInfo>)orderBuilder.where().in("mStatusId", status).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	public OrderInfo getCarrOutDetail(String deliveryBatCode) {
		OrderInfo info = new OrderInfo();
		try {
			ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
			QueryBuilder<OrderInfo, Integer> queryBuilder = getHelper().getOrderItemDao().queryBuilder();
			list = (ArrayList<OrderInfo>) queryBuilder.where().in("mDeliveryBatchCode", deliveryBatCode).query();
			info = list.get(0);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return info;
	}
	
	/*
	 * get list all delivery order for status
	 */
	public ArrayList<OrderInfo> getDeliveryOrderForRouteWithStatus(ArrayList<String> status)  {
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			QueryBuilder<OrderInfo, Integer> queryBuilder = getHelper().getOrderItemDao().queryBuilder();
			queryBuilder.groupBy("mBundleBatchCode");
			queryBuilder.orderByRaw("CAST(mAllocationPriorityOrder AS INTEGER)");
			queryBuilder.orderByRaw("CAST(mDisplayingOrder AS INTEGER)");
			list = (ArrayList<OrderInfo>) queryBuilder.where().eq("mCarryOutCheckFlag", ECarryOutStatus.YES.getValue())
										.and()
										.in("mStatusId", status).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	public ArrayList<OrderInfo> getDeliveryOrderForRouteDontCarryOut(ArrayList<String> status)  {
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			QueryBuilder<OrderInfo, Integer> queryBuilder = getHelper().getOrderItemDao().queryBuilder();
			queryBuilder.groupBy("mBundleBatchCode");
			queryBuilder.orderByRaw("CAST(mAllocationPriorityOrder AS INTEGER)");
			queryBuilder.orderByRaw("CAST(mDisplayingOrder AS INTEGER)");
			list = (ArrayList<OrderInfo>) queryBuilder.where().in("mStatusId", status).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	
	public ArrayList<CustomerPicInfo> getAllCustomerPic() {
		ArrayList<CustomerPicInfo> list = new ArrayList<CustomerPicInfo>();
		try {
			list = (ArrayList<CustomerPicInfo>) getHelper().getCustomerPicDao()
					.queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	/*
	 * get list all driver master
	 */
	public ArrayList<DriverInfo> getAllDriverInfo() {
		ArrayList<DriverInfo> list = new ArrayList<DriverInfo>();
		try {
			list = (ArrayList<DriverInfo>) getHelper().getDriverInfoDao().queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	/*
	 * get list all status info
	 */
	public ArrayList<OrderStatusInfo> getAllStatusInfo() throws SQLException {
		ArrayList<OrderStatusInfo> list = new ArrayList<OrderStatusInfo>();
		list = (ArrayList<OrderStatusInfo>) getHelper().getStausInfoDao().queryForAll();
		return list;
	}
	
	/*
	 * get list all status info
	 */
	public ArrayList<OrderStatusInfo> getStatusWithOtherFlag() throws SQLException {
		ArrayList<OrderStatusInfo> list = new ArrayList<OrderStatusInfo>();
		
		list = (ArrayList<OrderStatusInfo>) getHelper().getStausInfoDao().queryForAll();
		QueryBuilder<OrderStatusInfo, Integer> statusInfo = getHelper().getStausInfoDao().queryBuilder();
	
		statusInfo.where().eq("mOthersFlag", 1);
		
		list = (ArrayList<OrderStatusInfo>) statusInfo.query();
		return list;
	}
	
	/*
	 * get list all status info
	 */
	public ArrayList<CarryOutStatusMaster> getAllCarryOutStatusInfo() {
		ArrayList<CarryOutStatusMaster> list = new ArrayList<CarryOutStatusMaster>();
		try {
			list = (ArrayList<CarryOutStatusMaster>) getHelper().getCarryOutDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean updateDeliveryOrderByStatus(String deliveryBatchCode, String statusCD) {
		try {
			UpdateBuilder<OrderInfo, Integer> updateBuilder = getHelper().getOrderItemDao().updateBuilder();
			updateBuilder.updateColumnValue("mStatusCode", statusCD);
			updateBuilder.updateColumnValue("mDriverUpdatedDateTime", DateTimeUtils.getSystemTime());
			updateBuilder.updateColumnValue("mDriverCode", TMSConstrans.mDriver_code);
			updateBuilder.where().in("mDeliveryBatchCode", deliveryBatchCode);
			updateBuilder.update();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return true;
	}
	
	public boolean updateDeliveryOrderByCarryOutStatusCd(String deliveryBatchCode, String carryOutStatusCd) {
		try {
			UpdateBuilder<OrderInfo, Integer> updateBuilder = getHelper().getOrderItemDao().updateBuilder();
			updateBuilder.updateColumnValue("mCarryOutStatusCd", carryOutStatusCd);
			updateBuilder.updateColumnValue("mDriverUpdatedDateTime", DateTimeUtils.getSystemTime());
			updateBuilder.updateColumnValue("mDriverCode", TMSConstrans.mDriver_code);
			updateBuilder.where().in("mDeliveryBatchCode", deliveryBatchCode);
			updateBuilder.update();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return true;
	}
	
	public boolean updateDeliveryOrderByBundle(String bundleBatchCode, String status) {
		try {
			UpdateBuilder<OrderInfo, Integer> updateBuilder = getHelper().getOrderItemDao().updateBuilder();
			updateBuilder.updateColumnValue("mDeliveryCompletedDateTime", DateTimeUtils.getSystemTime());
			updateBuilder.updateColumnValue("mStatusId", status);
			updateBuilder.updateColumnValue("mDriverUpdatedDateTime", DateTimeUtils.getSystemTime());
			updateBuilder.updateColumnValue("mDriverCode", TMSConstrans.mDriver_code);
			updateBuilder.where().in("mBundleBatchCode", bundleBatchCode);
			updateBuilder.update();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return true;
	}
	
	public ArrayList<OrderStatusInfo> getStatusForWithIds(ArrayList<String> ids) {
		ArrayList<OrderStatusInfo> list = new ArrayList<OrderStatusInfo>();
		try {
			QueryBuilder<OrderStatusInfo, Integer> queryBuilder = getHelper().getStausInfoDao().queryBuilder();
			list = (ArrayList<OrderStatusInfo>) queryBuilder.where().in("mStatusId", ids).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	public ArrayList<CarryOutStatusMaster> getCarryOutStatusForWithIds(ArrayList<String> ids) {
		ArrayList<CarryOutStatusMaster> list = new ArrayList<CarryOutStatusMaster>();
		try {
			QueryBuilder<CarryOutStatusMaster, Integer> queryBuilder = getHelper().getCarryOutDao().queryBuilder();
			list = (ArrayList<CarryOutStatusMaster>) queryBuilder.where().in("mStaffCode", ids).query();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	/*
	 * Delete local data of the the date that is not current data
	 */
	public void deleteLocalData(String systemTime) {
		
		try {
			Dao<OrderInfo, Integer> dao = getHelper().getOrderItemDao();
			DeleteBuilder<OrderInfo, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().ne("mDeliveryDate", systemTime);
			deleteBuilder.delete();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}
	
	public boolean updateDeliveryData(ArrayList<String> deliveryBatchCodeList, String carroutFlag) {
		try {
			UpdateBuilder<OrderInfo, Integer> updateBuilder = getHelper().getOrderItemDao().updateBuilder();
			updateBuilder.updateColumnValue("mCarryOutCheckFlag", carroutFlag);
			updateBuilder.updateColumnValue("mDriverUpdatedDateTime", DateTimeUtils.getSystemTime());
			updateBuilder.updateColumnValue("mDriverCode", TMSConstrans.mDriver_code);
			updateBuilder.where().in("mDeliveryBatchCode", deliveryBatchCodeList);
			updateBuilder.update();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
			return false;
		}
		return true;
	}
	
	public boolean updateEstimatedTime(String deliveryBatchCode, String deliveryEstimatedTime) {
		try {
			UpdateBuilder<OrderInfo, Integer> updateBuilder = getHelper().getOrderItemDao().updateBuilder();
			updateBuilder.updateColumnValue("mEstimatedDeliveryTime", deliveryEstimatedTime);
			updateBuilder.updateColumnValue("mDriverUpdatedDateTime", DateTimeUtils.getSystemTime());
			updateBuilder.updateColumnValue("mDriverCode", TMSConstrans.mDriver_code);
			updateBuilder.where().in("mDeliveryBatchCode", deliveryBatchCode);
			updateBuilder.update();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
			return false;
		}
		
		return true;
	}
	
	public boolean updateCustomerMaster(String customerId, String conditionsInFontOfHouse) {
		try {
			UpdateBuilder<CustomerInfo, Integer> updateBuilder = getHelper().getCustomerDao().updateBuilder();
			updateBuilder.updateColumnValue("mConditionsInForntOfHouse", conditionsInFontOfHouse);
			updateBuilder.updateColumnValue("mDriverUpdateDateTime", DateTimeUtils.getSystemTime());
			updateBuilder.where().in("mCustomerId", customerId);
			updateBuilder.update();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
			return false;
		}
		return true;
	}
	
	public boolean updateCarryoutFlag(String deliveryBatchCode, ECarryOutStatus carryOutStatus) {
		try {
			UpdateBuilder<OrderInfo, Integer> updateBuilder = getHelper().getOrderItemDao().updateBuilder();
			updateBuilder.updateColumnValue("mCarryOutCheckFlag", carryOutStatus.getValue());
			updateBuilder.updateColumnValue("mCarryOutStatusCd", "");
			updateBuilder.updateColumnValue("mDriverUpdatedDateTime", DateTimeUtils.getSystemTime());
			updateBuilder.updateColumnValue("mDriverCode", TMSConstrans.mDriver_code);
			updateBuilder.where().in("mDeliveryBatchCode", deliveryBatchCode);
			updateBuilder.update();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return true;
	}
	
	public boolean updateCarryoutFlagWithOthers(String deliveryBatchCode, ECarryOutStatus carryOutStatus) {
		try {
			UpdateBuilder<OrderInfo, Integer> updateBuilder = getHelper().getOrderItemDao().updateBuilder();
			updateBuilder.updateColumnValue("mCarryOutCheckFlag", carryOutStatus.getValue());
			updateBuilder.updateColumnValue("mDriverUpdatedDateTime", DateTimeUtils.getSystemTime());
			updateBuilder.updateColumnValue("mDriverCode", TMSConstrans.mDriver_code);
			updateBuilder.where().in("mDeliveryBatchCode", deliveryBatchCode);
			
			updateBuilder.update();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return true;
	}
	
	public boolean updateCarryoutByTrackingId(String trackingID){
		try {
			UpdateBuilder<OrderInfo, Integer> updateBuilder = getHelper().getOrderItemDao().updateBuilder();
			updateBuilder.updateColumnValue("mCarryOutCheckFlag", ECarryOutStatus.YES.getValue());
			updateBuilder.updateColumnValue("mCarryOutStatusCd", "");
			updateBuilder.where().in("mTrackingId", trackingID);
			updateBuilder.update();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return true;
	}
	
	public String getCarryOutCheckName(String carryoutCheckFlag) {
		ArrayList<CarryOutStatusMaster> masters = getAllCarryOutStatusInfo();
		for (int i = 0; i < masters.size(); i++) {
			if (carryoutCheckFlag.equalsIgnoreCase(masters.get(i).getStaffCode())) {
				return masters.get(i).getStatusName();
			}
		}
		return "";
	}
	
	public ArrayList<OrderInfo> getDataAssignedToDriver(String bundleBatchCode){
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			QueryBuilder<OrderInfo, Integer> queryBuilder = getHelper().getOrderItemDao().queryBuilder();
			list = (ArrayList<OrderInfo>) queryBuilder.where().eq("mCarryOutCheckFlag", ECarryOutStatus.YES.getValue())
										.and()
										.eq("mBundleBatchCode", bundleBatchCode).query();

		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	public ArrayList<OrderInfo> getDataAssignedToDriverDontCarryOut(String bundleBatchCode){
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			QueryBuilder<OrderInfo, Integer> queryBuilder = getHelper().getOrderItemDao().queryBuilder();
			list = (ArrayList<OrderInfo>) queryBuilder.where().eq("mCarryOutCheckFlag", ECarryOutStatus.NO.getValue())
										.and()
										.eq("mBundleBatchCode", bundleBatchCode).query();

		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	public ArrayList<OrderInfo> getDataAssignedToDriverAll(String bundleBatchCode){
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			QueryBuilder<OrderInfo, Integer> queryBuilder = getHelper().getOrderItemDao().queryBuilder();
			list = (ArrayList<OrderInfo>) queryBuilder.where()
										.eq("mBundleBatchCode", bundleBatchCode).query();

		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	
	public ArrayList<OrderInfo> getDeliveryBatchCode(String deliveryBatchCode){
		ArrayList<OrderInfo> list = new ArrayList<OrderInfo>();
		try {
			QueryBuilder<OrderInfo, Integer> queryBuilder = getHelper().getOrderItemDao().queryBuilder();
			list = (ArrayList<OrderInfo>) queryBuilder.where()
										.eq("mDeliveryBatchCode", deliveryBatchCode).query();

		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return list;
	}
	public void clearAllTable(){
		
		try {
			getHelper().clearTable();
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}

	public int deleteOrderItem(OrderInfo mOrderItemInfo) {
		try {
			return getHelper().getOrderItemDao().delete(mOrderItemInfo);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return -1;
	}

	public int deleteDriverItem(DriverInfo driverInfo){
		try {
			return getHelper().getDriverInfoDao().delete(driverInfo);
		} catch (Exception e) {
			Log.e(TAG, "" +  e.toString());
		}
		return -1;
	}
	
	public void createOrderItem(OrderInfo orderInfo) {
		try {
			getHelper().getOrderItemDao().create(orderInfo);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}
	
}	
