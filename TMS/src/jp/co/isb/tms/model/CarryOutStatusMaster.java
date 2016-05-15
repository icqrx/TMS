package jp.co.isb.tms.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//Carryout status Table
@DatabaseTable(tableName ="CARRY_OUT_STATUS_MASTER")
public class CarryOutStatusMaster {
	@DatabaseField (id = true)
	private String mStaffCode;
	@DatabaseField
	private String mStatusName;
	
	public CarryOutStatusMaster(){
		
	}
	public CarryOutStatusMaster(String mStaffCode, String mStatusName) {
		super();
		this.mStaffCode = mStaffCode;
		this.mStatusName = mStatusName;
	}
	public String getStaffCode() {
		return mStaffCode;
	}
	
	public void setStaffCode(String mStaffCode) {
		this.mStaffCode = mStaffCode;
	}
	public String getStatusName() {
		return mStatusName;
	}
	public void setStatusName(String mStatusName) {
		this.mStatusName = mStatusName;
	}

	
	
}
