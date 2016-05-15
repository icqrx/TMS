package jp.co.isb.tms.ui;

import jp.co.isb.tms.R;
import jp.co.isb.tms.common.ConfirmEnableNetworkDialog;
import jp.co.isb.tms.common.ConfirmUnknownErrorDialog;
import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.IGetAccessToken;
import jp.co.isb.tms.common.INotifyChange;
import jp.co.isb.tms.common.StoragePref;
import jp.co.isb.tms.common.UserInfoPref;
import jp.co.isb.tms.db.DatabaseManager;
import jp.co.isb.tms.server.Send;
import jp.co.isb.tms.util.HttpUtils;
import jp.co.isb.tms.util.NetworkUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements INotifyChange{
	private static final String TAG = LoginActivity.class.getSimpleName();
	private Button mLoginButton;
	private EditText mStaffCodeEditText;
	private EditText mPasswordEditText;
	private TextView mErrorMsgTextView;
	private Handler mHandler;
	private int mStatus = Def.SERVER_ERROR;
	private String mAccessToken;
	String mUserCode;
	String mPassword;
	private ProgressDialog progressDlg;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_login);
		DatabaseManager.init(this);
		boolean isLogout = false;
		if (getIntent() != null && getIntent().getExtras() != null){
			isLogout = getIntent().getExtras().getBoolean(Def.IS_LOG_OUT);
		}
		
		if (!isLogout && UserInfoPref.isAutoLogin(this)) {
			mAccessToken = UserInfoPref.getPrefAccessToken(this);
			finish();
			Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
			intent.putExtra(Def.USER_ID, mAccessToken);
			startActivity(intent);
			return;
		}
		
		mStaffCodeEditText = (EditText)findViewById(R.id.staff_code_editText);
		mPasswordEditText = (EditText)findViewById(R.id.password_editText);
		mErrorMsgTextView = (TextView) findViewById(R.id.tv_ErrorMessage);
		
		
		//new SqliteHelper(this);
		mLoginButton = (Button) findViewById(R.id.btn_login);
		mLoginButton.setEnabled(false);
		mStaffCodeEditText.addTextChangedListener(mTextWatcher);
		mPasswordEditText.addTextChangedListener(mTextWatcher);
		mErrorMsgTextView.setText("");
		
		mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isLegalInput()){
					mErrorMsgTextView.setText(getResources().getString(R.string.alert_invalid_input));
				} else {
					if (!NetworkUtils.checkNetworkAvailable(getApplicationContext())) {
						new ConfirmEnableNetworkDialog(LoginActivity.this).show();
					} else {
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
						login();
					}
				}
			}
		});
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case Def.MESSAGE_DELAY: {
						showProgressDialg(false);
						if (mStatus == Def.SERVER_ERROR) {
							new ConfirmEnableNetworkDialog(LoginActivity.this).show();
						}
						enableComponents(true);
						break;
					}
					default: 
						break;
				}
				super.handleMessage(msg);
			}
		};	
	}
	
	private void login() {
		mErrorMsgTextView.setText("");
		
		mUserCode = mStaffCodeEditText.getText().toString().trim();
		//check user
		if(!mUserCode.equals(UserInfoPref.getPrefUserCode(this))){
			DatabaseManager.getInstance().clearAllTable();
		}
		try {
			mPassword = HttpUtils.hmacSHA256(Def.PASSWORD_SECRET, mPasswordEditText.getText().toString());
		} catch (Exception e) {
			Log.e(TAG, "" + e.toString());
		}
		
		UserInfoPref.setPrefUserCode(this, mUserCode);
		UserInfoPref.setPrefPassword(this, mPassword);
		Log.d(TAG, "mUserCode=" + mUserCode + "mPassword = "  + mPassword);

		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(Def.MESSAGE_DELAY, Def.TIME_OUT);
		}
		
		new Send.SendObject(Def.HTTP_LOGIN, LoginActivity.this, iAccessToken, mUserCode, mPassword).execute();
	}
	
	@SuppressLint("DefaultLocale")
	private boolean isLegalInput() {
		if (!mStaffCodeEditText.getText().toString().toLowerCase().matches(Def.staff_code_regex) 
				|| mPasswordEditText.getText().toString().matches("")){
			return false;
		}
		return true;
	}
	
	private IGetAccessToken iAccessToken = new IGetAccessToken() {		

		@Override
		public void sendAccessToken(String accessToken) {
			mAccessToken = accessToken;
		};
	};
	
	private void setStatusForLoginButton(){
		if (mStaffCodeEditText.getText().toString().matches("") || mPasswordEditText.getText().toString().matches("")) {
			mLoginButton.setEnabled(false);
		} else {
			mLoginButton.setEnabled(true);
		}
	}
	
	private void enableComponents(boolean value) {
		mStaffCodeEditText.setEnabled(value);
		mPasswordEditText.setEnabled(value);
		mLoginButton.setEnabled(value);
		mErrorMsgTextView.setEnabled(value);
	}
	
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			setStatusForLoginButton();
			if (mPasswordEditText.getText().toString().length() > Def.PASSWORD_MAX_LENGTH){
				mErrorMsgTextView.setText(getResources().getString(R.string.alert_invalid_input));
			} else {
				mErrorMsgTextView.setText("");
			}
			if (mStaffCodeEditText.getText().toString().length() > Def.STAFF_CODE_MAX_LENGTH){
				mErrorMsgTextView.setText(getResources().getString(R.string.alert_invalid_input));
			} else {
				mErrorMsgTextView.setText("");
			}
		}					
	};
	
	private void removeHandler() {
		if (mHandler != null) {
			mHandler.removeMessages(Def.MESSAGE_DELAY);
			mHandler = null;
		}
		showProgressDialg(false);
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	removeHandler();
        	mStaffCodeEditText.setText("");
        	mPasswordEditText.setText("");
        	mErrorMsgTextView.setText("");
        	moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	private void postExecute(int status) {
		if (status != Def.SERVER_ERROR) {
			showProgressDialg(false);
			if (mHandler != null) {
				mHandler.removeMessages(Def.MESSAGE_DELAY);
			}
			enableComponents(true);	
		}
		switch (status) {
			case Def.OK: 		
				Log.d(TAG, "Login successful, accesstoken: " + mAccessToken);
				if (mAccessToken != null && !mAccessToken.equals("")) {
					UserInfoPref.setPrefAutoLogin(this, true);
					UserInfoPref.setPrefAccessToken(this, mAccessToken);
					StoragePref.saveLoginData(this);
					finish();
					Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
					intent.putExtra(Def.USER_ID, mAccessToken);
					startActivity(intent);
				} else {
					Log.e(TAG, "InputStream is null");
				}
				break;
				
			case Def.FAIL: 
				new ConfirmEnableNetworkDialog(this).show();
				break;
				
			case Def.PASSWORD_INVALID: 
				Log.d(TAG, "password invalid");
				mErrorMsgTextView.setText(getResources().getString(R.string.alert_invalid_input));
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
			showProgressDialg(true);
			enableComponents(false);
			break;
		default: 
			postExecute(status);
			break;
		}
	}
	
	private void showProgressDialg(boolean show) {
		if (progressDlg != null) {
			progressDlg.dismiss();
			progressDlg = null;
		}
		if (progressDlg == null) {
			progressDlg = new ProgressDialog(this);
			progressDlg.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
			progressDlg.setMessage(getResources().getString(R.string.alert_loging));
		}
		if (show) {
			progressDlg.show();
		} else {
			progressDlg.dismiss();
			progressDlg = null;
		}
	}
}
