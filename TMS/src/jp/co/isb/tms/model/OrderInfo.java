package jp.co.isb.tms.model;

import java.io.Serializable;
import jp.co.isb.tms.util.SerializeUtils;
import android.os.Parcel;
import android.os.Parcelable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
// DeliveryBatchData Table

@DatabaseTable(tableName="D_HAISO_BATCH")
public class OrderInfo implements Parcelable, Serializable {
	private static final long serialVersionUID = 1L;
	
	@DatabaseField(id = true)
	private String mDeliveryBatchCode = "";
	@DatabaseField
	private String mOrganizationCode = "";
	@DatabaseField(canBeNull = true)
	protected String mTrackingId = "";
	@DatabaseField(canBeNull = true)
	protected String mOrderId = "";
	@DatabaseField(canBeNull = true)
	protected String mCustomerName = "";
	@DatabaseField(canBeNull = true)
	protected String mDepartmentName = "";
	@DatabaseField(canBeNull = true)
	protected String mNote = "";
	@DatabaseField(canBeNull = true)
	protected String mShipperName = "";
	@DatabaseField(canBeNull = true)
	protected String mFirstDeliveryDate = "";
	@DatabaseField(canBeNull = true)
	protected String mEstimatedDeliveryTime = "";
	@DatabaseField(canBeNull = true)
	protected String mReceiverAddress = "";
	@DatabaseField(canBeNull = true)
	protected String mReceiverName = "";
	@DatabaseField(canBeNull = true)
	protected String mPersonInChargeAtCustomerSide = "";
	@DatabaseField(canBeNull = true)
	protected String mPersonInChargeOfReceiverSide = "";
	@DatabaseField(canBeNull = true)
	protected String mMessage = "";
	@DatabaseField(canBeNull = true)
	protected String mConditionInFrontOfHouse = "";
	@DatabaseField(canBeNull = true)
	private boolean mIsOn = false;
	 
	@DatabaseField
	private String mBundleBatchCode;
	@DatabaseField
	public String  mDeliveryDate;
	@DatabaseField
	private String mAllocationType;
	@DatabaseField
	private String mAllocationTypeName;
	@DatabaseField
	public Integer mAllocationPriorityOrder;
	@DatabaseField
	private String mPresetCode;
	@DatabaseField
	private String mPresetName;
	@DatabaseField
	private String mDeliverySetCode;
	@DatabaseField
	private String mDeliverySetName;
	@DatabaseField
	private Integer mDisplayingOrder;
	@DatabaseField
	private String mShipperTypeCode;
	@DatabaseField
	public String mShipperCode;
	@DatabaseField
	public String mPostalCode;
	@DatabaseField
	private String mShipperTypeName;
	@DatabaseField
	private String mAdressCode;
	@DatabaseField
	public String mCustomerCD;
	@DatabaseField
	private String mCustomerPostalCode;
	@DatabaseField
	private String mCustomerAddress;
	@DatabaseField
	private String mCustomerAddress1;
	@DatabaseField
	private String mCustomerAddress2;
	@DatabaseField
	private String mCustomerAddress3;
	@DatabaseField
	private String mCustomerAddress4;
	@DatabaseField
	private String mCustomerTEL1;
	@DatabaseField
	private String mCustomerTEL2;
	@DatabaseField
	private String mCustomerTEL3;
	@DatabaseField
	private String mCustomerTEL4;
	@DatabaseField
	private String mCustomerTEL5;
	@DatabaseField
	private String mExpectedTimeRangeFROM;
	@DatabaseField
	private String mExpectedTimeRangeTO;
	@DatabaseField
	private String mStockOutDate;
	@DatabaseField
	private String mSquareMeterOfPackage;
	@DatabaseField
	private String mCubicMeterOfPackage;
	@DatabaseField
	private String mPackageWeight;
	@DatabaseField
	public String mPackageQuantity;
	@DatabaseField
	private String mCashOnDeliveryFlag;
	@DatabaseField
	private String mSubTotalBeforeTax;
	@DatabaseField
	private String mConsumptionTax;
	@DatabaseField
	private String mBillingAmount;
	@DatabaseField
	private String mStockOutPaySlipNumber;
	@DatabaseField
	private String mCODCommission;
	@DatabaseField
	public String  mDriverCode;
	@DatabaseField
	private String mDeliveryCompletedDateTime;
	@DatabaseField
	public String  mStatusId;
	@DatabaseField
	private String mCarryOutCheckFlag;
	@DatabaseField
	public String mCarryOutStatusName;
	@DatabaseField
	private String mCarryOutStatusCd;
	@DatabaseField
	public String  mDeliveredTimes;
	@DatabaseField
	public String  mStatusName;
	@DatabaseField
	private String mUpdatedDateTime;
	@DatabaseField
	public String  mDeliveryInterval;
	@DatabaseField
	private String mDriverUpdatedDateTime;
	@DatabaseField
	public String  mUpdatedDriverCode;
	@DatabaseField
	private String mRouteCode;
	@DatabaseField
	private String mRouteName;

	private boolean mLateFlag = false;
	
	public OrderInfo() {
		
	}

	public String getTrackingId() {
		return mTrackingId;
	}

	public void setTrackingId(String mTrackingId) {
		this.mTrackingId = mTrackingId;
	}
	public String getOrganizationCode() {
		return mOrganizationCode;
	}

	public void setOrganizationCode(String mOrganizationCode) {
		this.mOrganizationCode = mOrganizationCode;
	}

	public String getOrderId() {
		return mOrderId;
	}

	public void setOrderId(String mOrderId) {
		this.mOrderId = mOrderId;
	}

	public String getShipperCode() {
		return mShipperCode;
	}

	public void setShipperCode(String mShipper) {
		this.mShipperCode = mShipper;
	}

	public String getShipperName() {
		return mShipperName;
	}

	public void setShipperName(String mShipperName) {
		this.mShipperName = mShipperName;
	}

	public String getFirstDeliveryDate() {
		return mFirstDeliveryDate;
	}

	public void setFirstDeliveryDate(String mFirstDeliveryDate) {
		this.mFirstDeliveryDate = mFirstDeliveryDate;
	}

	public String getEstimatedDeliveryTime() {
		return mEstimatedDeliveryTime;
	}

	public void setEstimatedDeliveryTime(String mEstimatedDeliveryTime) {
		this.mEstimatedDeliveryTime = mEstimatedDeliveryTime;
	}

	public String getReceiverAdress() {
		return mReceiverAddress;
	}

	public void setReceiverAddress(String mReceiverAdress) {
		this.mReceiverAddress = mReceiverAdress;
	}

	public String getReceiverName() {
		return mReceiverName;
	}

	public void setReceiverName(String mReceiverName) {
		this.mReceiverName = mReceiverName;
	}

	public String getPersonInChargeOfReceiverSide() {
		return mPersonInChargeOfReceiverSide;
	}

	public void setPersonInChargeOfReceiverSide(
			String mPersonInChargeOfReceiverSide) {
		this.mPersonInChargeOfReceiverSide = mPersonInChargeOfReceiverSide;
	}

	public void setTel1(String mTel1) {
		this.mCustomerTEL1 = mTel1;
	}
	public void setTel2(String mTel2) {
		this.mCustomerTEL2 = mTel2;
	}
	public void setTel3(String mTel3) {
		this.mCustomerTEL3 = mTel3;
	}
	public void setTel4(String mTel4) {
		this.mCustomerTEL4 = mTel4;
	}
	public void setTel5(String mTel5) {
		this.mCustomerTEL5 = mTel5;
	}

	/**
	 * @return the mCustomerName
	 */
	public String getCustomerName() {
		return mCustomerName;
	}

	/**
	 * @param mCustomerName
	 *            the mCustomerName to set
	 */
	public void setCustomerName(String mCustomerName) {
		this.mCustomerName = mCustomerName;
	}

	/**
	 * @return the mDepartmentName
	 */
	public String getDepartmentName() {
		return mDepartmentName;
	}

	/**
	 * @param mDepartmentName
	 *            the mDepartmentName to set
	 */
	public void setDepartmentName(String mDepartmentName) {
		this.mDepartmentName = mDepartmentName;
	}

	/**
	 * @return the mNote
	 */
	public String getNote() {
		return mNote;
	}

	/**
	 * @param mNote
	 *            the mNote to set
	 */
	public void setNote(String mNote) {
		this.mNote = mNote;
	}

	/**
	 * @return the mMemo
	 */
	public String getmMessage() {
		return mMessage;
	}

	/**
	 * @param message
	 *            the mMemo to set
	 */
	public void setmMessage(String message) {
		this.mMessage = message;
	}

	/**
	 * @return the mConditionInFrontOfHouse
	 */
	public String getConditionInFrontOfHouse() {
		return mConditionInFrontOfHouse;
	}

	/**
	 * @return the mPersonInCharge
	 */
	public String getPersonInChargeAtCustomerSide() {
		return mPersonInChargeAtCustomerSide;
	}
	
	/**
	 * @param message
	 *            the mMemo to set
	 */
	public void setPersonInChargeAtCustomerSide(String personInChargeAtCustomerSide) {
		this.mPersonInChargeAtCustomerSide = personInChargeAtCustomerSide;
	}
	

	/**
	 * @param mPersonInCharge
	 *            the mPersonInCharge to set
	 */
	public void PersonInChargeAtCustomerSide(String mPersonInCharge) {
		this.mPersonInChargeAtCustomerSide = mPersonInCharge;
	}
	
	public boolean isIsOn() {
		return mIsOn;
	}
	public void setIsOn(boolean mIsOn) {
		this.mIsOn = mIsOn;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mTrackingId);
		out.writeString(mOrderId);
		out.writeString(mCustomerName);
		out.writeString(mDepartmentName);
		out.writeString(mNote);
		out.writeString(mShipperCode);
		out.writeString(mShipperName);
		out.writeString(mFirstDeliveryDate);
		out.writeString(mEstimatedDeliveryTime);
		out.writeString(mReceiverAddress);
		out.writeString(mReceiverName);
		out.writeString(mPersonInChargeAtCustomerSide);
		out.writeString(mPersonInChargeOfReceiverSide);
		out.writeString(mMessage);
		out.writeString(mDeliveryBatchCode);
		out.writeString(mBundleBatchCode);
		
		out.writeString(mAdressCode);
		out.writeInt(mAllocationPriorityOrder);
		out.writeString(mAllocationType);
		out.writeString(mAllocationTypeName);
		out.writeString(mBillingAmount);
		out.writeString(mCarryOutCheckFlag);
		out.writeString(mCashOnDeliveryFlag);
		out.writeString(mCODCommission);
		out.writeString(mConditionInFrontOfHouse);
		out.writeString(mConsumptionTax);
		out.writeString(mCubicMeterOfPackage);
		out.writeString(mCustomerAddress1);
		out.writeString(mCustomerAddress2);
		out.writeString(mCustomerAddress3);
		out.writeString(mCustomerAddress4);
		out.writeString(mCustomerCD);
		out.writeString(mCustomerPostalCode);
		out.writeString(mCustomerTEL1);
		out.writeString(mCustomerTEL2);
		out.writeString(mCustomerTEL3);
		out.writeString(mCustomerTEL4);
		out.writeString(mCustomerTEL5);
		out.writeString(mDeliveredTimes);
		out.writeString(mDeliveryCompletedDateTime);
		out.writeString(mDeliveryDate);
		out.writeString(mDeliveryInterval);
		out.writeString(mDeliverySetCode);
		out.writeString(mDeliverySetName);
		out.writeString(mEstimatedDeliveryTime);
		out.writeString(mExpectedTimeRangeFROM);
		out.writeString(mExpectedTimeRangeTO);
		out.writeString(mPackageQuantity);
		out.writeString(mPackageWeight);
		out.writeString(mPersonInChargeAtCustomerSide);
		out.writeString(mPersonInChargeOfReceiverSide);
		out.writeString(mPostalCode);
		out.writeString(mPresetCode);
		out.writeString(mPresetName);
		out.writeString(mRouteCode);
		out.writeString(mRouteName);
		out.writeString(mShipperCode);
		out.writeString(mShipperName);
		out.writeString(mShipperTypeCode);
		out.writeString(mShipperTypeName);
		out.writeString(mSquareMeterOfPackage);
		out.writeString(mStatusId);
		out.writeString(mStatusName);
		out.writeString(mStockOutDate);
		out.writeString(mStockOutPaySlipNumber);
		out.writeString(mSubTotalBeforeTax);
		out.writeString(mUpdatedDateTime);
		out.writeString(mUpdatedDriverCode);
		
	}

	public static final Parcelable.Creator<OrderInfo> CREATOR = new Parcelable.Creator<OrderInfo>() {
		public OrderInfo createFromParcel(Parcel in) {
			return new OrderInfo(in);
		}

		public OrderInfo[] newArray(int size) {
			return new OrderInfo[size];
		}
	};

	protected OrderInfo(Parcel in) {
		mTrackingId = in.readString();
		mOrderId = in.readString();
		mCustomerName = in.readString();
		mDepartmentName = in.readString();
		mNote = in.readString();
		mShipperCode = in.readString();
		mShipperName = in.readString();
		mFirstDeliveryDate = in.readString();
		mEstimatedDeliveryTime = in.readString();
		mReceiverAddress = in.readString();
		mReceiverName = in.readString();
		mPersonInChargeAtCustomerSide = in.readString();
		mPersonInChargeOfReceiverSide = in.readString();
		mMessage = in.readString();
		mConditionInFrontOfHouse = in.readString();
		mDeliveryBatchCode = in.readString();
		mBundleBatchCode = in.readString();
		
		mAdressCode = in.readString();
		mAllocationPriorityOrder = in.readInt();
		mAllocationType = in.readString();
		mAllocationTypeName = in.readString();
		mBillingAmount = in.readString();
		mCarryOutCheckFlag = in.readString();
		mCashOnDeliveryFlag = in.readString();
		mCODCommission = in.readString();
		mConsumptionTax = in.readString();
		mCubicMeterOfPackage = in.readString();
		mCustomerAddress1 = in.readString();
		mCustomerAddress2 = in.readString();
		mCustomerAddress3 = in.readString();
		mCustomerAddress4 = in.readString();
		mCustomerPostalCode = in.readString();
		mCustomerCD = in.readString();
		mCustomerTEL1 = in.readString();
		mCustomerTEL2 = in.readString();
		mCustomerTEL3 = in.readString();
		mCustomerTEL4 = in.readString();
		mCustomerTEL5 = in.readString();
		mDeliveredTimes = in.readString();
		mDeliveryCompletedDateTime = in.readString();
		mDeliveryInterval = in.readString();
		mDeliverySetCode = in.readString();
		mDeliverySetName = in.readString();
		mEstimatedDeliveryTime = in.readString();
		mExpectedTimeRangeFROM = in.readString();
		mExpectedTimeRangeTO = in.readString();
		mPackageQuantity = in.readString();
		mPackageWeight = in.readString();
		mPresetCode = in.readString();
		mPostalCode = in.readString();
		mPresetName = in.readString();
		mRouteCode = in.readString();
		mRouteName = in.readString();
		mShipperCode = in.readString();
		mShipperTypeCode = in.readString();
		mShipperTypeName = in.readString();
		mSquareMeterOfPackage = in.readString();
		mStatusId = in.readString();
		mStatusName = in.readString();
		mStockOutDate = in.readString();
		mStockOutPaySlipNumber = in.readString();
		mSubTotalBeforeTax = in.readString();
		mUpdatedDateTime = in.readString();
		mUpdatedDriverCode = in.readString();
		
	}
	
	public byte[] serializeObject(){
		return SerializeUtils.serializeObject(this);
	}
	
	@Override
	public String toString() {	
		return super.toString();
	}
	
	public void setAddress1(String mReceiverAddress1) {
		this.mCustomerAddress1 = mReceiverAddress1;
	}

	public void setAddress2(String mReceiverAddress2) {
		this.mCustomerAddress2 = mReceiverAddress2;
	}
	
	public void setAddress3(String mReceiverAddress3) {
		this.mCustomerAddress3 = mReceiverAddress3;
	}
	
	public void setAddress4(String mReceiverAddress4) {
		this.mCustomerAddress4 = mReceiverAddress4;
	}

	public String getDeliveryBatchCode() {
		return mDeliveryBatchCode;
	}

	public void setDeliveryBatchCode(String mDeliveryBatchCode) {
		this.mDeliveryBatchCode = mDeliveryBatchCode;
	}

	public void setConditionInFrontOfHouse(String mConditionInFrontOfHouse) {
		this.mConditionInFrontOfHouse = mConditionInFrontOfHouse;
	}

	public String getBundleBatchCode() {
		return mBundleBatchCode;
	}

	public void setBundleBatchCode(String mBundleBatchCode) {
		this.mBundleBatchCode = mBundleBatchCode;
	}

	public String getDeliveryDate() {
		return mDeliveryDate;
	}

	public void setDeliveryDate(String mDeliveryDate) {
		this.mDeliveryDate = mDeliveryDate;
	}

	public String getAllocationType() {
		return mAllocationType;
	}

	public void setAllocationType(String mAllocationType) {
		this.mAllocationType = mAllocationType;
	}

	public String getAllocationTypeName() {
		return mAllocationTypeName;
	}

	public void setAllocationTypeName(String mAllocationTypeName) {
		this.mAllocationTypeName = mAllocationTypeName;
	}

	public Integer getAllocationPriorityOrder() {
		return mAllocationPriorityOrder;
	}

	public void setAllocationPriorityOrder(Integer mAllocationPriorityOrder) {
		this.mAllocationPriorityOrder = mAllocationPriorityOrder;
	}

	public String getPresetCode() {
		return mPresetCode;
	}

	public void setPresetCode(String mPresetCode) {
		this.mPresetCode = mPresetCode;
	}

	public String getPresetName() {
		return mPresetName;
	}

	public void setPresetName(String mPresetName) {
		this.mPresetName = mPresetName;
	}

	public String getDeliverySetCode() {
		return mDeliverySetCode;
	}

	public void setDeliverySetCode(String mDeliverySetCode) {
		this.mDeliverySetCode = mDeliverySetCode;
	}

	public String getDeliverySetName() {
		return mDeliverySetName;
	}

	public void setDeliverySetName(String mDeliverySetName) {
		this.mDeliverySetName = mDeliverySetName;
	}

	public Integer getDisplayingOrder() {
		return mDisplayingOrder;
	}

	public void setDisplayingOrder(Integer mDisplayingOrder) {
		this.mDisplayingOrder = mDisplayingOrder;
	}

	public String getShipperTypeCode() {
		return mShipperTypeCode;
	}

	public void setShipperTypeCode(String mShipperTypeCode) {
		this.mShipperTypeCode = mShipperTypeCode;
	}

	public String getPostalCode() {
		return mPostalCode;
	}

	public void setPostalCode(String mPostalCode) {
		this.mPostalCode = mPostalCode;
	}

	public String getShipperTypeName() {
		return mShipperTypeName;
	}

	public void setShipperTypeName(String mShipperTypeName) {
		this.mShipperTypeName = mShipperTypeName;
	}

	public String getAdressCode() {
		return mAdressCode;
	}

	public void setAddressCode(String mAdressCode) {
		this.mAdressCode = mAdressCode;
	}

	public String getCustomerCD() {
		return mCustomerCD;
	}

	public void setCustomerCD(String mCustomerCD) {
		this.mCustomerCD = mCustomerCD;
	}

	public String getCustomerPostalCode() {
		return mCustomerPostalCode;
	}

	public void setCustomerPostalCode(String mCustomerPostalCode) {
		this.mCustomerPostalCode = mCustomerPostalCode;
	}

	public String getCustomerAddress1() {
		return mCustomerAddress1;
	}

	public void setCustomerAddress1(String mCustomerAddress1) {
		this.mCustomerAddress1 = mCustomerAddress1;
	}

	public String getCustomerAddress2() {
		return mCustomerAddress2;
	}

	public void setCustomerAddress2(String mCustomerAddress2) {
		this.mCustomerAddress2 = mCustomerAddress2;
	}

	public String getCustomerAddress3() {
		return mCustomerAddress3;
	}

	public void setCustomerAddress3(String mCustomerAddress3) {
		this.mCustomerAddress3 = mCustomerAddress3;
	}

	public String getCustomerAddress4() {
		return mCustomerAddress4;
	}

	public void setCustomerAddress4(String mCustomerAddress4) {
		this.mCustomerAddress4 = mCustomerAddress4;
	}

	public String getCustomerTEL1() {
		return mCustomerTEL1;
	}

	public void setCustomerTEL1(String mCustomerTEL1) {
		this.mCustomerTEL1 = mCustomerTEL1;
	}

	public String getCustomerTEL2() {
		return mCustomerTEL2;
	}
	

	public void setCustomerTEL2(String mCustomerTEL2) {
		this.mCustomerTEL2 = mCustomerTEL2;
	}

	public String getCustomerTEL3() {
		return mCustomerTEL3;
	}

	public void setCustomerTEL3(String mCustomerTEL3) {
		this.mCustomerTEL3 = mCustomerTEL3;
	}

	public String getCustomerTEL4() {
		return mCustomerTEL4;
	}

	public void setCustomerTEL4(String mCustomerTEL4) {
		this.mCustomerTEL4 = mCustomerTEL4;
	}

	public String getCustomerTEL5() {
		return mCustomerTEL5;
	}

	public void setCustomerTEL5(String mCustomerTEL5) {
		this.mCustomerTEL5 = mCustomerTEL5;
	}

	public String getExpectedTimeRangeFROM() {
		return mExpectedTimeRangeFROM;
	}

	public void setExpectedTimeRangeFROM(String mExpectedTimeRangeFROM) {
		this.mExpectedTimeRangeFROM = mExpectedTimeRangeFROM;
	}

	public String getExpectedTimeRangeTO() {
		return mExpectedTimeRangeTO;
	}

	public void setExpectedTimeRangeTO(String mExpectedTimeRangeTO) {
		this.mExpectedTimeRangeTO = mExpectedTimeRangeTO;
	}

	public String getStockOutDate() {
		return mStockOutDate;
	}

	public void setStockOutDate(String mStockOutDate) {
		this.mStockOutDate = mStockOutDate;
	}

	public String getSquareMeterOfPackage() {
		if(this.mSquareMeterOfPackage == null){
			this.mSquareMeterOfPackage = "0";
		}
		return mSquareMeterOfPackage;
	}

	public void setSquareMeterOfPackage(String mSquareMeterOfPackage) {
		this.mSquareMeterOfPackage = mSquareMeterOfPackage;
	}

	public String getCubicMeterOfPackage() {
		return mCubicMeterOfPackage;
	}

	public void setCubicMeterOfPackage(String mCubicMeterOfPackage) {
		this.mCubicMeterOfPackage = mCubicMeterOfPackage;
	}

	public String getPackageWeight() {
		if(this.mPackageWeight == null){
			this.mPackageWeight = "0";
		}
		return mPackageWeight;
	}

	public void setPackageWeight(String mPackageWeight) {
		this.mPackageWeight = mPackageWeight;
	}

	public String getPackageQuantity() {
		if(this.mPackageQuantity == null){
			this.mPackageQuantity = "0";
		}
		return mPackageQuantity;
	}

	public void setPackageQuantity(String mPackageQuantity) {
		this.mPackageQuantity = mPackageQuantity;
	}

	public String getCashOnDeliveryFlag() {
		return mCashOnDeliveryFlag;
	}

	public void setCashOnDeliveryFlag(String mCashOnDeliveryFlag) {
		this.mCashOnDeliveryFlag = mCashOnDeliveryFlag;
	}

	public String getSubTotalBeforeTax() {
		return mSubTotalBeforeTax;
	}

	public void setSubTotalBeforeTax(String mSubTotalBeforeTax) {
		this.mSubTotalBeforeTax = mSubTotalBeforeTax;
	}

	public String getConsumptionTax() {
		return mConsumptionTax;
	}

	public void setConsumptionTax(String mConsumptionTax) {
		this.mConsumptionTax = mConsumptionTax;
	}

	public String getBillingAmount() {
		return mBillingAmount;
	}

	public void setBillingAmount(String mBillingAmount) {
		this.mBillingAmount = mBillingAmount;
	}

	public String getStockOutPaySlipNumber() {
		return mStockOutPaySlipNumber;
	}

	public void setStockOutPaySlipNumber(String mStockOutPaySlipNumber) {
		this.mStockOutPaySlipNumber = mStockOutPaySlipNumber;
	}

	public String getCODCommission() {
		return mCODCommission;
	}

	public void setCODCommission(String mCODCommission) {
		this.mCODCommission = mCODCommission;
	}

	public String getDriverCode() {
		return mDriverCode;
	}

	public void setDriverCode(String mDriverCode) {
		this.mDriverCode = mDriverCode;
	}

	public String getDeliveryCompletedDateTime() {
		return mDeliveryCompletedDateTime;
	}

	public void setDeliveryCompletedDateTime(String mDeliveryCompletedDateTime) {
		this.mDeliveryCompletedDateTime = mDeliveryCompletedDateTime;
	}

	public String getStatusId() {
		return mStatusId;
	}

	public void setStatusId(String mStatusId) {
		this.mStatusId = mStatusId;
	}

	public String getCarryOutCheckFlag() {
		return mCarryOutCheckFlag;
	}

	public void setCarryOutCheckFlag(String mCarryOutCheckFlag) {
		this.mCarryOutCheckFlag = mCarryOutCheckFlag;
	}

	public String getDeliveredTimes() {
		return mDeliveredTimes;
	}

	public void setDeliveredTimes(String mDeliveredTimes) {
		this.mDeliveredTimes = mDeliveredTimes;
	}

	public String getStatusName() {
		return mStatusName;
	}

	public void setStatusName(String mStatusName) {
		this.mStatusName = mStatusName;
	}

	public String getUpdatedDateTime() {
		return mUpdatedDateTime;
	}

	public void setUpdatedDateTime(String mUpdatedDateTime) {
		this.mUpdatedDateTime = mUpdatedDateTime;
	}

	public String getDeliveryInterval() {
		return mDeliveryInterval;
	}

	public void setDeliveryInterval(String mDeliveryInterval) {
		this.mDeliveryInterval = mDeliveryInterval;
	}

	public String getDriverUpdatedDateTime() {
		return mDriverUpdatedDateTime;
	}

	public void setDriverUpdatedDateTime(String mDriverUpdatedDateTime) {
		this.mDriverUpdatedDateTime = mDriverUpdatedDateTime;
	}

	public String getUpdatedDriverCode() {
		return mUpdatedDriverCode;
	}

	public void setUpdatedDriverCode(String mUpdatedDriverCode) {
		this.mUpdatedDriverCode = mUpdatedDriverCode;
	}

	public String getRouteCode() {
		return mRouteCode;
	}

	public void setRouteCode(String mRouteCode) {
		this.mRouteCode = mRouteCode;
	}

	public String getRouteName() {
		return mRouteName;
	}

	public void setRouteName(String mRouteName) {
		this.mRouteName = mRouteName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Parcelable.Creator<OrderInfo> getCreator() {
		return CREATOR;
	}

	public String getCustomerAddress() {
		return mCustomerAddress;
	}

	public void setCustomerAddress(String mCustomerAddress) {
		this.mCustomerAddress = mCustomerAddress;
	}

	public String getCarryOutStatusCd() {
		return mCarryOutStatusCd;
	}

	public void setCarryOutStatusCd(String mCarryOutStatusCd) {
		this.mCarryOutStatusCd = mCarryOutStatusCd;
	}
	
	public boolean getLateFlag() {
		return mLateFlag;
	}

	public void setLateFlag(boolean mLateFlag) {
		this.mLateFlag = mLateFlag;
	}
}
