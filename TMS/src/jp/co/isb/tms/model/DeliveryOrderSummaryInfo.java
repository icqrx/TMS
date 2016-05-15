package jp.co.isb.tms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DeliveryOrderSummaryInfo implements Parcelable   {
	
	private String mHouses;
	private String mPackages;
	private String mWeight;
	private String mCapacity;

	public DeliveryOrderSummaryInfo() {
		this.mHouses = "0";
		this.mPackages = "0";
		this.mWeight = "0";
		this.mCapacity = "0";
	}

	public String getHouses() {
		return mHouses;
	}

	public void setHouses(String mHouses) {
		this.mHouses = mHouses;
	}
	
	// Increase houses by a specified value
	public void increaseHouses(Integer mHouses) {
		this.mHouses = Integer.toString(Integer.parseInt(this.mHouses) + mHouses);
	}

	/**
	 * @return the mPackages
	 */
	public String getPackages() {
		return mPackages;
	}

	/**
	 * @param mPackages the mPackages to set
	 */
	public void setPackages(String mPackages) {
		this.mPackages = mPackages;
	}
	
	// Increase packages by a specified value
	public void increasePackages(Integer mPackages) {
		this.mPackages = Integer.toString(Integer.parseInt(this.mPackages) + mPackages);
	}

	/**
	 * @return the mWeight
	 */
	public String getWeight() {
		return mWeight;
	}

	/**
	 * @param mWeight the mWeight to set
	 */
	public void setWeight(String mWeight) {
		this.mWeight = mWeight;
	}
	
	// Increase Weight by a specified value
	public void increaseWeight(Float mWeight) {
		this.mWeight = Float.toString(Float.parseFloat(this.mWeight) + mWeight);
	}

	/**
	 * @return the mCapacityt
	 */
	public String getCapacity() {
		return mCapacity;
	}

	/**
	 * @param mCapacityt the mCapacityt to set
	 */
	public void setCapacity(String mCapacity) {
		this.mCapacity = mCapacity;
	}
	
	// Increase Weight by a specified value
	public void increaseCapacity(Float mCapacity) {
		this.mCapacity = Float.toString(Float.parseFloat(this.mCapacity) + mCapacity);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mHouses);
		out.writeString(mPackages);
		out.writeString(mWeight);
		out.writeString(mCapacity);
	}

	public static final Parcelable.Creator<DeliveryOrderSummaryInfo> CREATOR = new Parcelable.Creator<DeliveryOrderSummaryInfo>() {
		public DeliveryOrderSummaryInfo createFromParcel(Parcel in) {
			return new DeliveryOrderSummaryInfo(in);
		}

		public DeliveryOrderSummaryInfo[] newArray(int size) {
			return new DeliveryOrderSummaryInfo[size];
		}
	};

	protected DeliveryOrderSummaryInfo(Parcel in) {
		mHouses = in.readString();
		mPackages = in.readString();
		mWeight = in.readString();
		mCapacity = in.readString();
	}

}
