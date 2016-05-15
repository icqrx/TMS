package jp.co.isb.tms.ui;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import jp.co.isb.tms.CarrayOutAdapter;
import jp.co.isb.tms.ECarryOutStatus;
import jp.co.isb.tms.R;
import jp.co.isb.tms.barcode.BarcodeReaderThread;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.IGetBarCode;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.dialog.ConfirmDialog;
import jp.co.isb.tms.dialog.OnConfirmListener;
import jp.co.isb.tms.model.CustomerInfo;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.server.Send;
import jp.co.isb.tms.util.DateTimeUtils;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CarryOutListActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = CarryOutListActivity.class
			.getSimpleName();

	protected static UUID Uuid;

	private Button mPackageDeleteButton;
	private Button mTimeSettingButton;
	private Button mPacakgeConfirm;

	private ListView mListView;
	private CarrayOutAdapter mAdapter;
	private ArrayList<OrderInfo> mCarryOutList = new ArrayList<OrderInfo>();
	private ArrayList<String> mDeliveryBatchCodeList = new ArrayList<String>();
	private ArrayList<String> ids = new ArrayList<String>();
	
	private ArrayList<BarcodeReaderThread> readerThreads = new ArrayList<BarcodeReaderThread>();
	private ArrayList<Thread> threadList = new ArrayList<Thread>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carry_out_list);

		setupHeaderLayoutView();

		// set title screen
		((TextView) findViewById(R.id.title_screen))
				.setText(R.string.title_carry_out);

		mPackageDeleteButton = (Button) findViewById(R.id.btn_package_delete);
		mTimeSettingButton = (Button) findViewById(R.id.btn_time_settings);
		mPacakgeConfirm = (Button) findViewById(R.id.btn_package_confirm);

		mPackageDeleteButton.setOnClickListener(this);
		mTimeSettingButton.setOnClickListener(this);
		mPacakgeConfirm.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.listview_carry_out);

		// create database provider

		
		ids.add("000"); // un-delivered
		ids.add("050"); // re-delivered
		ids.add("040"); // under_investigate
		ids.add("030"); // delivering

		mCarryOutList = DatabaseManager.getInstance().getDeliveryOrderForCarryOutWithStatus(ids);
		for (int i = 0; i < mCarryOutList.size(); i++) {
			mDeliveryBatchCodeList.add(mCarryOutList.get(i).getDeliveryBatchCode());
		}
		
		if (mCarryOutList.size() > 0) {
			((TextView) findViewById(R.id.tv_hikiate_kbn))
			.setText(mCarryOutList.get(0).getAllocationTypeName());
		}
		
		if (TMSConstrans.MOCHIDASHI_FLG == 0) {
			mPacakgeConfirm.setVisibility(View.GONE);
		}
		
		//Set Message
		for (int i = 0; i < mCarryOutList.size(); i++) {
			String customerCD = mCarryOutList.get(i).getCustomerCD();
			ArrayList<String> idList = new ArrayList<String>();
			idList.add(mCarryOutList.get(i).getStatusId());
			
			ArrayList<CustomerInfo> cusList = DatabaseManager.getInstance().getCustomerInfoById(customerCD);
			
			if (cusList.size() > 0) {
				mCarryOutList.get(i).setmMessage(cusList.get(0).getMessage());
			}

		}

		mAdapter = new CarrayOutAdapter(this, mCarryOutList);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				mAdapter.setSelectedPostition(position);
				Log.d(TAG, "open CarryOutDetailActivity");
				Intent i = new Intent(CarryOutListActivity.this,
						CarryOutDetailActivity.class);
				i.putExtra(Def.TAG_TOTAL, mCarryOutList.size());
				i.putExtra(Def.TAG_DELIVERY_BATCH_CODE, mCarryOutList.get(position).getDeliveryBatchCode());
				startActivityForResult(i, Def.REQ_CODE_CARRY_OUT_DETAIL);
			}

		});
		mListView.setAdapter(mAdapter);

		((TextView) findViewById(R.id.tv_total_item)).setText(getResources()
				.getString(R.string.total_item,
						String.valueOf(mCarryOutList.size())));
		
	}

	private String TOI_NO = "";
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
    	
    	if (TMSConstrans.mBarcode_check_flag) {
    		
    		if (event.getAction() == 0) {
    		
	            switch (event.getUnicodeChar()) {
	            case Def.BARCODE_START_CHAR:
	            	TOI_NO = "";
	                return false;
	            case Def.BARCODE_LAST_CHAR:
	            	
	            	if (TOI_NO.length() >= 10) {
	            		Log.d(TAG, "TOI_NO :" + TOI_NO );
	            		
	            		boolean foundFlg = false;
	            		for(int i=0; i < mCarryOutList.size(); i++) {
	            			
	            			if (mCarryOutList.get(i).getTrackingId().equals(TOI_NO)) {
	            				foundFlg = true;
	            				break;
	            			}
	            		}
	            		
	            		if (foundFlg) {
	            			DatabaseManager.getInstance().updateCarryoutByTrackingId(TOI_NO);
	            			this.notifiyChange();
	            		}
	            	}
	            	
	            	TOI_NO = "";
	            	return false;
	            case '0':
	            	TOI_NO = TOI_NO + "0";
	                return false;
	            case '1':
	            	TOI_NO = TOI_NO + "1";
	                return false;
	            case '2':
	            	TOI_NO = TOI_NO + "2";
	                return false;
	            case '3':
	            	TOI_NO = TOI_NO + "3";
	                return false;
	            case '4':
	            	TOI_NO = TOI_NO + "4";
	                return false;
	            case '5':
	            	TOI_NO = TOI_NO + "5";
	                return false;
	            case '6':
	            	TOI_NO = TOI_NO + "6";
	                return false;
	            case '7':
	            	TOI_NO = TOI_NO + "7";
	                return false;
	            case '8':
	            	TOI_NO = TOI_NO + "8";
	                return false;
	            case '9':
	            	TOI_NO = TOI_NO + "9";
	                return false;
	            default:
	            }
    		}
		}
    	
    	if( event.getKeyCode() == 4) {
    		return super.dispatchKeyEvent(event);
    	} else {
    		return false;
    	}
    }

	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		
		for (int i = 0; i < readerThreads.size(); i++) {
			readerThreads.get(i).isStop = true;
		}
		for (int j = 0; j < threadList.size(); j++){
			threadList.get(j).interrupt();
		}
	}
	
	@Override
	protected void notifiyChange() {
		Log.d(TAG, "notifiyChange");
		super.notifiyChange();
		
		mCarryOutList = DatabaseManager.getInstance()
				.getDeliveryOrderForCarryOutWithStatus(ids);
    	mAdapter.setmItemList(mCarryOutList);
    	mAdapter.notifyDataSetChanged();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		refreshCarryOutList();
		
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult");

		switch (requestCode) {
		case Def.REQ_CODE_CARRY_OUT_DETAIL:
			if (resultCode == RESULT_OK) {
				((OrderInfo) mAdapter.getItem(mAdapter.getSelectedPostition())).setCarryOutCheckFlag(ECarryOutStatus.YES.getValue());
				mAdapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
	}
	
	private void refreshCarryOutList() {
		mCarryOutList = DatabaseManager.getInstance().getDeliveryOrderForCarryOutWithStatus(ids);
		mAdapter.setmItemList(mCarryOutList);
		mAdapter.notifyDataSetChanged();
	}
	

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_package_delete:
			new ConfirmDialog(this, getString(R.string.alert_dialog_confirm_delete), 
					new OnConfirmListener() {
				
				@Override
				public void onYes() {
					for (int i = 0; i < mDeliveryBatchCodeList.size(); i++) {
						updateData(mDeliveryBatchCodeList.get(i), ECarryOutStatus.NO.getValue());
					}
					
					DatabaseManager.getInstance().updateDeliveryData(mDeliveryBatchCodeList, ECarryOutStatus.NO.getValue());
					refreshCarryOutList();
				}
				
				@Override
				public void onNo() {
					
				}
			}).show();;
			break;

		case R.id.btn_time_settings:

			startActivity(new Intent(this, CarryOutTimeSettingsActivity.class));
			break;
		case R.id.btn_package_confirm:
			new ConfirmDialog(this, getString(R.string.alert_dialog_confirm_package), 
					new OnConfirmListener() {
				
				@Override
				public void onYes() {
					for (int i = 0; i < mDeliveryBatchCodeList.size(); i++) {
						updateData(mDeliveryBatchCodeList.get(i), ECarryOutStatus.YES.getValue());
					}
					DatabaseManager.getInstance().updateDeliveryData(mDeliveryBatchCodeList, ECarryOutStatus.YES.getValue());
					refreshCarryOutList();
				}
				
				@Override
				public void onNo() {
					
				}
			}).show();;

			break;

		default:
			break;
		}

	}
	public void updateData(String deliveryBatchCode,String carryOutStatusCode){
		Send mSend = new Send(this);
		mSend.requestToUpdateDeliveryBatchToServer(TMSConstrans.mSoshiki_cd, deliveryBatchCode, 
				carryOutStatusCode, DateTimeUtils.getSystemTime());
		
	}
}
