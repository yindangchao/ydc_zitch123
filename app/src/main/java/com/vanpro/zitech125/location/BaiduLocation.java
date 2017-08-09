package com.vanpro.zitech125.location;

import android.content.Context;
import android.location.Location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.vanpro.zitech125.util.Gps;
import com.vanpro.zitech125.util.PositionUtil;
import com.vanpro.zitech125.util.StringUtil;

import java.util.Date;

/**
 * Created by Jinsen on 16/12/26.
 */
public class BaiduLocation extends ZBaseLocation {

    public BaiduLocation(Context context) {
        super(context);

        synchronized (objLock) {
            if(client == null){
                client = new LocationClient(context);
                client.setLocOption(getDefaultLocationClientOption());
            }

            registerListener(mListener);
        }

        start();
    }

    @Override
    public ZLocation getLocation() {
        BDLocation location = client.getLastKnownLocation();
        if(location != null)
            return new ZLocation(location);
        else
            return null;
    }

    @Override
    public void onDestroy() {

    }

    private LocationClient client = null;
    private LocationClientOption mOption,DIYoption;
    private Object  objLock = new Object();

    /***
     *
     * @param listener
     * @return
     */
    public boolean registerListener(BDLocationListener listener){
        boolean isSuccess = false;
        if(listener != null){
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return  isSuccess;
    }

    public void unregisterListener(BDLocationListener listener){
        if(listener != null){
            client.unRegisterLocationListener(listener);
        }
    }

    /***
     *
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option){
        boolean isSuccess = false;
        if(option != null){
            if(client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    public LocationClientOption getOption(){
        return DIYoption;
    }
    /***
     *
     * @return DefaultLocationClientOption
     */
    public LocationClientOption getDefaultLocationClientOption(){
        if(mOption == null){
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(2000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用

        }
        return mOption;
    }

    public void start(){
        synchronized (objLock) {
            if(client != null && !client.isStarted()){
                client.start();
            }
        }
    }
    public void stop(){
        synchronized (objLock) {
            if(client != null && client.isStarted()){
                client.stop();
            }
        }
    }

    public boolean requestHotSpotState(){
        return client.requestHotSpotState();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//
//                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                    for (int i = 0; i < location.getPoiList().size(); i++) {
//                        Poi poi = (Poi) location.getPoiList().get(i);
//                    }
//                }
//                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//
//                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//
//                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//
//                } else if (location.getLocType() == BDLocation.TypeServerError) {
//                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                }
//            }
            if(location != null)
                mZLListener.onLocationChange(new ZLocation(location));

            //TODO test
            StringBuffer time = new StringBuffer()
                    .append(StringUtil.dateTimeFormat(new Date()))
                    .append(" type:");
            if(location != null){
                ZLocation ll = new ZLocation(location);
                time.append("baidu").append(" " + ll.getLongitude() + "," + ll.getLatitude());
            }else{
                time.append("baidu").append(" location null");
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };

    private Location gcjToGps(BDLocation bdLocation){
        if(bdLocation == null)
            return null;

        Gps gps = PositionUtil.gcj_To_Gps84(bdLocation.getLatitude(),bdLocation.getLongitude());
        Location location1 = new Location("GPS");
        location1.setLongitude(gps.getWgLon());
        location1.setLatitude(gps.getWgLat());

        return location1;
    }

}
