package com.vanpro.zitech125.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.event.LocationEvent;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.present.LocationMgr;
import com.vanpro.zitech125.ui.dialog.ParkingTimeAlertDialog;
import com.vanpro.zitech125.ui.extend.BaseFragment;
import com.vanpro.zitech125.ui.widget.SlipLayout;
import com.vanpro.zitech125.util.AppDataManager;

/**
 * navigation found car
 *
 * Created by Jinsen on 16/4/10.
 */
public class NavigationFragment extends BaseFragment {

    String TAG = NavigationFragment.this.getClass().getSimpleName().toString();

    LocationMgr mLocationMgr;

    Location mCarLocation = null;
    Location mCurLocation = null;


    SlipLayout mSlipLayout = null;

    long MIN = 60000;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_navigation_layout;
    }

    @Override
    protected void initView() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);

        mSlipLayout = (SlipLayout) findViewById(R.id.navigation_view);


        sm=(SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        aSensor=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor=sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener(myListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void initListener() {
        mSlipLayout.setAlertOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCountDownTime();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSlipLayout.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        mSlipLayout.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSlipLayout.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mSlipLayout.onLowMemory();
    }

    @Override
    protected void initData() {
//        mNavigationView.setNavigation(true);
        String locStr = AppDataManager.getInstance().getString(AppDataManager.KEY.LOCATION_KEY);
        if(locStr != null){
            String [] loc = locStr.split("//");
            if(loc != null && loc.length == 2){
                mCarLocation = new Location(android.location.LocationManager.GPS_PROVIDER);
                mCarLocation.setLongitude(Double.valueOf(loc[0]));
                mCarLocation.setLatitude(Double.valueOf(loc[1]));
                mSlipLayout.setCarLocation(mCarLocation);
                mSlipLayout.setNaviMode(true);
            }
        }

//        mCarLocation = new Location(android.location.LocationManager.GPS_PROVIDER);
//        mCarLocation.setLatitude(0.0);
//        mCarLocation.setLongitude(23);
//
//        mSlipLayout.setCarLocation(mCarLocation);
//        mSlipLayout.setNaviMode(true);

        mLocationMgr = LocationMgr.getInstance();
        mSlipLayout.setCurLocation(mLocationMgr.getLocation().getLocation());

//        AppDataManager.getInstance().setData(AppDataManager.KEY.LAST_PARK_TIME_KEY,System.currentTimeMillis()-100000);
//
//        Location location = new Location(android.location.LocationManager.GPS_PROVIDER);
//        location.setLatitude(0.1);
//        location.setLongitude(23.1);
//        location.setAccuracy(10);
//        mSlipLayout.setCurLocation(location);
//
//        mSlipLayout.setParkingLongTime(24 + " mins ago");
//
//        mParkTime = 24;
//        mHandler.sendEmptyMessage(0);

        getLastParkTime();
    }

    long mParkTime = 0;

    private void getLastParkTime(){
        long lastTime = AppDataManager.getInstance().getLong(AppDataManager.KEY.LAST_PARK_TIME_KEY);
        long nowTime = System.currentTimeMillis();
        long time = nowTime - lastTime;
        if(time < 0){

        }else{
            mParkTime = time / MIN;
            mSlipLayout.setParkingLongTime(formatTime(mParkTime));
            mHandler.sendEmptyMessageDelayed(0,MIN);
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mHandler.removeMessages(0);
            mParkTime ++;
            mSlipLayout.setParkingLongTime(formatTime(mParkTime));
            mHandler.sendEmptyMessageDelayed(0,MIN);
        }
    };

    private String formatTime(long time) {
        String str = null;
        if (time < 1) {
            str = "Less than a minute ago";
        } else if (time < 60) {
            str = time + "mins ago";
        } else if (time < 24 * 60) {

            long hours = time / 60;
            long mins = time % 60;
            str = hours + "hours " + mins + "mins ago";
        } else {
            long days = time / 24 / 60;
            long hours = time % (24 * 60) / 60;
            long mins = time % (24 * 60) % 60;
            str = days + "days " + hours + "hours " + mins + "mins ago";
        }

        return str;
    }


    public void onEventMainThread(LocationEvent event){
        ZLocation location =  event.location;
        if(location != null){
            mSlipLayout.setCurLocation(location.getLocation());
        }
    }

    private void setCountDownTime() {
        ParkingTimeAlertDialog dialog = new ParkingTimeAlertDialog(getContext());
        dialog.show();
    }

    private void locationFailed(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_view, new LocationFailFragment());
        ft.commit();
    }

    private SensorManager sm=null;
    private Sensor aSensor=null;
    private Sensor mSensor=null;

    float[] accelerometerValues=new float[3];
    float[] magneticFieldValues=new float[3];
    float[] values=new float[3];
    float[] rotate=new float[9];


    final SensorEventListener myListener=new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                accelerometerValues=event.values;
            }
            if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                magneticFieldValues=event.values;
            }

            SensorManager.getRotationMatrix(rotate, null, accelerometerValues, magneticFieldValues);
            SensorManager.getOrientation(rotate, values);
            //经过SensorManager.getOrientation(rotate, values);得到的values值为弧度
            //转换为角度
            values[0]=(float)Math.toDegrees(values[0]);

            mSlipLayout.updateDirection(values[0]);

        }};
}
