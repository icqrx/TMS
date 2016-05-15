package jp.co.isb.tms.server;

import java.io.FileInputStream;

import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.IGetAccessToken;
import jp.co.isb.tms.common.IGetTransferResult;
import jp.co.isb.tms.common.INotifyChange;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.common.UserInfoPref;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.util.HttpUtils;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Send {

	public static final String TAG = Send.class.getSimpleName();
	private Context mContext;

	public Send(Context context) {
		mContext = context;
	}

	public static class SendObject extends AsyncTask<Void, Void, Integer> {
		private int mWhatApi;
		private INotifyChange mNotifyChange;
		private IGetAccessToken mAccessToken;
		private IGetTransferResult mGetTransferResult;
		private byte[] mImageData;
		private String[] mParams;

		public SendObject(int aWhatApi, INotifyChange iNotifyChange,
				String... params) {
			this.mWhatApi = aWhatApi;
			this.mParams = params;
			this.mNotifyChange = iNotifyChange;
		}

		public SendObject(int aWhatApi, INotifyChange iNotifyChange,
				IGetTransferResult aGetTransferResult, String... params) {
			this.mWhatApi = aWhatApi;
			this.mGetTransferResult = aGetTransferResult;
			this.mParams = params;
			this.mNotifyChange = iNotifyChange;
		}

		public SendObject(int aWhatApi, INotifyChange iNotifyChange,
				IGetAccessToken iAccessToken, String... params) {
			this.mWhatApi = aWhatApi;
			this.mNotifyChange = iNotifyChange;
			this.mAccessToken = iAccessToken;
			this.mParams = params;
		}

		public SendObject(int aWhatApi, INotifyChange iNotifyChange,
				byte[] imageData, String... params) {
			this.mWhatApi = aWhatApi;
			this.mNotifyChange = iNotifyChange;
			this.mImageData = imageData;
			this.mParams = params;
		}

		@Override
		protected void onPreExecute() {
			mNotifyChange.notify(0);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			if (mWhatApi == Def.HTTP_LOGOUT) {
				return HttpUtils.logoutRequest(mParams);
			} else if (mWhatApi == Def.HTTP_TRANSFER) {
				return HttpUtils.transferRequest(mParams);
			} else if (mWhatApi == Def.HTTP_UPLOAD_IMAGES) {
				return HttpUtils.uploadImagesRequest(mImageData, mParams);
			} else if (mWhatApi == Def.HTTP_UPLOAD_COMMENT) {
				return HttpUtils.updateCommentRequest(mParams);
			} else if (mWhatApi == Def.HTTP_GPS) {
				return HttpUtils.request(Def.HTTP_GPS, Def.BASE_GPS_ADD_URL,
						mParams);
			}
			return HttpUtils.loginRequest(mParams);
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.d(TAG, "onPostExecute: " + result.intValue());
			if (mWhatApi == Def.HTTP_LOGIN) {
				Log.d(TAG, "accessToken: " + HttpUtils.getAccessToken());
				mAccessToken.sendAccessToken(HttpUtils.getAccessToken());
			} else if (mWhatApi == Def.HTTP_TRANSFER) {
				mGetTransferResult.sendTransferResult(HttpUtils.isTransfered());
			}
			mNotifyChange.notify(result);
		}
	}

	/*
	 * Update Delivery Estimated Time
	 * 
	 * @param OrderInfo object
	 * 
	 * @author ptlinh
	 * 
	 * @date 20131203
	 */
	public void requestToUpdateDelvieryEstimatedTime(OrderInfo orderInfo) {

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, orderInfo.getDeliveryBatchCode(),
				orderInfo.getCarryOutCheckFlag(),
				orderInfo.getCarryOutStatusCd(),
				orderInfo.getEstimatedDeliveryTime(),
				orderInfo.getDeliveryCompletedDateTime(), orderInfo.mStatusId,
				orderInfo.getDeliveredTimes(),
				orderInfo.getDriverUpdatedDateTime(), orderInfo.getDriverCode() };
		HttpUtils.request(Def.HTTP_DBATCH_UPDATE_ESTIMATED,
				Def.UPDATE_DELIVERY_BATCHS_API, params);
	}

	/*
	 * Update Delivery Estimated Time
	 * 
	 * @param OrderInfo object
	 * 
	 * 
	 * @author ptlinh
	 * 
	 * @date 20131203
	 */
	public void requestToUpdateComment(String organizationCode,
			String customerCode, String ImageCD,

			String dataTime, String comment) {

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, customerCode, ImageCD, dataTime,
				comment };

		HttpUtils.request(Def.HTTP_UPLOAD_COMMENTS, Def.IMAGE_UPDATE_API,
				params);
	}

	public void requestToUpdateDriverDeliveryAverageIntervalTime(
			String soshikiCD, String averageIntervalTime) {

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, averageIntervalTime };

		HttpUtils.request(Def.HTTP_DBATCH_UPDATE_AVEGARE_INTERVAL_TIME,
				Def.AVERAGE_INTERVAL_TIME_API, params);
	}

	public void requestToUpdateDeliveryBatch(String organizationCode,
			String deliveryBatchCode, String driverUpdateDateTime,
			String driverDateTime, String status) {

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, deliveryBatchCode,
				driverUpdateDateTime, status, driverDateTime };
		HttpUtils.request(Def.HTTP_DBATCH_UPDATE,
				Def.UPDATE_DELIVERY_BATCHS_API, params);
	}

	public void requestToUpdateDeliveryBatchToServer(String organizationCode,
			String deliveryBatchCode, String carryOutStatusCode,
			String driverUpdateDateTime) {

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, deliveryBatchCode,
				carryOutStatusCode, driverUpdateDateTime };
		HttpUtils.request(Def.HTTP_DBATCH_UPDATE_SERVER,
				Def.UPDATE_DELIVERY_BATCHS_API, params);
	}

	public void requestToUpdateDiplayingOrder(String organizationCode,
			String deliveryBatchCode, String displayingOrder,
			String driverUpdatedDateTime,String bundledBatchCode) {

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, deliveryBatchCode, displayingOrder,
				driverUpdatedDateTime,bundledBatchCode };

		HttpUtils.request(Def.HTTP_DBATCH_UPDATE_DISPLAY,
				Def.UPDATE_DELIVERY_BATCHS_API, params);
	}

	public void requestToImageUpload(String organizationCode,
			FileInputStream fileInputStream, String customerCode,
			String dataTime, String comment) {

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, customerCode, dataTime, comment };
		HttpUtils.request1(Def.HTTP_UPLOAD_IMAGE, Def.IMAGE_UPLOAD_API,
				fileInputStream, params);
	}

	public void requestRegisterDBatchStatus(String organizationCode,
			String deliveryBatchCode, String bundleBatchCode, String mtd_flg,
			String mtd_status_cd, String deliveryDate, String updatedDateTime,
			String modifiedDriverDatetime, String presetCode,
			String presetName, String deliverySetCode, String deliverySetName,
			String routeCode, String routeName, String shipperCode,
			String shipperTypeCode, String trackingID, String customerCD,
			String customerPostalCode, String packageQuantity,
			String StockOutDate, String squareMeterOfPackage,
			String cubicMeterOfPackage, String packageWeight,
			String subTotalBeforeTax, String cashOnDeliveryFlag,
			String consumptionTax, String billingAmount,
			String StockOutPaySlipNumber, String CODCommission,
			String statusCode, String deliveryEstimatedDateTime,
			String deliveryCompletedDateTime, String deliveryTimes) {

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, deliveryBatchCode, bundleBatchCode,
				mtd_flg, mtd_status_cd, deliveryDate, updatedDateTime,
				modifiedDriverDatetime, presetCode, presetName,
				deliverySetCode, deliverySetName, routeCode, routeName,
				shipperCode, shipperTypeCode, trackingID, customerCD,
				customerPostalCode, packageQuantity, StockOutDate,
				squareMeterOfPackage, cubicMeterOfPackage, cashOnDeliveryFlag,
				subTotalBeforeTax, consumptionTax, billingAmount,
				StockOutPaySlipNumber, CODCommission, statusCode,
				deliveryEstimatedDateTime, deliveryCompletedDateTime,
				deliveryTimes };

		HttpUtils.request(Def.HTTP_REGISTER_DBATCH_STATUS,
				Def.UPDATE_REGISTER_BATCHS_STATUS, params);
	}

	public void requestToUpdateCustomerMaster(String organizationCode,
			String customerCode, String conditionsInFrontOfHouse,
			String driverUpdatedDateTime) {
		// TODO Auto-generated method stub

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, customerCode,
				conditionsInFrontOfHouse, driverUpdatedDateTime };
		HttpUtils.request(Def.HTTP_UPDATE_CUSTOMER_API,
				Def.UPDATE_CUSTOMER_API, params);
	}

	public void requestUpdateComment(String organizationCode,
			String customerCode, String imageCode, String commentContent) {
		// TODO Auto-generated method stub
		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext),
				TMSConstrans.mSoshiki_cd, customerCode, imageCode,
				commentContent };
		HttpUtils.request(Def.HTTP_UPDATE_COMMENT_API, Def.UPDATE_COMMENT_API,
				params);
	}

	public void requestToSendGPSData(String organizationCode, double latitude,
			double longitude, String deviceIP,
			String informationConfidenceLevel, String deliveryDate,
			String presetCode, String presetName, String deliverySetCode,
			String deliverySetName, String routeCode, String routeName,
			String driverRegisteredDateTime, String registeredDriverCode) {

		String[] params = { UserInfoPref.getPrefUserCode(mContext),
				UserInfoPref.getPrefAccessToken(mContext), organizationCode,
				String.valueOf(latitude), String.valueOf(longitude), deviceIP,
				informationConfidenceLevel, deliveryDate, presetCode,
				presetName, deliverySetCode, deliverySetName, routeCode,
				routeName, driverRegisteredDateTime, registeredDriverCode };
		HttpUtils.request(Def.HTTP_GPS, Def.BASE_GPS_ADD_URL, params);
	}

}
