package jp.co.isb.tms.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class StreamUtils {
	private static final String TAG = StreamUtils.class.getSimpleName();

	public static String converStreamToString(InputStream aInputStream) {
		StringBuilder mBuilder = new StringBuilder();
		String line = "";
		try {
			BufferedReader mReader = new BufferedReader(new InputStreamReader(aInputStream));
			while((line = mReader.readLine()) != null) {
				mBuilder.append(line);
			}
		} catch (Exception e) {
			Log.e(TAG, "Error caused when convert stream to string.");
		}
		return mBuilder.toString().replace("\\", "");
	}
}
