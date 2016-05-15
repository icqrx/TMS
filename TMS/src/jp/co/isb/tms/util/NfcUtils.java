package jp.co.isb.tms.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;

public class NfcUtils {
	@SuppressLint("NewApi")
	public static boolean isNfcEnabled(Context context) {
		NfcAdapter mNfcAdapter = null;
		if (hasNfcFeature(context)) {
			mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
			if (mNfcAdapter == null){
				return false;
			}
			return mNfcAdapter.isEnabled();
		}
		return false;
	}
	
	@SuppressLint("InlinedApi")
	public static boolean hasNfcFeature(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }
        return pm.hasSystemFeature(PackageManager.FEATURE_NFC);
    }
}
