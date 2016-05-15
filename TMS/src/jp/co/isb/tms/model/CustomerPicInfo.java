package jp.co.isb.tms.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName ="M_KOKYAKU_IMG")
public class CustomerPicInfo implements Parcelable {
	
	private Bitmap mBitmap;
	@DatabaseField(generatedId =true)
	public int mCustomerImageKey;
	@DatabaseField
	private String mCustomerId;
	
	
	@DatabaseField
	private String mImageId;
	@DatabaseField
	private String dsp_sort;


	@DatabaseField
	private String mThumnailData;
	
	@DatabaseField
	private String mComment;
	@DatabaseField
	private String mUpdatedDateTime;
	@DatabaseField
	private String mDriverUpdateDateTime;
	@DatabaseField
	private String mUpdatedStaff;
	@DatabaseField
	private String mBitmapBuffer;

	public String getmBitmapBuffer() {
		return mBitmapBuffer;
	}
	
	public void recyleBitmap() {
		try{
			if (mBitmap != null) {
				if (! mBitmap.isRecycled()) {
					mBitmap.recycle();
					mBitmap = null;
				}
			
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setmBitmapBuffer(String mBitmapBuffer) {
		this.mBitmapBuffer = mBitmapBuffer;
	}

	public CustomerPicInfo() {

	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public void setBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}
	
	public void setBitmap(String data) {
		// convert data to bitmap
	}
	
	public String getCustomerId() {
		return mCustomerId;
	}

	public void setCustomerId(String id) {
		this.mCustomerId = id;
	}
	
	public String getImageId() {
		return mImageId;
	}
	public String getDspSort() {
		return dsp_sort;
	}

	public void setDspSort(String dsp_sort) {
		this.dsp_sort = dsp_sort;
	}
	public void setImageId(String id) {
		this.mImageId = id;
	}

	public String getImageComment() {
		return mComment;
	}

	public void setImageComment(String comment) {
		this.mComment = comment;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(mBitmap, flags);
		out.writeString(mComment);
		out.writeString(mCustomerId);
		out.writeString(mImageId);
		out.writeString(mUpdatedDateTime);
		out.writeString(mUpdatedStaff);
		out.writeString(mBitmapBuffer);
	}

	public static final Parcelable.Creator<CustomerPicInfo> CREATOR = new Parcelable.Creator<CustomerPicInfo>() {
		public CustomerPicInfo createFromParcel(Parcel in) {
			return new CustomerPicInfo(in);
		}

		public CustomerPicInfo[] newArray(int size) {
			return new CustomerPicInfo[size];
		}
	};

	protected CustomerPicInfo(Parcel in) {
		mBitmap = in.readParcelable(getClass().getClassLoader());
		mComment = in.readString();
		mCustomerId = in.readString();
		mImageId = in.readString();
		mUpdatedDateTime = in.readString();
		mUpdatedStaff = in.readString();
		mBitmapBuffer = in.readString();
	}

	public String getUpdatedDateTime() {
		return mUpdatedDateTime;
	}

	public void setUpdatedDateTime(String mUpdatedDateTime) {
		this.mUpdatedDateTime = mUpdatedDateTime;
	}

	public String getUpdatedStaff() {
		return mUpdatedStaff;
	}

	public void setUpdatedStaff(String mUpdatedStaff) {
		this.mUpdatedStaff = mUpdatedStaff;
	}

	public String getmThumnailData() {
		return mThumnailData;
	}

	public void setmThumnailData(String mThumnailData) {
		this.mThumnailData = mThumnailData;
	}

	public String getmDriverUpdateDateTime() {
		return mDriverUpdateDateTime;
	}

	public void setmDriverUpdateDateTime(String mDriverUpdateDateTime) {
		this.mDriverUpdateDateTime = mDriverUpdateDateTime;
	}

}
