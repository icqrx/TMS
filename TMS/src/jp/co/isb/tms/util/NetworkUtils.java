package jp.co.isb.tms.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Networking utility
 * 
 * @author pddthinh
 * 
 */
public class NetworkUtils {
	private static final String TAG = "NetworkUtil";

	/**
	 * Get device WiFi IP address
	 * 
	 * @param aContext
	 * @return
	 */
	public static String getWifiIpAddress(Context aContext) {
		return getWifiIpAddressInfo(aContext, 1);
	}
	/**
	 * get device Gateway Ip address.
	 * @param aContext
	 * @return
	 */
	public static String getWiFiGatewayIpAddress(Context aContext) {
		return getWifiIpAddressInfo(aContext, 3);
	}
	
	/**
	 * Get the WiFi IP address info
	 * 
	 * @param aContext
	 * @param aiField
	 * 		1: Device address
	 * 		3: Device gateway address 
	 * @return
	 */
	private static String getWifiIpAddressInfo(Context aContext, int aiField) {
		String lstrAdd = null;

		do {
			if (aContext == null) {
				Log.v(TAG, "Context is null");
				break;
			}

			WifiManager lWifiManager = (WifiManager) aContext
					.getSystemService(Context.WIFI_SERVICE);

			/* Get DHCP Information */
			DhcpInfo lDhcp = lWifiManager.getDhcpInfo();
			String[] lData = lDhcp.toString().split(" ");

			if (lData.length > aiField) {
				lstrAdd = lData[aiField];
			}
		} while (false);

		return lstrAdd;
	}

	/**
	 * Get all local IP addresses
	 * 
	 * @return
	 */
	public static ArrayList<InetAddress> getAllLocalIpAddress() {
		ArrayList<InetAddress> llstAddress = new ArrayList<InetAddress>();

	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();

	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                
	                Log.d(TAG, "IP" + inetAddress);
	                Log.d(TAG, "IP_HOST" + inetAddress.getHostAddress());

	                if (!inetAddress.isLoopbackAddress()) {
	                	Log.d(TAG, "IP address: " + inetAddress.getHostAddress().toString());
	                	String ipv4 = inetAddress.getHostAddress();
	                	if (InetAddressUtils.isIPv4Address(ipv4)) {
	                		Log.d(TAG, "IP4 address: " + inetAddress.getHostAddress().toString());
	                		llstAddress.add(inetAddress);
	                	}
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e(TAG, ex.toString());
	        llstAddress = null;
	    }

	    return llstAddress;
	}

	/**
	 * Get the first local IP address (not loop-back and local link address) 
	 * 
	 * @return
	 */
	public static InetAddress getLocalIPAddress() {
		InetAddress lLocalAddr = null;
		try {
			ArrayList<InetAddress> llstLocalAddr = getAllLocalIpAddress();

			for (InetAddress ladd : llstLocalAddr) {
				if (!(ladd.isLinkLocalAddress() || ladd.isLoopbackAddress())) {
					lLocalAddr = ladd;
					break;
				}
			}
			if(lLocalAddr != null) {
				Log.d(TAG, "getLocalIPAddress: " + lLocalAddr.toString());
			}
		} catch (Exception e) {
			Log.e(TAG, "" + e.toString());
		}
		
		return lLocalAddr;
	}

	/**
	 * Get local IP address in String type
	 * 
	 * @return
	 */
	public static String getLocalIPString() {
		String lstrAddress = null;
		InetAddress lAdd = getLocalIPAddress();

		do {
			if (lAdd == null)
				break;

			String lstrTmp = lAdd.toString();
			String[] lstrAddr = lstrTmp.split("/");
			if (lstrAddr.length >= 2)
				lstrAddress = lstrAddr[1];
		} while (false);

		return lstrAddress;
	}

	/**
	 * Get Sub-net broadcast address
	 * 
	 * @param aContext
	 * @return
	 */
	public static InetAddress getBroadcastAddress(Context aContext) {
		InetAddress lAddress = null;
		
		do {
			if (aContext == null) {
				Log.v(TAG, "Context is null");
				break;
			}

			try {
				WifiManager wifi = (WifiManager) aContext.getSystemService(Context.WIFI_SERVICE);
				DhcpInfo dhcp = wifi.getDhcpInfo();
				int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;

				byte[] quads = new byte[4];
				for (int k = 0; k < 4; k++)
					quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
				lAddress = InetAddress.getByAddress(quads);
			} catch (Exception e) {
				Log.e(TAG, "" + e.toString());
			}
		} while (false);

		return lAddress;
	}
	/**
	 * Check network connection (Wifi or Mobile) is ok or not
	 * @return true: ok. false: false.
	 */
	public static boolean checkNetworkAvailable(Context context) {
		if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }

        boolean isConnectied = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            isConnectied = true;
        }
        return isConnectied;
	}
	
	@SuppressLint("InlinedApi")
	public static void startNfcSettingsActivity(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
        	context.startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
        } else {
            context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }
	
	public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
}
