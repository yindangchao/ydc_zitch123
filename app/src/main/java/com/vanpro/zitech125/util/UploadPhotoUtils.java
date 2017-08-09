package com.vanpro.zitech125.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import com.vanpro.zitech125.MyApplication;

public class UploadPhotoUtils {
    public static final String TAG = UploadPhotoUtils.class.getSimpleName();

    public final static int REQUEST_CODE_CAMERA = 10;
    public final static int REQUEST_CODE_PHOTO = 10001;
    public final static int REQUEST_CODE_CROP_PHOTO = 10002;

    private static UploadPhotoUtils sInstance = null;

    public static final Uri EXTERNAL_CONTENT_URI = FileUtils.getLocalFileUri(
            MyApplication.getInstance(), "/"+ System.currentTimeMillis()+".jpg");

    private static boolean mCropPhoto = false;
    private static boolean mFullScreenCrop = false;
    private static float mAspectRatio = 1f;

    public UploadPhotoUtils() {
        super();
    }

    public synchronized static UploadPhotoUtils getInstance() {
        if (sInstance == null) {
            sInstance = new UploadPhotoUtils();
        }
        return sInstance;
    }

    public static void getPhotoFromCamera(Context context, int requestCode, boolean cropPhoto) {
        mCropPhoto = cropPhoto;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, EXTERNAL_CONTENT_URI);
        intent.putExtra("return-data", false);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public static void getPhotoFromAlbum(Context context, int requestCode, boolean cropPhoto) {
        mCropPhoto = cropPhoto;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, EXTERNAL_CONTENT_URI);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    public static String handlePhotoResult(Context context, Intent data, int requestCode, int resultCode) {
        Bitmap bitmap = null;
        String path = null;
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode != Activity.RESULT_OK) {
                    return null;
                }

                if (FileUtils.checkSaveLocationExists()) {
                    path = FileUtils.getPath(context, UploadPhotoUtils.EXTERNAL_CONTENT_URI);
//                path = ImageUtils.getBimapPath(context, UploadPhotoUtils.EXTERNAL_CONTENT_URI);
                    if (mCropPhoto) {
                        path = null;
                    } else {
                        bitmap = BitmapUtils.decodeSampledBitmapFromFileName(path, UploadPhotoUtils.getRequireSize(), UploadPhotoUtils.getRequireSize());
                        int degree = BitmapUtils.readPictureDegree(path);
                        if (bitmap != null && degree != ExifInterface.ORIENTATION_NORMAL) {
                            bitmap = BitmapUtils.setRotate(bitmap, degree, false);
                        }
                        FileUtils.writeImage(bitmap, path, 100);
                        bitmap.recycle();
                    }
                } else {
//                    UIHelper.toastMessage(context, context.getString(R.string.pick_photo_no_storage_error));
                }
                break;

            case REQUEST_CODE_PHOTO:
                if (resultCode != Activity.RESULT_OK) {
                    return null;
                }

                path = FileUtils.getPath(context, data.getData());
                if (mCropPhoto) {
                    path = null;
                } else {
                    if (path.startsWith("http")) {
//                        UIHelper.toastMessage(context, context.getString(R.string.pick_photo_handle_webphoto_error));
                        return null;

                    } else {
                        bitmap = BitmapUtils.decodeSampledBitmapFromFileName(path, UploadPhotoUtils.getRequireSize(), UploadPhotoUtils.getRequireSize());

                        if (bitmap == null) {
//                            UIHelper.toastMessage(context, context.getString(R.string.pick_photo_error));
                            return null;
                        }

                        int degree = BitmapUtils.readPictureDegree(path);
                        if (degree != ExifInterface.ORIENTATION_NORMAL) {
                            bitmap = BitmapUtils.setRotate(bitmap, degree, false);
                        }
                        FileUtils.writeImage(bitmap, path, 100);
                        bitmap.recycle();
                    }
                }

                break;

            default:
                break;
        }
        return path;
    }

    public static int getRequireSize() {
        return 0;//TTApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.photo_crop_size);
    }

}
