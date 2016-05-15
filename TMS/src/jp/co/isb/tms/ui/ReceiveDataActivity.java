package jp.co.isb.tms.ui;

import jp.co.isb.tms.R;
import jp.co.isb.tms.common.ConfirmEnableNetworkDialog;
import jp.co.isb.tms.common.ConfirmUnknownErrorDialog;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.IGetTransferResult;
import jp.co.isb.tms.common.INotifyChange;
import jp.co.isb.tms.common.UserInfoPref;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.model.OrderInfo;
import jp.co.isb.tms.server.Send;
import jp.co.isb.tms.util.SerializeUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiveDataActivity extends Activity implements INotifyChange{
	private static final String TAG = ReceiveDataActivity.class.getSimpleName();
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	IntentFilter[] mReadTagFilters;
	private OrderInfo receiveOrder;
	private Handler mHandler;
	private Dialog dialog;
	private Button no, yes;
	
	@SuppressLint("NewApi")
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_receive_data);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
			Toast.makeText(this, getResources().getString(R.string.nfc_is_not_available), Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,	getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefDetected.addDataType(Def.NFC_DATA_TYPE);
		} catch (MalformedMimeTypeException e) {
			Log.e(TAG, "Could not add MIME type.");
		}
		mReadTagFilters = new IntentFilter[] { ndefDetected };
		
		mHandler = new Handler() {
			private int mStatus;

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case Def.MESSAGE_DELAY: {
						if (mStatus == Def.SERVER_ERROR) {
							new ConfirmEnableNetworkDialog(ReceiveDataActivity.this).show();
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
	
	@SuppressLint("NewApi")
	NdefMessage[] getNdefMessagesFromIntent(Intent intent) {
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)|| action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				msgs = new NdefMessage[] { msg };
			}
		} else {
			finish();
		}
		return msgs;
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {	
		super.onResume();
		if (!mNfcAdapter.isEnabled()) {
			return;
		}
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mReadTagFilters, null);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onPause() {	
		super.onPause();
		mNfcAdapter.disableForegroundDispatch(this);
	}
	
	private void confirmRetryDialog(){
		dialog = new Dialog(this, R.style.FullHeightDialog);
		 //this is a reference to the style above
		dialog.setContentView(R.layout.custom_dialog); 
		dialog.setCancelable(true);
		
		//to set the message
		TextView message =(TextView) dialog.findViewById(R.id.tvmessagedialogtext);
		message.setText(getResources().getString(R.string.text_retry));
		 
		//add some action to the buttons
        yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
        yes.setText(getResources().getString(R.string.alert_dialog_yes));
        yes.setOnClickListener(new OnClickListener() {
             
            public void onClick(View v) {
                 dialog.dismiss();
            }
        });
         
        no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
        no.setOnClickListener(new OnClickListener() {
             
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
	}
	
	private void postExecute(int status) {
		if (status != Def.SERVER_ERROR) {
			if (mHandler != null) {
				mHandler.removeMessages(Def.MESSAGE_DELAY);
			}
		}
		switch (status) {
			case Def.OK: 	
				if (isTranfered) {
					Log.d(TAG, "Transfer OK");
					finish();
					DatabaseManager.getInstance().createOrderItem(receiveOrder);
					Intent i = new Intent(ReceiveDataActivity.this, DeliveryOrderListActivity.class);
					startActivity(i);
				} else {
					confirmRetryDialog();
				}
				break;
			
			case Def.FAIL: 
				new ConfirmEnableNetworkDialog(this).show();
				break;
				
			default:
				new ConfirmUnknownErrorDialog(this).show();
				break;
		}
	}
	
	@Override
	public void notify(int status) {
		switch (status) {
			case 0: 
				break;
			default: 
				postExecute(status);
				break;
		}
	}
	private boolean isTranfered = false;
	private IGetTransferResult  iGetTransferResult = new IGetTransferResult() {
		
		@Override
		public void sendTransferResult(boolean bResult) {
			isTranfered = bResult;			
		}
	};
	
	private void tranfer(OrderInfo orderInfo) {
		Log.d(TAG, "tranfer");
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(Def.MESSAGE_DELAY, Def.TIME_OUT);
		}
		
		String deliverySetCode = "";
		if (DatabaseManager.getInstance().getAllDeliveryOrder().size() > 0) {
			deliverySetCode = DatabaseManager.getInstance().getAllDeliveryOrder().get(0).getDeliverySetCode();
		}
		Log.d(TAG, "DeliverySetCode " + deliverySetCode);
		Log.d(TAG, "OrganizationCode " + orderInfo.getOrganizationCode());
		Log.d(TAG, "DeliveryBatchCode " + orderInfo.getDeliveryBatchCode());
		new Send.SendObject(Def.HTTP_TRANSFER, ReceiveDataActivity.this,
				iGetTransferResult,
				UserInfoPref.getPrefUserCode(this), 
				UserInfoPref.getPrefAccessToken(this),
				orderInfo.getOrganizationCode(), 
				orderInfo.getDeliveryBatchCode(),
				deliverySetCode).execute();
		
	}
	
	private void confirmDisplayedContentOverwrite(final NdefMessage msg) {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.new_tag_found))
				.setMessage(getResources().getString(R.string.alert_show_content))
				.setPositiveButton(getResources().getString(R.string.alert_dialog_yes),
					new DialogInterface.OnClickListener() {
						@SuppressLint("NewApi")
						@Override
						public void onClick(DialogInterface dialog, int id) {
							byte[] payloadData = msg.getRecords()[0].getPayload();
							receiveOrder = (OrderInfo) SerializeUtils.deserializeObject(payloadData);							
							tranfer(receiveOrder);
							
						}
					})
				.setNegativeButton(getResources().getString(R.string.alert_dialog_no), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).show();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
			NdefMessage[] msgs = getNdefMessagesFromIntent(intent);
			confirmDisplayedContentOverwrite(msgs[0]);
		} else if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
			Toast.makeText(this, getResources().getString(R.string.alert_no_ndef_data), Toast.LENGTH_LONG).show();
		}
	}
}
