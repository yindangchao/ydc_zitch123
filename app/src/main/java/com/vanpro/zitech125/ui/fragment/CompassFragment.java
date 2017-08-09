package com.vanpro.zitech125.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.constants.UnitType;
import com.vanpro.zitech125.event.LocationChangeEvent;
import com.vanpro.zitech125.event.ParkAlertTimeEvent;
import com.vanpro.zitech125.event.PhotoMenuActionEvent;
import com.vanpro.zitech125.event.SensorRorationChangeEvent;
import com.vanpro.zitech125.event.StatusChangeEvent;
import com.vanpro.zitech125.event.UnitChangeEvent;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.manage.NotificationMgr;
import com.vanpro.zitech125.manage.StatusManage;
import com.vanpro.zitech125.ui.activity.TimeAlertActivity;
import com.vanpro.zitech125.ui.dialog.ParkingTimeAlertDialog;
import com.vanpro.zitech125.ui.dialog.PhotoSettingDialog;
import com.vanpro.zitech125.ui.extend.BaseFragment;
import com.vanpro.zitech125.ui.widget.CompassView;
import com.vanpro.zitech125.util.AppDataManager;
import com.vanpro.zitech125.util.BitmapUtils;
import com.vanpro.zitech125.util.Config;
import com.vanpro.zitech125.util.FileUtils;
import com.vanpro.zitech125.util.LogUtil;
import com.vanpro.zitech125.util.StringUtil;
import com.vanpro.zitech125.util.UploadPhotoUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Jinsen on 16/7/1.
 */
public class CompassFragment extends BaseFragment implements View.OnClickListener {

    private ZLocation mCarLocation;
    private ZLocation mCurrentLocation;

    View mCompassView, mPhotoView;
    ImageView mPhotosImage = null;
    ImageView mTakePhotoBtnIv, mPhotoSettingBtn;
    TextView mPoolGpsTv, mTakePhotoDescTv, mTakePhotoTv, mPhotoTipsTv;

    CompassView mCompass;
    private TextView mConnectedState1View, mConnectedState2View;
    private TextView mDistanceFromCarTv;
    private TextView mUnitTv;
    private TextView mLocatorAccuracyTv;

    private TextView mParkLongTv;
    private ImageView mSetAlertIv;
    private ImageView mConnectedIv;
    private TextView mAlertTimeCountdownTv;
    private TextView mDescTv;

    boolean isNavi = false;

    boolean isConnected = false;

    boolean isMeterUnitType = true;
    int mCurStatus = -1;

    ParkingTimeAlertDialog mParkingTimeAlertDialog = null;
    PhotoSettingDialog mPhotoSettingDialog = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_compass_layout;
    }

    @Override
    protected void initView() {
        mCompassView = findViewById(R.id.navi_by_compass_view);
        mPhotoView = findViewById(R.id.navi_by_photo_view);
        mConnectedIv = (ImageView) findViewById(R.id.img_has_connected);
        mPhotosImage = (ImageView) findViewById(R.id.navi_by_photo_pic_imageview);
        mPhotoTipsTv = (TextView) findViewById(R.id.navi_by_photo_pic_tips_textview);
        mTakePhotoBtnIv = (ImageView) findViewById(R.id.navi_by_photo_take_photo_btn);
        mTakePhotoTv = (TextView) findViewById(R.id.navi_by_photo_take_photo_text);
        mPhotoSettingBtn = (ImageView) findViewById(R.id.navi_by_photo_setting_btn);
        mPoolGpsTv = (TextView) findViewById(R.id.navi_by_photo_gps_pool_desc);
        mTakePhotoDescTv = (TextView) findViewById(R.id.navi_by_photo_take_photo_desc_text);

        mCompass = (CompassView) findViewById(R.id.navigation_view);
        mConnectedState1View = (TextView) findViewById(R.id.navi_compass_connected_1);
        mConnectedState2View = (TextView) findViewById(R.id.navi_compass_connected_2);

        mDistanceFromCarTv = (TextView) findViewById(R.id.navi_compass_distance_to_car);
        mUnitTv = (TextView) findViewById(R.id.navi_compass_unit);
        mLocatorAccuracyTv = (TextView) findViewById(R.id.navi_compass_locator_accuracy_textview);

        mParkLongTv = (TextView) findViewById(R.id.navi_compass_parking_time);
        mSetAlertIv = (ImageView) findViewById(R.id.navi_compass_parking_alert_set);
        mAlertTimeCountdownTv = (TextView) findViewById(R.id.navi_compass_parking_alert_countdown);

        mDescTv = (TextView) findViewById(R.id.navi_compass_last_parking_time);
    }

    @Override
    protected void initListener() {
        mSetAlertIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCountDownTime();
            }
        });
        mAlertTimeCountdownTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TimeAlertActivity.class);
                startActivity(intent);
            }
        });

        mTakePhotoBtnIv.setOnClickListener(this);
        mPhotoSettingBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        isMeterUnitType = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.UNIT_TYPE_KEY, true);

        stateView();
    }

    @Override
    public int getTitle() {
        return R.string.nav_compass;
    }


    private void setCountDownTime() {
        mParkingTimeAlertDialog = new ParkingTimeAlertDialog(getContext());
        mParkingTimeAlertDialog.show();
    }

    //正在进行连接
    private void connectingView(){
        mCompass.connected();
        mConnectedState1View.setVisibility(View.VISIBLE);
        mConnectedState2View.setVisibility(View.GONE);
        mConnectedState1View.setText(R.string.ble_connecting_str);
        mConnectedState1View.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);

        mCompassView.setVisibility(View.VISIBLE);

        mPhotoView.setVisibility(View.GONE);
        mDistanceFromCarTv.setVisibility(View.GONE);
        mUnitTv.setVisibility(View.GONE);
        mLocatorAccuracyTv.setVisibility(View.GONE);
        mDescTv.setVisibility(View.GONE);
        mAlertTimeCountdownTv.setVisibility(View.GONE);
        mSetAlertIv.setVisibility(View.GONE);
        mConnectedIv.setVisibility(View.GONE);
        mParkLongTv.setVisibility(View.GONE);
        mParkLongTv.setText(R.string.fyndr_is_connect);
        mParkLongTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    //连接状态中
    private void connectedView() {
        isConnected = true;
        mCompass.connected();
        mConnectedIv.setVisibility(View.VISIBLE);
        mConnectedState1View.setVisibility(View.GONE);
        mConnectedState2View.setVisibility(View.GONE);
        mConnectedState1View.setText(R.string.compass_connected_title);
        mConnectedState2View.setText(R.string.compass_connected_desc);
        mConnectedState1View.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
        mConnectedState2View.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

        mCompassView.setVisibility(View.VISIBLE);

        mPhotoView.setVisibility(View.GONE);
        mDistanceFromCarTv.setVisibility(View.GONE);
        mUnitTv.setVisibility(View.GONE);
        mLocatorAccuracyTv.setVisibility(View.GONE);
        mDescTv.setVisibility(View.GONE);
        mAlertTimeCountdownTv.setVisibility(View.GONE);
        mSetAlertIv.setVisibility(View.GONE);

        mParkLongTv.setVisibility(View.VISIBLE);
        mParkLongTv.setText(R.string.fyndr_is_connect);
        mParkLongTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    private void disconnectView() {
        isConnected = false;
        mConnectedState1View.setVisibility(View.GONE);
        mConnectedState2View.setVisibility(View.GONE);
        mConnectedIv.setVisibility(View.GONE);
        mSetAlertIv.setVisibility(mAlertTimeCountdownTv.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        mParkLongTv.setVisibility(View.VISIBLE);
        mParkLongTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mLocatorAccuracyTv.setVisibility(View.VISIBLE);
        mDistanceFromCarTv.setVisibility(View.VISIBLE);
        mUnitTv.setVisibility(View.VISIBLE);
        mDescTv.setVisibility(View.VISIBLE);
        mUnitTv.setText(isMeterUnitType ? UnitType.UNIT_METER : UnitType.UNIT_FEET);
        mDescTv.setText(R.string.compass_last_park_time);

        mCompass.direction();
        setDistance(0);
    }

    private void showCompassView() {
        mCompassView.setVisibility(View.VISIBLE);
        mPhotoView.setVisibility(View.GONE);
        mSetAlertIv.setVisibility(mAlertTimeCountdownTv.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    private void showPhotoView() {
        mCompassView.setVisibility(View.GONE);
        mPhotoView.setVisibility(View.VISIBLE);

        String imagePath = AppDataManager.getInstance().getString(AppDataManager.KEY.PHOTO_PTAH);
        setPhotoImage(imagePath);
    }

    private void setPhotoImage(String path) {
        if (StringUtil.isEmpty(path)) {
            photoInitState();
        } else {
            showPhotoState(path);
        }
    }

    //已经设置照片的状态
    private void showPhotoState(String path) {
        mTakePhotoBtnIv.setVisibility(View.GONE);
        mTakePhotoTv.setVisibility(View.GONE);
        mTakePhotoDescTv.setVisibility(View.GONE);
        mPoolGpsTv.setVisibility(View.GONE);

        mPhotoSettingBtn.setVisibility(View.VISIBLE);
        mPhotoTipsTv.setVisibility(View.VISIBLE);

        String tips = AppDataManager.getInstance().getString(AppDataManager.KEY.PHOTO_TIPS_PTAH);
        mPhotoTipsTv.setText(tips);

        Bitmap bitmap = BitmapUtils.loadBitmapByFile(path);
        mPhotosImage.setImageBitmap(bitmap);
    }

    //未设置照片的初始状态
    private void photoInitState() {
        mTakePhotoBtnIv.setVisibility(View.VISIBLE);
        mTakePhotoTv.setVisibility(View.VISIBLE);
        mTakePhotoDescTv.setVisibility(View.VISIBLE);
        mPoolGpsTv.setVisibility(View.VISIBLE);

        mPhotoSettingBtn.setVisibility(View.GONE);
        mPhotoTipsTv.setVisibility(View.GONE);

        mPhotosImage.setImageBitmap(null);
    }

    //marker my car position
    public void setCarLocation(ZLocation location) {
        if (location != null) {
            mCarLocation = location;
            setNaviMode(true);
            showCompassInNavi();
        }
    }

    //set view status, is navigation mode?
    public void setNaviMode(boolean isNavi) {
        this.isNavi = isNavi;
    }

    public void setCurLocation(ZLocation location) {
        if (location != null) {
            mCurrentLocation = location;
            if (isNavi) {
                showCompassInNavi();
            } else if(isConnected) {
                showCompassInConnected();
            }
        }
    }

    private void showCompassInConnected() {
        mDistanceFromCarTv.setText(R.string.app_name);
        mUnitTv.setText(R.string.compass_connected_desc);
        mLocatorAccuracyTv.setVisibility(View.GONE);
        mSetAlertIv.setVisibility(View.GONE);
    }

    private void showCompassInNavi() {
        float distance = StatusManage.getInstance().getDistance();
        if (distance >= 0)
            setDistance(distance);
    }

    private void setDistance(float distance) {
        if (distance < 5) {
            mDistanceFromCarTv.setVisibility(View.GONE);
            mUnitTv.setVisibility(View.GONE);
            mLocatorAccuracyTv.setVisibility(View.GONE);

            mConnectedState1View.setVisibility(View.VISIBLE);
            mConnectedState2View.setVisibility(View.VISIBLE);
            mConnectedState1View.setText(R.string.car_is_nearby);

            String unit = isMeterUnitType ? getString(R.string.setting_unit_meters) : getString(R.string.setting_unit_feet);
            int len = isMeterUnitType ? 5 : (int) (5 * UnitType.METER_TO_FEET);
            mConnectedState2View.setText(getString(R.string.compass_within_text,len,unit));
            mConnectedState1View.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            mConnectedState2View.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            mCompass.isNearby();
        } else {
            mConnectedState1View.setVisibility(View.GONE);
            mConnectedState2View.setVisibility(View.GONE);

            mCompass.direction();
            mCompass.setDistance(distance);

            mDistanceFromCarTv.setVisibility(View.VISIBLE);
            mUnitTv.setVisibility(View.VISIBLE);
            mLocatorAccuracyTv.setVisibility(View.VISIBLE);
            String unit = isMeterUnitType ? getString(R.string.setting_unit_meters) : getString(R.string.setting_unit_feet);
            int accuracy = (int) (isMeterUnitType ? distance : distance * UnitType.METER_TO_FEET);
            mDistanceFromCarTv.setText(String.valueOf(accuracy));
            String disStr = getResources().getString(R.string.location_distance_str);
            Integer len = (int) StatusManage.getInstance().getCurLocation().getAccuracy();
            len = isMeterUnitType ? len : (int) (len * UnitType.METER_TO_FEET);
            mUnitTv.setText(unit);
            mLocatorAccuracyTv.setText(String.format(disStr, len, unit));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navi_by_photo_take_photo_btn:
                UploadPhotoUtils.getPhotoFromCamera(getActivity(), UploadPhotoUtils.REQUEST_CODE_CAMERA, false);
                break;

            case R.id.navi_by_photo_setting_btn:
                mPhotoSettingDialog = new PhotoSettingDialog(getActivity());
                mPhotoSettingDialog.show();
                break;
        }
    }

    public void onEventMainThread(PhotoMenuActionEvent event) {
        switch (event.action) {
            case PhotoMenuActionEvent.ACTION_TIPS:
                String tips = AppDataManager.getInstance().getString(AppDataManager.KEY.PHOTO_TIPS_PTAH);
                mPhotoTipsTv.setText(tips);
                break;

            case PhotoMenuActionEvent.ACTION_RETAKE:
                UploadPhotoUtils.getPhotoFromCamera(getActivity(), UploadPhotoUtils.REQUEST_CODE_CAMERA, false);
                break;

            case PhotoMenuActionEvent.ACTION_DELETE:
                setPhotoImage(null);
                break;
        }
    }

    public void onEventMainThread(LocationChangeEvent event) {
        ZLocation location = StatusManage.getInstance().getCurLocation();
        if (location != null) {
            setCurLocation(location);
        }
    }

    private void loctionNavi() {
        ZLocation carLocation = StatusManage.getInstance().getCarLocation();
        if (carLocation != null) {
            showCompassView();
            setCarLocation(carLocation);
            setNaviMode(true);
            return;
        }
        showPhotoView();
    }

    private void picNavi() {
        showPhotoView();
    }

    public void onEventMainThread(SensorRorationChangeEvent event) {
        if (mCompass != null) {
            if (isNavi)
                showCompassInNavi();

            mCompass.updateRotation(StatusManage.getInstance().getRotation());
        }
    }

    public void onEventMainThread(UnitChangeEvent event) {
        isMeterUnitType = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.UNIT_TYPE_KEY, true);

        if (!isConnected && isNavi) {
            showCompassInNavi();
        }
    }

    long mParkTime = 0;
    long MIN = 60000;
    long mAlertCountdownTime = 0;

    private void getLastParkTime() {
        long lastTime = AppDataManager.getInstance().getLong(AppDataManager.KEY.LAST_PARK_TIME_KEY);
        if(lastTime < 1) {
            mSetAlertIv.setVisibility(View.VISIBLE);
            return;
        }
        long nowTime = System.currentTimeMillis();
        long time = nowTime - lastTime;
        if (time < 0 || lastTime < 1) {
        } else {
            mParkTime = time / MIN;
            mParkLongTv.setText(formatTime(mParkTime));
        }
        mHandler.sendEmptyMessageDelayed(0, MIN);

        long endTime = AppDataManager.getInstance().getLong(AppDataManager.KEY.LAST_SET_ALERT_TIME);
        if (endTime > System.currentTimeMillis()) {
            mAlertCountdownTime = (endTime - System.currentTimeMillis()) / 1000;
            mSetAlertIv.setVisibility(View.GONE);
            mHandler.sendEmptyMessageDelayed(1, 1000);
            showAlertTime();
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mHandler.removeMessages(0);
                    mParkTime++;
                    mParkLongTv.setText(formatTime(mParkTime));
                    mHandler.sendEmptyMessageDelayed(0, MIN);
                    break;
                case 1:
                    if (mAlertCountdownTime > 0) {
                        mAlertCountdownTime--;
                        mHandler.sendEmptyMessageDelayed(1, 1000);
                    }
                    showAlertTime();
                    break;
            }
        }
    };

    private void showAlertTime() {
        if (isConnected)
            return;

        if (mAlertCountdownTime < 1) {
            mAlertTimeCountdownTv.setVisibility(View.GONE);
            mSetAlertIv.setVisibility(View.VISIBLE);
            return;
        } else {
            mSetAlertIv.setVisibility(View.GONE);
            mAlertTimeCountdownTv.setVisibility(View.VISIBLE);
            LogUtil.e("CompassFragment","showAlertTime mAlertCountdownTime > 1");
        }

        long hours = mAlertCountdownTime / (60 * 60);
        long mins = mAlertCountdownTime % (60 * 60) / 60;
        long seco = mAlertCountdownTime % 60;
        String time = (hours < 10 ? "0" + hours : hours) + ":" +
                (mins < 10 ? "0" + mins : mins) + ":" +
                (seco < 10 ? "0" + seco : seco);

        mAlertTimeCountdownTv.setText(time);

        if (mAlertCountdownTime > 10 * 60) {
            mAlertTimeCountdownTv.setBackgroundResource(R.drawable.compass_alert_cd_bg_1);
            mAlertTimeCountdownTv.setTextColor(getResources().getColor(R.color.common_orgin_color));
        } else {
            mAlertTimeCountdownTv.setBackgroundResource(R.drawable.compass_alert_cd_bg_red);
            mAlertTimeCountdownTv.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void onEventMainThread(ParkAlertTimeEvent event) {
        mHandler.removeMessages(1);
        mAlertCountdownTime = event.time;
        if (event.time > 0) {
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }
        showAlertTime();
    }

    private String formatTime(long time) {
        String str = null;
        if (time < 1) {
            str = getString(R.string.compass_park_time_less_minute);
        } else if (time < 60) {
            str = getString(R.string.compass_park_time_minute_ago,time);
        } else if (time < 24 * 60) {
            long hours = time / 60;
            long mins = time % 60;
            str = getString(R.string.compass_park_time_hours_ago,hours,mins);
        } else {
            long days = time / 24 / 60;
            long hours = time % (24 * 60) / 60;
            long mins = time % (24 * 60) % 60;

            long t = time * MIN;
            Date date = new Date(System.currentTimeMillis() - t);
            str = StringUtil.dateTimeFormat(date);
        }

        return str;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String path;
            switch (requestCode) {
                case UploadPhotoUtils.REQUEST_CODE_CAMERA:
                    if (FileUtils.checkSaveLocationExists()) {
                        path = FileUtils.getPath(getActivity(), UploadPhotoUtils.EXTERNAL_CONTENT_URI);
                        savePhoto(path);
                        NotificationMgr.getInstance().cancelLocationFailNotify();
                    }
                    break;

                case UploadPhotoUtils.REQUEST_CODE_PHOTO:
                    path = FileUtils.getPath(getActivity(), data.getData());
                    if (path == null || (!path.endsWith(".png") && !path.endsWith(".jpg"))) {
                        showToast("selected photo error");
                        return;
                    }
                    if (FileUtils.checkSaveLocationExists()) {
                        savePhoto(path);
                    } else {
                        showToast("devices no sdcard");
                    }
                    break;
            }
        }

    }

    private void savePhoto(String path) {
        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFileName(path, 480, 800);
        int degree = BitmapUtils.readPictureDegree(path);
        if (degree != ExifInterface.ORIENTATION_NORMAL) {
            bitmap = BitmapUtils.setRotate(bitmap, degree, false);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();
        bitmap.recycle();

        try {
            String filePath = saveToSDCard(b);
            AppDataManager.getInstance().setData(AppDataManager.KEY.PHOTO_PTAH, filePath);
            setPhotoImage(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将拍下来的照片存放在SD卡中
     *
     * @param data
     * @throws IOException
     */
    public String saveToSDCard(byte[] data) throws IOException {
        //剪切为正方形
        Bitmap b = byteToBitmap(data);
        Bitmap bitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight());
        String filename = System.currentTimeMillis() + ".jpg";
        File jpgFile = new File(Config.getAppImagesPath(), filename);
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        outputStream.flush();
        outputStream.close(); // 关闭输出流

        return jpgFile.getPath();
    }

    /**
     * 把图片byte流编程bitmap
     *
     * @param data
     * @return
     */
    private Bitmap byteToBitmap(byte[] data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int i = 0;
        while (true) {
            if ((options.outWidth >> i <= 2000)
                    && (options.outHeight >> i <= 2000)) {
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                break;
            }
            i += 1;
        }
        return b;
    }

    public void onEventMainThread(StatusChangeEvent event) {
        stateView();
    }

    private void stateView() {
        int status = StatusManage.getInstance().getStatus();
        if(mCurStatus == status)
            return;
        mCurStatus = status;
        switch (status) {
            case StatusManage.Status.CONTECTED:
                connectedView();
                mHandler.removeMessages(0);
                mHandler.removeMessages(1);
                setNaviMode(false);
                if(mParkingTimeAlertDialog != null)
                    mParkingTimeAlertDialog.dismissDialog();
                if(mPhotoSettingDialog != null)
                    mPhotoSettingDialog.dismiss();
                break;

            case StatusManage.Status.LOCATION_SUCC:
                disconnectView();
                getLastParkTime();
                loctionNavi();
                setCurLocation(StatusManage.getInstance().getCurLocation());
                mCompass.updateRotation(StatusManage.getInstance().getRotation());
                break;

            case StatusManage.Status.LOCATION_FAIL:
                disconnectView();
                getLastParkTime();
                picNavi();
                break;
            case StatusManage.Status.CONTECTING:
                connectingView();
                mHandler.removeMessages(0);
                mHandler.removeMessages(1);
                setNaviMode(false);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
    }
}
