package com.vanpro.zitech125.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 */
public class BitmapUtils {

    /**
     * Note: this method may return null
     *
     * @param fileName
     * @return a bitmap decoded from the specified file
     */
    public synchronized static Bitmap loadBitmapByFile(String fileName) {
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeFile(fileName);//BitmapFactory.decodeFile(fileName, options);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Note: this method may return null
     *
     * @param fileName
     * @param reqWidth
     * @param reqHeight
     * @return a bitmap decoded from the specified file
     */
    public synchronized static Bitmap decodeSampledBitmapFromFileName(String fileName, int reqWidth, int reqHeight) {
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
//            Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(fileName, options);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 读取照片exif信息中的旋转角度<br>http://www.eoeandroid.com/thread-196978-1-1.html
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public synchronized static Bitmap setRotate(Bitmap bitmap, int degree, boolean recycleOrig) {
        try {
            Matrix matrix = new Matrix();
            //设置图像的旋转角度
            matrix.setRotate(degree);
            //旋转图像，并生成新的Bitmap对像
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            if (recycleOrig && bitmap != null && !bitmap.isRecycled())
                bitmap.recycle();

            return newBitmap;

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
