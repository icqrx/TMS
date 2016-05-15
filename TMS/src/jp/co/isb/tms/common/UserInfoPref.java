package jp.co.isb.tms.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserInfoPref {
	public static String getPrefAccessToken(Context context) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    String aToken = app_preferences.getString(Def.PREF_ACCESS_TOKEN, "");
	    
	    return aToken;
	}
	
	public static void setPrefAccessToken(Context context, String aToken) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = app_preferences.edit();
	    editor.putString(Def.PREF_ACCESS_TOKEN, aToken);
	    
	    editor.commit(); 
	}
	
	public static void setPrefUserCode(Context context, String mUserCode) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = app_preferences.edit();
	    editor.putString(Def.PREF_USER_CODE, mUserCode);
	    
	    editor.commit(); 
	}
	public static void setInputStream(Context context, String input) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = app_preferences.edit();
	    editor.putString(Def.INPUT_STREAM, input);
	    
	    editor.commit(); 
	}
	public static String getInputStream(Context context) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    String input = app_preferences.getString(Def.INPUT_STREAM, "");
	    
	    return input;
	}
	
	
	public static String getPrefUserCode(Context context) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    String userCode = app_preferences.getString(Def.PREF_USER_CODE, "");
	    
	    return userCode;
	}
	
	public static void setPrefPassword(Context context, String mPassword) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = app_preferences.edit();
	    editor.putString(Def.PREF_PASSWORD, mPassword);
	    
	    editor.commit(); 
	}
	
	public static String getPrefPassword(Context context) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    String password = app_preferences.getString(Def.PREF_PASSWORD, "");
	    
	    return password;
	}
	
	public static void setPrefAutoLogin(Context context, boolean flag) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = app_preferences.edit();
	    editor.putBoolean(Def.PREF_AUTO_LOGIN, flag);
	    editor.commit(); 
	}
	
	public static boolean isAutoLogin(Context context) {
	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    boolean autoLogin = app_preferences.getBoolean(Def.PREF_AUTO_LOGIN, false);
	    
	    return autoLogin;
	}
}
