package jp.co.isb.tms.db;

import java.io.File;
import java.sql.SQLException;

import jp.co.isb.tms.model.CarryOutStatusMaster;
import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.CustomerPicInfo;
import jp.co.isb.tms.model.DriverInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.model.OrderStatusInfo;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabasesHelper extends OrmLiteSqliteOpenHelper {
	public static String FOLDER_PATH = "";//Environment.getExternalStorageDirectory().getAbsolutePath() + "/TMS/";
	private static final String DATABASE_NAME = FOLDER_PATH + "tms.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = DatabasesHelper.class.getSimpleName();
	public static DatabasesHelper helper;
	
	private Dao<OrderInfo, Integer> orderDao = null;
	private Dao<DriverInfo, Integer> driverDao = null;
	private Dao<OrderStatusInfo, Integer> statusInfoDao = null;
	private Dao<CarryOutStatusMaster, Integer> carryOutDao = null;
	private Dao<CustomerInfo, Integer> customerDao = null;
	private Dao<CustomerPicInfo, Integer> customerDaoPic = null;
	public DatabasesHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	public static synchronized DatabasesHelper getHelper(Context context) {
		if (helper == null) {
			helper = new DatabasesHelper(context);
		}
		return helper;
	}
	
	public static void createFolder() {
		File folder = new File(FOLDER_PATH);
		boolean success = true;
		if (!folder.exists()) {
		    success = folder.mkdir();
		}
		if (success) {
		    // Do something on success
		} else {
		    // Do something else on failure 
		}
	}
		 
	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
		orderDao =null;
		driverDao = null;
		statusInfoDao = null;
		carryOutDao = null;
		customerDao = null;
		customerDaoPic = null;
	}
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTable(connectionSource, OrderInfo.class);
			TableUtils.createTable(connectionSource, DriverInfo.class);
			TableUtils.createTable(connectionSource, OrderStatusInfo.class);
			TableUtils.createTable(connectionSource, CarryOutStatusMaster.class);
			TableUtils.createTable(connectionSource, CustomerInfo.class);
			TableUtils.createTable(connectionSource, CustomerPicInfo.class);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		// TODO Auto-generated method stub
		try {
			TableUtils.dropTable(connectionSource, OrderInfo.class, true);
			TableUtils.dropTable(connectionSource, DriverInfo.class, true);
			TableUtils.dropTable(connectionSource, OrderStatusInfo.class, true);
			TableUtils.dropTable(connectionSource, CarryOutStatusMaster.class, true);
			TableUtils.dropTable(connectionSource, CustomerInfo.class, true);
			TableUtils.dropTable(connectionSource, CustomerPicInfo.class, true);
		} catch (SQLException e) {
			Log.e(TAG, "" +  e.toString());
		}
	}

	public void clearTable() throws SQLException {
		
		TableUtils.clearTable(getConnectionSource(), OrderInfo.class);
		TableUtils.clearTable(getConnectionSource(), CustomerInfo.class);
		TableUtils.clearTable(getConnectionSource(), CustomerPicInfo.class);
	}

	public Dao<OrderInfo, Integer> getOrderItemDao(){
		if(orderDao == null){
			try {
				orderDao = getDao(OrderInfo.class);
			} catch (SQLException e) {
				Log.e(TAG, "" +  e.toString());
			}
		}
		return this.orderDao;
	}
	public Dao<DriverInfo, Integer> getDriverInfoDao() throws SQLException{
		if(driverDao == null){
			driverDao = getDao(DriverInfo.class);
		}
		return this.driverDao;
	}
	public Dao<OrderStatusInfo, Integer> getStausInfoDao() throws SQLException{
		if(statusInfoDao == null){
			statusInfoDao = getDao(OrderStatusInfo.class);
		}
		return this.statusInfoDao;
	}
	public Dao<CarryOutStatusMaster, Integer> getCarryOutDao() throws SQLException{
		if(carryOutDao == null){
			carryOutDao = getDao(CarryOutStatusMaster.class);
		}
		return this.carryOutDao;
	}
	public Dao<CustomerInfo, Integer> getCustomerDao() throws SQLException{
		if(customerDao == null){
			customerDao = getDao(CustomerInfo.class);
		}
		return this.customerDao;
	}

	public Dao<CustomerPicInfo, Integer> getCustomerPicDao() throws SQLException {
		if(customerDaoPic == null){
			customerDaoPic = getDao(CustomerPicInfo.class);
		}
		return this.customerDaoPic;
	}

}
