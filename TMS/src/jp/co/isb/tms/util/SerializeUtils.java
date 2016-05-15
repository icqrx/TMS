package jp.co.isb.tms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import android.util.Log;

public class SerializeUtils {
	private static final String TAG = SerializeUtils.class.getSimpleName();

	public static byte[] serializeObject(Object o) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			ObjectOutput out = new ObjectOutputStream(baos);
			out.writeObject(o);
			out.close();

			// Get the bytes of the serialized object
			byte[] buffer = baos.toByteArray();

			return buffer;
		} catch (IOException ioe) {

			return null;
		}
	}

	public static Object deserializeObject(byte[] b) {
		try {
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
			Object object = in.readObject();
			in.close();

			return object;
		} catch (ClassNotFoundException cnfe) {
			Log.e(TAG, "class not found error", cnfe);
			return null;
		} catch (IOException ioe) {
			Log.e(TAG, "io error", ioe);
			return null;
		}
	}
}
