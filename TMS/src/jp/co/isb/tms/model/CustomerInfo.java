package jp.co.isb.tms.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//Status Master Table
@DatabaseTable(tableName ="M_KOKYAKU")
public class CustomerInfo {
	@DatabaseField (id = true)
	private String mCustomerId;
	@DatabaseField
	private String mConditionsInForntOfHouse;
	@DatabaseField
	private String mMessage;
	@DatabaseField
	private String mUpdateDateTime;
	@DatabaseField
	private String mDriverUpdateDateTime;

	public CustomerInfo() {

	}
	public String getCustomerId() {
		return mCustomerId;
	}

	public String getmUpdateDateTime() {
		return mUpdateDateTime;
	}
	public void setmUpdateDateTime(String mUpdateDateTime) {
		this.mUpdateDateTime = mUpdateDateTime;
	}
	public String getmDriverUpdateDateTime() {
		return mDriverUpdateDateTime;
	}
	public void setmDriverUpdateDateTime(String mDriverUpdateDateTime) {
		this.mDriverUpdateDateTime = mDriverUpdateDateTime;
	}
	public void setCustomerId(String id) {
		this.mCustomerId = id;
	}
	public String getConditionsInForntOfHouse() {
		return mConditionsInForntOfHouse;
	}

	public void setConditionsInForntOfHouse(String statusName) {
		this.mConditionsInForntOfHouse = statusName;
	}
	
	public void setMessage(String message) {
		this.mMessage = message;
	}
	public String getMessage() {
		return mMessage;
	}
	
}
