package jp.co.isb.tms.ui;

import jp.co.isb.tms.R;
import jp.co.isb.tms.common.ConfirmEnableNetworkDialog;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.server.Sync;
import jp.co.isb.tms.util.NetworkUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

public class BaseActivity extends Activity implements OnClickListener {
	private Button mBtnMenu;
	private Button mBtnSyc;
	private ProgressBar mProgressLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	protected void setupHeaderLayoutView() {
		mBtnMenu = (Button) findViewById(R.id.btn_menu);
		mBtnSyc = (Button) findViewById(R.id.btn_sync);
		mProgressLoading = (ProgressBar)findViewById(R.id.progress_loading);
		
		if (mBtnMenu != null) {
			mBtnMenu.setOnClickListener(this);
		}
		if (mBtnSyc != null) {
			mBtnSyc.setOnClickListener(this);
		}
		
		//registerSync();
	}
	
	public void registerSync() {
		if (TMSConstrans.mLoadData != null && TMSConstrans.mLoadData.isSyncing()) {
			mProgressLoading.setVisibility(View.VISIBLE);
			
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					while (TMSConstrans.mLoadData.isSyncing()) {
//					}
//					runOnUiThread(new Runnable() {
//						
//						@Override
//						public void run() {
//							mProgressLoading.setVisibility(View.INVISIBLE);
//						}
//					});
					
//				}
//			}).start();;
		
		} else {
			mProgressLoading.setVisibility(View.INVISIBLE);
		}
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	notifiyChange();
        }
    };
    
    protected void notifiyChange() {
    	
    }

	
	@Override
	protected void onPause() {
		super.onPause();
		
		unregisterReceiver(receiver);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
        filter.addAction(Def.BROADCAST_ACTION);
        registerReceiver(receiver, filter);
        
		registerSync();
	}
	
	public class LoadingData extends AsyncTask<Void, Void, Void>{
		
		private boolean isSyncing = false;
		
		public LoadingData() {
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Sync sync = new Sync(getBaseContext());
				sync.executeSync();
				//Thread.sleep(10000);
			} catch (Exception e) {
				Log.e("LoadingData", "" +  e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mProgressLoading.setVisibility(View.INVISIBLE);
			
			isSyncing = false;
		}

		@Override
		protected void onPreExecute() {
			isSyncing = true;
			mProgressLoading.setVisibility(View.VISIBLE);
		}
		public boolean isSyncing() {
			return isSyncing;
		}
		public void setSyncing(boolean isSyncing) {
			this.isSyncing = isSyncing;
		}
	}
	
	// Sync Data
	public void syncData(boolean autoFlag){
		if (!NetworkUtils.checkNetworkAvailable(getBaseContext())){
			if (!autoFlag) {
				new ConfirmEnableNetworkDialog(this).show();
			}
		}else{
			try {
				TMSConstrans.mLoadData = new LoadingData();
				TMSConstrans.mLoadData.execute();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_sync:
			
			syncData(false);
			
			break;
		case R.id.btn_menu:

			gotoMenuScreen();

			break;

		default:
			break;
		}

	}
	
	public void gotoMenuScreen() {
		Intent newIntent = new Intent(this, MenuActivity.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(newIntent);
	}
	
	public void gotoRouteListScreen() {
		Intent newIntent = new Intent(this, RouteListActivity.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(newIntent);
	}

}
