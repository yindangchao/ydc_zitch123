package com.vanpro.zitech125.location;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.vanpro.zitech125.present.LocationMgr;
import com.vanpro.zitech125.util.LogUtil;
import com.vanpro.zitech125.util.StringUtil;

/**
 * Created by Jinsen on 16/12/26.
 */

public class GoogleLocation extends ZBaseLocation implements LocationListener {

    LocationManager mLocationManager;

    String PROVIDER;

    public GoogleLocation(Context context) {
        super(context);

        mLocationManager = (LocationManager) mContext.
                getSystemService(Context.LOCATION_SERVICE);
        //绑定监听状态
        if (!LocationMgr.checkPermission(mContext))
            mLocationManager.addGpsStatusListener(listener);

        PROVIDER = createProvider();//"network";//

        LogUtil.e("xxxxxx", "provider " + PROVIDER);

        if (StringUtil.isEmpty(PROVIDER))
            PROVIDER = LocationManager.NETWORK_PROVIDER;

        mLocationManager.requestLocationUpdates(PROVIDER, 10000, 10, this);

        getLocation();
    }

    public ZLocation getLocation() {
        if (LocationMgr.checkPermission(mContext)) {
            return null;
        }

        Location location = mLocationManager.getLastKnownLocation(PROVIDER);
        if(location != null)
            return new ZLocation(location);
        else
            return null;
    }

    @Override
    public void onDestroy() {
        mLocationManager.removeGpsStatusListener(listener);
        //绑定监听状态
        if (!LocationMgr.checkPermission(mContext))
            mLocationManager.removeUpdates(this);

        mLocationManager = null;
    }

    /**
     * 位置信息变化时触发
     */
    public void onLocationChanged(Location location) {
        LogUtil.e(TAG,"onLocationChanged :: " );
        if(location == null)
            return;
        mZLListener.onLocationChange(new ZLocation(location));
    }


    /**
     * GPS状态变化时触发
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            //GPS状态为可见时
            case LocationProvider.AVAILABLE:
                LogUtil.e(TAG, "当前GPS状态为可见状态");
                break;
            //GPS状态为服务区外时
            case LocationProvider.OUT_OF_SERVICE:
                LogUtil.e(TAG, "当前GPS状态为服务区外状态");
                break;
            //GPS状态为暂停服务时
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                LogUtil.e(TAG, "当前GPS状态为暂停服务状态");
                break;
        }
    }

    /**
     * GPS开启时触发
     */
    public void onProviderEnabled(String provider) {
        if (LocationMgr.checkPermission(mContext)) {
            return;
        }

        ZLocation location = getLocation();
        if (location != null) {
            mZLListener.onLocationChange(location);
        }
    }

    /**
     * GPS禁用时触发
     */
    public void onProviderDisabled(String provider) {
    }


    private String createProvider() {
        //查找服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); //定位精度: 最高
        criteria.setAltitudeRequired(false); //海拔信息：不需要
        criteria.setBearingRequired(true); //方位信息: 需要
        criteria.setCostAllowed(false);  //是否允许付费
        criteria.setSpeedRequired(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); //耗电量: 低功耗

        return mLocationManager.getBestProvider(criteria, true); //获取GPS信息
    }

    //状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                //第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    LogUtil.e(TAG, "第一次定位");
                    break;
                //卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    LogUtil.e(TAG, "卫星状态改变");
                    break;
                //定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    LogUtil.e(TAG, "定位启动");
                    break;
                //定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
//                    LogUtil.e(TAG, "定位结束");
                    break;
            }
        }

        ;
    };

}
