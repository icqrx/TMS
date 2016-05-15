package jp.co.isb.tms.common;

import jp.co.isb.tms.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDeviceNoSupportNfcDialog {
	private Context mContext;
	private Dialog dialog;
	private Button no, yes;
	
	public ConfirmDeviceNoSupportNfcDialog(Context context){
		mContext = context;
		dialog = new Dialog(mContext, R.style.FullHeightDialog);
		 //this is a reference to the style above
		dialog.setContentView(R.layout.custom_dialog); //I saved the xml file above as yesnomessage.xml
		dialog.setCancelable(true);
		
		//to set the message
		TextView message =(TextView) dialog.findViewById(R.id.tvmessagedialogtext);
		message.setText(mContext.getResources().getString(R.string.text_device_no_support_nfc));
		 
		//add some action to the buttons
        yes = (Button) dialog.findViewById(R.id.bmessageDialogYes);
        yes.setText(mContext.getResources().getString(R.string.alert_dialog_yes));
        yes.setOnClickListener(new OnClickListener() {
             
            public void onClick(View v) {
                 dialog.dismiss();
            }
        });
         
        no = (Button) dialog.findViewById(R.id.bmessageDialogNo);
        no.setVisibility(View.GONE);
	}
	 
	 public void show(){
		  dialog.show();
	 }
}
