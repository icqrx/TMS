package jp.co.isb.tms.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import jp.co.isb.tms.PictureAdapter;
import jp.co.isb.tms.R;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.model.CustomerPicInfo;
import jp.co.isb.tms.util.DateTimeUtils;
import jp.co.isb.tms.util.ImageUtils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.TextView;

public class RoutePictureActivity extends RouteTopDetailActivity implements
		OnClickListener {
	private static final String TAG = RoutePictureActivity.class.getSimpleName();
	private Button mBtnEdit;
	private Button mBtnBack;
	private Button mBtnChangeEstimatedTime;
	private Gallery mDestinationGallary;
	private Button mBtnTakePic;
	private ImageView mSelectedImage;
	private PictureAdapter mPicAdapter;
	private String mNewImage;
	CustomerPicInfo mPicInfo;
	private int mPosition;
	private SharedPreferences pref;  
	private SharedPreferences.Editor editor;  
	
	private ArrayList<CustomerPicInfo> mPicList = new ArrayList<CustomerPicInfo>();
	private View lastView = null;

	private int TAKE_PHOTO_CODE = 1;
	private int TAKE_COMMENT_CODE = 2;
	private int TAKE_NEW_PIC_CODE = 3;
	
	private String mCustomerCD;
	private String mDeliveryEstimatedDateTime = "";
	private String mDeliveryBatchCode = "";
	
	AlertDialog alertDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_picture);
		
		Intent intent = getIntent();
		mCustomerCD =  intent.getStringExtra(Def.TAG_CUSTOMER_CD);
		mDeliveryBatchCode = getIntent().getStringExtra(Def.TAG_DELIVERY_BATCH_CODE);
		mDeliveryEstimatedDateTime = getIntent().getStringExtra(Def.TAG_DELIVERY_ESTIMATED_DATE_TIME);
		
		mBtnChangeEstimatedTime = (Button) findViewById(R.id.btn_change_estimated_time);
		
		setCommonLayout();
		setupHeaderLayoutView();
		
		// get image list from local database
		
		mPicList = DatabaseManager.getInstance().getCustomerPicListByCustomerId(mCustomerCD);
		
		// set title screen
		((TextView) findViewById(R.id.title_screen)).setText(R.string.title_display_route);

		mBtnEdit = (Button) findViewById(R.id.btn_edit);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mDestinationGallary = (Gallery) findViewById(R.id.destination_gallery);
		mBtnTakePic = (Button) findViewById(R.id.btn_take_pic);
		mSelectedImage = (ImageView) findViewById(R.id.selected_image);
		
		mSelectedImage.setImageResource(R.drawable.no_image_icon);
		
		mPicAdapter = new PictureAdapter(this, mPicList);
		mDestinationGallary.setAdapter(mPicAdapter);

		mDestinationGallary.setOnItemSelectedListener(new SelectListener(this));
		
		mBtnChangeEstimatedTime.setOnClickListener(this);

		mBtnEdit.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mBtnTakePic.setOnClickListener(this);
		
		pref = getSharedPreferences("pict", Context.MODE_PRIVATE); 
	}

	private class SelectListener implements AdapterView.OnItemSelectedListener {

		private Animation grow = null;
		

		public SelectListener(Context c) {
			grow = AnimationUtils.loadAnimation(c, R.anim.grow);
		}

		public void onItemSelected(AdapterView<?> parent, View v, int position,
				long id) {

			// Shrink the view that was zoomed
			try {
				if (null != lastView)
					lastView.clearAnimation();
			} catch (Exception clear) {
			}

			// Zoom the new selected view
			try {
				mPosition = position;
				v.startAnimation(grow);
				mSelectedImage.setImageBitmap(ImageUtils.convertStringToBitmap(mPicList.get(position).getmThumnailData()));
				((TextView) findViewById(R.id.tv_comment)).setText(mPicList.get(position).getImageComment());
			} catch (Exception animate) {
			}

			// Set the last view so we can clear the animation
			lastView = v;
		}

		public void onNothingSelected(AdapterView<?> parent) {
		}

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (int i = 0; i < mPicList.size(); i++) {
			mPicList.get(i).recyleBitmap();
		}
	}
	
	@Override
	protected void enableUpdateTimeButton(boolean flag) {
		super.enableUpdateTimeButton(flag);
		
		mBtnChangeEstimatedTime.setEnabled(flag);
		
		if (flag) {
			mBtnChangeEstimatedTime.setBackgroundResource(R.drawable.btn_blue);
		} else {
			mBtnChangeEstimatedTime.setBackgroundResource(R.drawable.btn_blue_selected);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_edit:
			if (mPicList.size() > 0) {
				Intent intent = new Intent(RoutePictureActivity.this, RouteTakePictureActivity.class);
				String imageID = mPicList.get(mPosition).getImageId();
				intent.putExtra(Def.TAG_PIC_IMAGE_ID, imageID );
				startActivityForResult(intent,TAKE_COMMENT_CODE);
			}
			break;

		case R.id.btn_back:
			finish();
			break;

		case R.id.btn_change_estimated_time:
			
			Intent i = new Intent(RoutePictureActivity.this,
					RouteUpdateEstimatedTimeActivity.class);
			i.putExtra(Def.TAG_DELIVERY_BATCH_CODE, mDeliveryBatchCode);
			i.putExtra(Def.TAG_DELIVERY_ESTIMATED_DATE_TIME, mDeliveryEstimatedDateTime);

			startActivity(i);
			break;

		case R.id.btn_take_pic:
			final String dir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
					+ "/picFolder/";
			File newdir = new File(dir);
			newdir.mkdirs();

			mNewImage = dir + new Random().nextInt() + ".jpg";
			TMSConstrans.mPathImage = mNewImage;
			File newfile = new File(mNewImage);
			try {
				newfile.createNewFile();
			} catch (IOException e) {
			}

			Uri outputFileUri = Uri.fromFile(newfile);
			
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

			startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
			
			editor = pref.edit();  
			editor.clear();
			editor.putString(  
			    "mNewImage",  
			    mNewImage
			);  
		    editor.commit();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
			// add new image 
			mNewImage = pref.getString("mNewImage", "noData");
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			
			try {
			
				Bitmap bitmap = BitmapFactory.decodeFile(mNewImage, options);
			
				if (bitmap != null) {
					mPicInfo = new CustomerPicInfo();
					mPicInfo.setCustomerId(mCustomerCD);
					
					bitmap = Bitmap.createScaledBitmap(bitmap, 240, 200, false);
					mPicInfo.setBitmap(bitmap);
					Intent it = new Intent(RoutePictureActivity.this,RouteTakePictureActivity.class);
					
					it.putExtra(Def.TAG_PIC1, mPicInfo);
					startActivityForResult(it,TAKE_NEW_PIC_CODE);
					
				}
			
			} catch (Exception e) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		        alertDialogBuilder.setTitle("警告");
		        alertDialogBuilder.setMessage("メモリー不足です。");
		        alertDialogBuilder.setCancelable(true);
		        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		            	alertDialog.dismiss();
		            }
		        });
		        alertDialog = alertDialogBuilder.create();
		        alertDialog.show();
			}
		}
		
		if(requestCode == TAKE_COMMENT_CODE && resultCode == RESULT_OK){
			
			String flagDelete = data.getStringExtra(Def.TAG_DELETE_ITEM);
			String comment = data.getStringExtra(Def.TAG_COMMENT_RESULT);
			// remove item
			if(flagDelete != null && flagDelete.equals(Def.TAG_DELETE)){
				//int index = mDestinationGallary.getSelectedItemPosition();
				CustomerPicInfo pos = mPicList.get(mPosition);
				String imageID = mPicList.get(mPosition).getImageId();
				DatabaseManager.getInstance().deletePicsWithImageId(imageID);
				mPicList.remove(pos);
				try {
					if (null != lastView)
						lastView.clearAnimation();
				} catch (Exception clear) {
				}
				if(mPicList.size() == 0){
					mSelectedImage.setImageDrawable(getResources().getDrawable(R.drawable.no_image_icon));
					((TextView) findViewById(R.id.tv_comment)).setText("");
				}
				
				mPicAdapter.notifyDataSetChanged();
				
			}else{
				// add comment
				Log.w("COMMENT", comment);
				((TextView) findViewById(R.id.tv_comment)).setText(comment);
				mPicList.get(mPosition).setImageComment(comment);
			}
		}
		
		if(requestCode == TAKE_NEW_PIC_CODE && resultCode == RESULT_OK ){
			String comment = data.getStringExtra(Def.TAG_COMMENT_RESULT);
			String thumData = data.getStringExtra(Def.TAG_THUMBNAIL_RESULT);
			String imageID = data.getStringExtra(Def.TAG_IMAGE_ID);
			
			if (mPicInfo != null) {
				mPicInfo.setImageComment(comment);
				mPicInfo.setmThumnailData(thumData);
				mPicInfo.setImageId(imageID);
				mPicList.add(0,mPicInfo);
			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		        alertDialogBuilder.setTitle("警告");
		        alertDialogBuilder.setMessage("メモリー不足です。");
		        alertDialogBuilder.setCancelable(true);
		        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		            	alertDialog.dismiss();
		            }
		        });
		        alertDialog = alertDialogBuilder.create();
		        alertDialog.show();

			}
			
			mPosition = 0;
			mSelectedImage.setImageBitmap(ImageUtils.convertStringToBitmap(mPicList.get(0).getmThumnailData()));
			((TextView) findViewById(R.id.tv_comment)).setText(mPicList.get(0).getImageComment());

			mPicAdapter.notifyDataSetChanged();
		}
	}
}
