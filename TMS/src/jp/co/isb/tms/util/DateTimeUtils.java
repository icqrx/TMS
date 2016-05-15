package jp.co.isb.tms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
import jp.co.isb.tms.common.Def;


public class DateTimeUtils {
	
	private static final String TAG = DateTimeUtils.class.getSimpleName();

	public static String getSystemTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(Def.FORMAT_DATE_TIME);
		String currentDateandTime = sdf.format(new Date());
		Log.d("Sync", "system time: " + currentDateandTime);
		return currentDateandTime;
	}
	
	public static String getTime(String dateTime) {
		if (dateTime.isEmpty()) {
			return "";
		}
		
		try {
			Date normalDate = null;
			normalDate = new SimpleDateFormat(Def.FORMAT_DATE_TIME).parse(dateTime);
			SimpleDateFormat sdf = new SimpleDateFormat(Def.FORMAT_TIME);
			String currentDateandTime = sdf.format(normalDate);
			return currentDateandTime;
		} catch (ParseException e) {
			Log.e(TAG, "" +  e.toString());
		}
		return "";
		
	}
	
	public static boolean equal(String beforeDate, String afterDate) {
		String array[] = beforeDate.split(" ");
		String time1 = array[0];
		
		String array2[] = afterDate.split(" ");
		String time2 = array2[0];

		
		if (time1.equalsIgnoreCase(time2)) {
			return true;
		}
		
		return false;
	}
}
