package jp.co.isb.tms.ui;

import java.nio.charset.Charset;

import jp.co.isb.tms.R;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.TMSConstrans;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.model.OrderInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SendDataActivity extends Activity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback{
	private static final String TAG = SendDataActivity.class.getSimpleName();
	protected static final int DATA_SENT = 1;
	private NfcAdapter mNfcAdapter;	
	private Dialog dialog;
	private Button no, yes;
	private OrderInfo mOrderItemInfo; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_send_data);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			Toast.makeText(this, getResources().getString(R.string.nfc_is_not_available), Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		mOrderItemInfo = (OrderInfo) getIntent().getParcelableExtra(Def.TAG_ORDER_ITEM);
		mNfcAdapter.setNdefPushMessageCallback(this, this);
		mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
	}
	
	@Override
	public NdefMessage createNdefMessage(NfcEvent arg0) {		
		String mimeType = Def.NFC_DATA_TYPE;
		byte[] mimeBytes = mimeType.getBytes(Charset.forName(Def.UFT_8));		
		mOrderItemInfo.setOrganizationCode(TMSConstrans.mSoshiki_cd);
		byte[] dataBytes = mOrderItemInfo.serializeObject();
		Log.d(TAG, "getOrganizationCode = " + mOrderItemInfo.getOrganizationCode());
		byte[] id = new byte[0];
		NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, id, dataBytes);
		NdefMessage message = new NdefMessage(new NdefRecord[] { record });
		return message;
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		mHandler.sendEmptyMessage(DATA_SENT);
		DatabaseManager.getInstance().deleteOrderItem(mOrderItemInfo);
	}
	
	private void confirmDataSentDialog(){
		dialog = new Dialog(this, R.style.FullHeightDialog);
		 //this is a reference to the style above
		dialog.setContentView(R.layout.custom_dialog); 
		dialog.setCancelable(true);
		
		//to set the message
		TextView message =(TextView) dialog.findViewById(R.id.tvmessagedialogtext);
		message.setText(getResources().getString(R.string.text_data_sent));
		 
		//add some action to the buttons
        yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
        yes.setText(getResources().getString(R.string.alert_dialog_ok));
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
	
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	        case DATA_SENT:
	        	confirmDataSentDialog();
	            break;
	        }
	    }
	};
}
