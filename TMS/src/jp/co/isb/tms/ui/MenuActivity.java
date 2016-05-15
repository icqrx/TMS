package jp.co.isb.tms.ui;

import jp.co.isb.tms.R;
import jp.co.isb.tms.common.ConfirmDeviceNoSupportNfcDialog;
import jp.co.isb.tms.common.ConfirmEnableNFCDialog;
import jp.co.isb.tms.common.ConfirmEnableNetworkDialog;
import jp.co.isb.tms.common.ConfirmUnknownErrorDialog;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.INotifyChange;
import jp.co.isb.tms.common.StoragePref;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.common.UserInfoPref;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.db.DatabasesHelper;
import jp.co.isb.tms.server.Send;
import jp.co.isb.tms.util.NfcUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends BaseActivity implements OnClickListener, INotifyChange{
	
	private static final String TAG = MenuActivity.class.getSimpleName();
	
	private static final int MSG_AUTO_SYNC = 1001;
	private Button mBtnSettings;
	private Button mBtnCarryOut;
	private Button mBtnDisplayRoute;
	private Button mBtnReceiveData;
	private Button mBtnLogout;
	private DatabasesHelper helper;
	private Handler mHandler;
	private ProgressDialog progressDlg;
	
	private Handler mSyncHandler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_AUTO_SYNC:
				syncData(true);
				break;
			}
			return false;
		}
	});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		setupHeaderLayoutView();
		
		autoSync();
		DatabaseManager.init(this);
		StoragePref.loadConfig(this);

		mBtnSettings = (Button) findViewById(R.id.btn_settings);
		mBtnCarryOut = (Button) findViewById(R.id.btn_carry_out);
		mBtnDisplayRoute = (Button) findViewById(R.id.btn_display_route);
		mBtnReceiveData = (Button) findViewById(R.id.btn_receive_data);
		mBtnLogout = (Button) findViewById(R.id.btn_logout);
		
		mBtnSettings.setOnClickListener(this);
		mBtnCarryOut.setOnClickListener(this);
		mBtnDisplayRoute.setOnClickListener(this);
		mBtnReceiveData.setOnClickListener(this);
		mBtnLogout.setOnClickListener(this);
		
		mHandler = new Handler() {
			private int mStatus;

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case Def.MESSAGE_DELAY: {
						showProgressDialg(false);
						if (mStatus == Def.SERVER_ERROR) {
							new ConfirmEnableNetworkDialog(MenuActivity.this).show();
						}
						break;
					}
					default: 
						break;
				}
				super.handleMessage(msg);
			}
		};		
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		return false;
	}
	
	/*
	 * auto sync
	 */
	public void autoSync() {
		mSyncHandler.sendEmptyMessageDelayed(MSG_AUTO_SYNC, TMSConstrans.mAuto_sync_time_device);
	}

	private void logout() {
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(Def.MESSAGE_DELAY, Def.TIME_OUT);
		}
		Log.d(TAG, "access token = " + UserInfoPref.getPrefAccessToken(this));
		new Send.SendObject(Def.HTTP_LOGOUT, MenuActivity.this,  UserInfoPref.getPrefUserCode(this), UserInfoPref.getPrefAccessToken(this)).execute();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mSyncHandler.removeMessages(MSG_AUTO_SYNC);
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_logout:
			logout();
			break;
		case R.id.btn_settings:
			startActivity(new Intent(this, DeliveryOrderListActivity.class));
			break;
			
		case R.id.btn_carry_out:
			startActivity(new Intent(this, CarryOutListActivity.class));
			break;
			
		case R.id.btn_display_route:
			startActivity(new Intent(this, RouteListActivity.class));
			break;

		case R.id.btn_receive_data:
			if (NfcUtils.hasNfcFeature(getBaseContext())) {
				if (!NfcUtils.isNfcEnabled(getBaseContext())) {
					new ConfirmEnableNFCDialog(MenuActivity.this).show();
				} else {
					startActivity(new Intent(this, ReceiveDataActivity.class));
				}
			} else {
				new ConfirmDeviceNoSupportNfcDialog(MenuActivity.this).show();
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {	
		super.onBackPressed();
		finish();
	}
	
	private void postExecute(int status) {
		if (status != Def.SERVER_ERROR) {
			showProgressDialg(false);
			if (mHandler != null) {
				mHandler.removeMessages(Def.MESSAGE_DELAY);
			}
		}
		
		switch (status) {
			case Def.OK: 		
				Log.d(TAG, "Logout OK");
				clearPref();
				directLoginScreen();
				break;
				
			case Def.FAIL: 
				new ConfirmEnableNetworkDialog(this).show();
				break;
				
			default:
				new ConfirmUnknownErrorDialog(this).show();
				break;				
		}
	}

	private void directLoginScreen() {
		finish();
		Intent newIntent = new Intent(this, LoginActivity.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		newIntent.putExtra(Def.IS_LOG_OUT, true);
		startActivity(newIntent);
	}

	private void clearPref() {
		UserInfoPref.setPrefAutoLogin(this, false);
		UserInfoPref.setPrefAccessToken(this, "");
		UserInfoPref.setPrefUserCode(this, "");
		UserInfoPref.setPrefPassword(this, "");
	}

	@Override
	public void notify(int status) {
		switch (status) {
			case 0: 
				showProgressDialg(true);
				break;
			default: 
				postExecute(status);
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
			progressDlg.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
			progressDlg.setMessage(getResources().getString(R.string.alert_logout));
		}
		if (show) {
			progressDlg.show();
		} else {
			progressDlg.dismiss();
			progressDlg = null;
		}
	}
}
