package jp.co.isb.tms.dialog;

import jp.co.isb.tms.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog{
	
	private Dialog mDialog;
	private Button mBtnNo;
	private Button mBtnYes;
	public boolean isClose = true;
	
	public ConfirmDialog(Context context, String tile, final OnConfirmListener confirmListener){
        
		mDialog = new Dialog(context, R.style.FullHeightDialog);
		 //this is a reference to the style above
		mDialog.setContentView(R.layout.custom_dialog); //I saved the xml file above as yesnomessage.xml
		mDialog.setCancelable(true);
		
		//to set the message
		TextView message =(TextView) mDialog.findViewById(R.id.tvmessagedialogtext);
		message.setText(tile);
		 
		//add some action to the buttons
		mBtnYes = (Button) mDialog.findViewById(R.id.bmessageDialogYes);
		mBtnYes.setOnClickListener(new OnClickListener() {
             
            public void onClick(View v) {
            	confirmListener.onYes();
            	if (isClose) {
            		mDialog.dismiss();
            	}
            }
        });
         
        mBtnNo = (Button) mDialog.findViewById(R.id.bmessageDialogNo);
        mBtnNo.setOnClickListener(new OnClickListener() {
             
            public void onClick(View v) {
            	confirmListener.onNo();
                mDialog.dismiss();
            }
        });
        
		            
	}
	 
	 public void show(){
		  mDialog.show();
	 }
	 public void dismiss() {
		 mDialog.dismiss();
	 }
}
