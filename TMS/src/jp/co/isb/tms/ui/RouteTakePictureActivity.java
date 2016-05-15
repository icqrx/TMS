package jp.co.isb.tms.ui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import jp.co.isb.tms.R;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.model.CustomerPicInfo;
import jp.co.isb.tms.server.Send;
import jp.co.isb.tms.util.DateTimeUtils;
import jp.co.isb.tms.util.ImageUtils;

import org.apache.commons.httpclient.util.HttpURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent.OnFinished;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteTakePictureActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = RouteTakePictureActivity.class
			.getSimpleName();

	private Button mBtnRegister;
	private EditText mEdtComment;
	public CustomerPicInfo mEditPic = null;
	private CustomerPicInfo mNewPic = null;
	private File inputFile;
	private FileInputStream fileInputStream = null;

	private ProgressDialog progressDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_take_picture);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setupHeaderLayoutView();

		String imageId = (String) getIntent().getStringExtra(
				Def.TAG_PIC_IMAGE_ID);
		mNewPic = (CustomerPicInfo) getIntent()
				.getParcelableExtra(Def.TAG_PIC1);
		if (imageId != null) {
			mEditPic = DatabaseManager.getInstance().getListCustomerPicByImageID(imageId).get(0);
		}

		if (mEditPic != null) {
			((ImageView) findViewById(R.id.iv_pic_detail))
					.setImageBitmap(ImageUtils.convertStringToBitmap(mEditPic
							.getmThumnailData()));
			((TextView) findViewById(R.id.tv_comment)).setText(mEditPic
					.getImageComment());
		}
		if (mNewPic != null) {
			((ImageView) findViewById(R.id.iv_pic_detail))
					.setImageBitmap(mNewPic.getBitmap());
		}

		// set title screen
		mBtnRegister = (Button) findViewById(R.id.btn_register);
		mEdtComment = (EditText) findViewById(R.id.edt_new_comment);

		mBtnRegister.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mEditPic != null) {
			mEditPic.recyleBitmap();
		}
		if (mNewPic != null) {
			mNewPic.recyleBitmap();
		}
		if (fileInputStream != null) {
			fileInputStream = null;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_register:
			final String newComment = mEdtComment.getText().toString();

			if (mNewPic != null) {
				mNewPic.setImageComment(newComment);

				mNewPic.setBitmap(mNewPic.getBitmap());
				mNewPic.setmThumnailData(ImageUtils
						.convertBitmapToBase64(Bitmap.createScaledBitmap(
								mNewPic.getBitmap(), 240, 200, false)));
				mNewPic.setmDriverUpdateDateTime(DateTimeUtils.getSystemTime());

				inputFile = new File(TMSConstrans.mPathImage);

				try {
					fileInputStream = new FileInputStream(inputFile);
					TMSConstrans.fileInputStream = fileInputStream;
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				showProgressDialg(true);

				new Thread(new Runnable() {

					@Override
					public void run() {
						Send mSend = new Send(RouteTakePictureActivity.this);

						mSend.requestToImageUpload(TMSConstrans.mSoshiki_cd,
								TMSConstrans.fileInputStream,
								mNewPic.getCustomerId(),
								mNewPic.getmDriverUpdateDateTime(), newComment);
						Log.d("REQUEST", "OK");
						if (!Def.JSON.equalsIgnoreCase("")) {

							try {
								JSONObject json = new JSONObject(Def.JSON);
								String data = json.getString(Def.DATA);
								Log.d("REQUEST", data);
								JSONObject ob = new JSONObject(data);

								int affected_row = ob.getInt("affected_rows");
								if (affected_row == 1) {
									String image_cd = ob.getString("image_cd");
									String dsp = ob.getString("dsp_sort");
									mNewPic.setImageId(image_cd);
									mNewPic.setDspSort(dsp);
								}
							} catch (JSONException e) {
								Log.e(TAG, "" + e.toString());
							}
						}
						DatabaseManager.getInstance().createCustomerPic(mNewPic);
						Intent returnIntent = getIntent();

						returnIntent.putExtra(Def.TAG_COMMENT_RESULT, newComment);
						returnIntent.putExtra(Def.TAG_THUMBNAIL_RESULT,mNewPic.getmThumnailData());
						returnIntent.putExtra(Def.TAG_IMAGE_ID,mNewPic.getImageId());

						setResult(RESULT_OK, returnIntent);
						
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showProgressDialg(false);
								finish();
							}
						});

					}
				}).start();
			

			}

			if (mEditPic != null) {
				Log.d(TAG, "customer pic key: " + mEditPic.mCustomerImageKey);
				mEditPic.setImageComment(newComment);
				mEditPic.setmDriverUpdateDateTime(DateTimeUtils.getSystemTime());
				Send mSend = new Send(RouteTakePictureActivity.this);
				mSend.requestToUpdateComment(TMSConstrans.mSoshiki_cd,
						mEditPic.getCustomerId(), mEditPic.getImageId(),
						mEditPic.getmDriverUpdateDateTime(), mEditPic.getImageComment());
				Log.d(TAG, "update comment: " + mEditPic.getCustomerId() + " " + mEditPic.getImageId() +  " ok");
				DatabaseManager.getInstance().createOrUpdateCustomerPic(mEditPic);
				Intent returnIntent = getIntent();
				returnIntent.putExtra(Def.TAG_COMMENT_RESULT, newComment);
				setResult(RESULT_OK, returnIntent);
				finish();
			}

			fileInputStream = null;
			break;
		default:
			break;
		}

	}

	public void showProgressDialg(boolean show) {
		if (progressDlg != null) {
			progressDlg.dismiss();
			progressDlg = null;
		}
		if (progressDlg == null) {
			progressDlg = new ProgressDialog(this);
			progressDlg.setIndeterminateDrawable(getResources().getDrawable(
					R.drawable.progress));
			progressDlg.setMessage(getResources().getString(
					R.string.alert_loading));
		}
		if (show) {
			progressDlg.show();
		} else {
			progressDlg.dismiss();
			progressDlg = null;
		}
	}

}
