package jp.co.isb.tms.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//Status Master Table
@DatabaseTable(tableName ="M_STATUS")
public class OrderStatusInfo {
	@DatabaseField (id = true)
	private String mStatusId;
	@DatabaseField
	private String mStatusName;
	@DatabaseField
	private int mReDeliverableFlag;
	@DatabaseField
	private int mOthersFlag;

	public OrderStatusInfo() {

	}
	public String getStatusId() {
		return mStatusId;
	}

	public void setStatusId(String mStatusId) {
		this.mStatusId = mStatusId;
	}
	public String getStatusName() {
		return mStatusName;
	}

	public void setStatusName(String statusName) {
		this.mStatusName = statusName;
	}
	
	public void setReDeliverableFlag(int flag) {
		this.mReDeliverableFlag = flag;
	}
	public int getReDeliverableFlag() {
		return mReDeliverableFlag;
	}
	public void setOthersFlag(int flag) {
		this.mOthersFlag = flag;
	}
	public int getOthersFlag() {
		return mOthersFlag;
	}

	
}
