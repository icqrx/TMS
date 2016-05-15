package jp.co.isb.tms.barcode;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import jp.co.isb.tms.common.Def;
import jp.co.isb.tms.common.IGetBarCode;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BarcodeReaderThread extends Thread {
	private final BluetoothServerSocket mmServerSocket;
	private String TAG = BarcodeReaderThread.class.getSimpleName();
	private IGetBarCode iGetBarCode;
	public boolean isStop = false;
	
	public BarcodeReaderThread(UUID UUID_BLUETOOTH, IGetBarCode aGetBarCode) {
		iGetBarCode = aGetBarCode;
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	    if (mBluetoothAdapter == null) {
	        Log.d(TAG , "Could not get bluetooth adapter");
	    }
		// Use a temporary object that is later assigned to mmServerSocket,
		// because mmServerSocket is final
		BluetoothServerSocket tmp = null;
		try {
			// MY_UUID is the app's UUID string, also used by the client code
			tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("BarcodeScannerForSGST", UUID_BLUETOOTH);
			/*
			 * The UUID is also included in the SDP entry and will be the basis
			 * for the connection agreement with the client device. That is,
			 * when the client attempts to connect with this device, it will
			 * carry a UUID that uniquely identifies the service with which it
			 * wants to connect. These UUIDs must match in order for the
			 * connection to be accepted (in the next step)
			 */
		} catch (IOException e) {
		}
		mmServerSocket = tmp;
	}

	public void run() {
		BluetoothSocket socket = null;
		// Keep listening until exception occurs or a socket is returned
		if(mmServerSocket == null){
			return ;
		}
		while (true) {
			if (isStop) {
				try {
					socket.close();
					return;
				} catch (IOException e) {
					Log.d(TAG, "" + e.toString());
				}
				
			}
			try {
				socket = mmServerSocket.accept();
				try {
					// If a connection was accepted
					if (socket != null) {
						// Do work to manage the connection (in a separate
						// thread)
						InputStream mmInStream = null;

						// Get the input and output streams, using temp objects
						// because
						// member streams are final
						mmInStream = socket.getInputStream();
						Log.d(TAG, "mmInStream = " + mmInStream);
						byte[] buffer = new byte[1024]; // buffer store for the
														// stream
						int bytes; // bytes returned from read()

						// Keep listening to the InputStream until an exception
						// occurs
						// Read from the InputStream
						bytes = mmInStream.read(buffer);
						if (bytes > 0) {
							// Send the obtained bytes to the UI activity
							String readMessage = new String(buffer, 0, bytes);
							// doMainUIOp(BARCODE_READ, readMessage);
							if (readMessage.length() > 0) {
								Log.d(TAG, "readMessage = " + readMessage);
								iGetBarCode.sendBarCode(getTrackinfgID(readMessage));
							}
						}
					}
				} catch (Exception ex) {
				}
			} catch (IOException e) {
				break;
			}
		}
	}

	/**
	 * Will cancel the listening socket, and cause the thread to finish
	 */
	public void cancel() {
		try {
			mmServerSocket.close();
		} catch (IOException e) {
		}
	}
	
	private String getTrackinfgID(String data){
		int beginPos = -1;
		int endPos = -1;
		for (int i = 0 ; i < data.length(); i++) {
			if (data.charAt(i) == Def.BARCODE_START_CHAR) {
				beginPos = i;
			} else if (data.charAt(i) == Def.BARCODE_LAST_CHAR) {
				endPos = i;
			}
		}
		if ((beginPos == -1 && endPos == -1) || (endPos < 1)){
			return "";
		}
		return data.substring(beginPos + 1 , endPos - 1);
	}
}
