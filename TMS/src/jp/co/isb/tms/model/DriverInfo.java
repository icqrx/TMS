package jp.co.isb.tms.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

// Driver Master Table
@DatabaseTable(tableName ="M_DRIVER")
public class DriverInfo {
	
	@DatabaseField (id = true)
	private String mDriverId;
	
	@DatabaseField
	private String mDriverLastName;
	
	@DatabaseField
	private String mDriverFirstName;
	
	@DatabaseField
	private String mDriverLastNameInKatakana;
	
	@DatabaseField
	private String mDriverFirstNameInKatakana;
	
	public DriverInfo() {
	}

	public String getDriverId() {
		return mDriverId;
	}

	public void setDriverId(String mDriverId) {
		this.mDriverId = mDriverId;
	}

	public String getDriverLastName() {
		return mDriverLastName;
	}

	public void setDriverLastName(String name) {
		this.mDriverLastName = name;
	}
	
	public String getDriverFirstName() {
		return mDriverFirstName;
	}
	
	public String getDriverFullName(){
		return mDriverLastName + " " + mDriverFirstName;
	}

	public void setDriverFirstName(String name) {
		this.mDriverFirstName = name;
	}
	
	public String getDriverLastNameInKatakana() {
		return mDriverLastNameInKatakana;
	}

	public void setDriverLastNameInKatakana(String name) {
		this.mDriverLastNameInKatakana = name;
	}

	public String getDriverFirstNameInKatakana() {
		return mDriverFirstNameInKatakana;
	}

	public void setDriverFirstNameInKatakana(String name) {
		this.mDriverFirstNameInKatakana = name;
	}

}
