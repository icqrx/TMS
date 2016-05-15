package jp.co.isb.tms.util;

import java.io.ByteArrayOutputStream;

import jp.co.isb.tms.model.CustomerPicInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.util.Log;


public class ImageUtils {
    private static final int UNCONSTRAINED = -1;
    /**
     * Constant used to indicate the dimension of mini thumbnail.
     * @hide Only used by media framework and media provider internally.
     */
    public static final int TARGET_SIZE_MINI_THUMBNAIL = 120;
    /* Maximum pixels size for created bitmap. */
    private static final int MAX_NUM_PIXELS_THUMBNAIL = 240 * 200;
	private static final String TAG = ImageUtils.class.getSimpleName();
    
    
	public static Bitmap convertStringToBitmap(String picInfoLocal){
		Bitmap bitmap = null;
		byte[] data;
		try{
			data = Base64.decode(picInfoLocal, Base64.DEFAULT);
			picInfoLocal = null;
			
			Options option = new Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, option);
			int w = option.outWidth;
			int h = option.outHeight;
			Log.d("TAG", "original size=" + w + "x" + h);
			option.inSampleSize = computeSampleSize(option, TARGET_SIZE_MINI_THUMBNAIL, MAX_NUM_PIXELS_THUMBNAIL);
			option.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, option);
			if (bitmap != null) {
				Log.d("TAG", "decoded bitmap size->" + bitmap.getWidth() + "x" + bitmap.getHeight());
			}
		} catch(Exception e) {
			Log.e(TAG, "" +  e.toString());
		} finally {
			picInfoLocal = null;
			data = null;
		}
		
		return bitmap;
	}
	
	/*
     * Compute the sample size as a function of minSideLength
     * and maxNumOfPixels.
     * minSideLength is used to specify that minimal width or height of a
     * bitmap.
     * maxNumOfPixels is used to specify the maximal size in pixels that is
     * tolerable in terms of memory usage.
     *
     * The function returns a sample size based on the constraints.
     * Both size and minSideLength can be passed in as IImage.UNCONSTRAINED,
     * which indicates no care of the corresponding constraint.
     * The functions prefers returning a sample size that
     * generates a smaller bitmap, unless minSideLength = IImage.UNCONSTRAINED.
     *
     * Also, the function rounds up the sample size to a power of 2 or multiple
     * of 8 because BitmapFactory only honors sample size this way.
     * For example, BitmapFactory downsamples an image by 2 even though the
     * request is 3. So we round up the sample size to avoid OOM.
     */
    private static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8 ) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == UNCONSTRAINED) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == UNCONSTRAINED) &&
                (minSideLength == UNCONSTRAINED)) {
            return 1;
        } else if (minSideLength == UNCONSTRAINED) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
	
	public static String  convertBitmapToBase64(Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		String strBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
		return strBase64;
	}
}
