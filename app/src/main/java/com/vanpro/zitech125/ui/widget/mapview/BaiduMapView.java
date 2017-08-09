package com.vanpro.zitech125.ui.widget.mapview;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.vanpro.zitech125.R;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.manage.StatusManage;
import com.vanpro.zitech125.util.LogUtil;

/**
 * Created by Jinsen on 16/12/26.
 */

public class BaiduMapView extends BaseMapView {

    MapView mMapView = null;
    BaiduMap mBaiduMap = null;

    Marker mPeopleMarker = null;

    Marker mCarMarker = null;

    public BaiduMapView(Context context) {
        super(context);
        SDKInitializer.initialize(context.getApplicationContext());
        mMapView = new MapView(mContext);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mCurrentLocation = StatusManage.getInstance().getCurLocation();

        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                isDragMode = true;
                LogUtil.e(TAG,"setOnMapTouchListener");
            }
        });
//        //构建Marker图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.marker_start_point);
//        MyLocationConfiguration config = new MyLocationConfiguration(
//                MyLocationConfiguration.LocationMode.NORMAL, true, bitmap, Color.RED, Color.BLUE);
//        mBaiduMap.setMyLocationConfigeration(config);
    }

    @Override
    public View getMapView() {
        return mMapView;
    }

    @Override
    public void onCreate(Bundle bundle, boolean save) {
        mMapView.onCreate(mContext, bundle);
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onPause() {
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        mMapView.onSaveInstanceState(state);
    }

    @Override
    public void setCarMarkerVisible(boolean visible) {
        isNavi = visible;

        if (!visible && mCarMarker != null) {
            mCarMarker.remove();
            mCarMarker = null;
            mCarLocation = null;
        }
    }

    @Override
    public void showMyLocation(ZLocation location) {
        isDragMode = false;
        mCurrentLocation = location;
        cameraLocation(mCurrentLocation, 18);
//        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(200)
//                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(StatusManage.getInstance().getBearing()).latitude(mCurrentLocation.getBDLocation().getLatitude())
//                .longitude(mCurrentLocation.getBDLocation().getLongitude()).build();
//        // 设置定位数据
//        mBaiduMap.setMyLocationData(locData);
    }

    @Override
    public void updateDirection() {
        cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 18);
    }

    @Override
    public void setCarLocation(ZLocation location) {
        if (location != null) {
            mCarLocation = location;
            isNavi = true;

            if (addCarMarker() != null) {
                LatLng ll = mCarLocation.getBDLatLng();
                addCarMarker().setPosition(ll);
            }

            if (addPeopleMarker() != null) {
                mPeopleMarker.setRotate(StatusManage.getInstance().getBearing());

                setPeopleMarkerRotaiton();
            }
        }
    }

    private void setPeopleMarkerRotaiton() {
        if (isNavi && mCurrentLocation != null) {
            Location car = mCarLocation.getLocation();
            Location people = mCurrentLocation.getLocation();
            float rotate = people.bearingTo(car);
            mPeopleMarker.setRotate(rotate);
        }
    }

    @Override
    public void updateCurLocation(ZLocation location) {
        mCurrentLocation = location;
//        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(200)
//                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(StatusManage.getInstance().getBearing()).latitude(mCurrentLocation.getBDLocation().getLatitude())
//                .longitude(mCurrentLocation.getBDLocation().getLongitude()).build();
//        // 设置定位数据
//        mBaiduMap.setMyLocationData(locData);
        carNaviInMapCompass();
    }

    private void carNaviInMapCompass() {
        if (addPeopleMarker() != null) {
            LatLng ll = mCurrentLocation.getBDLatLng();
            addPeopleMarker().setPosition(ll);
        }

        if (mCarLocation != null && mCurrentLocation != null) {
            int distance = (int) mCurrentLocation.distanceTo(mCarLocation);
            if (distance < 100) {//in 100 meters
                cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 18);
            } else if (distance < 3000) {// in 3000 meters
                cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 16);
            } else {// > 3000 meters
                cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 16);
            }
        } else if (mCurrentLocation != null) {
            cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 18);
        }
    }

    Marker addPeopleMarker() {
        if (mPeopleMarker == null) {
            //定义Maker坐标点
            LatLng point = null;
            if (mCurrentLocation != null) {
                point = mCurrentLocation.getBDLatLng();
            } else {
                point = new LatLng(0, 0);
            }

            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.marker_start_point);
//                    .fromResource(R.drawable.marker_people);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions options = new MarkerOptions()
                    .position(point)  //设置marker的位置
                    .icon(bitmap)  //设置marker图标
                    .zIndex(9)  //设置marker所在层级
                    .draggable(true);  //设置手势拖拽
            //将marker添加到地图上
            mPeopleMarker = (Marker) (mBaiduMap.addOverlay(options));
        }
        return mPeopleMarker;
    }

    Marker addCarMarker() {
        if (mCarLocation == null || mBaiduMap == null)
            return null;

        if (mCarMarker == null) {
            //定义Maker坐标点
            LatLng point = mCarLocation.getBDLatLng();

            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.marker_car_dripping);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions options = new MarkerOptions()
                    .position(point)  //设置marker的位置
                    .icon(bitmap)  //设置marker图标
                    .zIndex(9)  //设置marker所在层级
                    .draggable(true);  //设置手势拖拽
            //将marker添加到地图上
            mCarMarker = (Marker) (mBaiduMap.addOverlay(options));
        }
        return mCarMarker;
    }

    private void cameraLocation(ZLocation location, float zoom) {
        if(isDragMode)
            return;

        if (location != null && mBaiduMap != null) {
            mZoomSize = zoom > 0 ? zoom : mZoomSize;
            LatLng latLng = location.getBDLatLng();
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng);
//            if (zoom > 0)
//                builder.zoom(zoom);
            builder.zoom(mZoomSize);

            MapStatusUpdate updateFactory = MapStatusUpdateFactory.newMapStatus(builder.build());

            mBaiduMap.animateMapStatus(updateFactory);
        }
    }

    //转换方法如下
    public LatLng convertGPSToBaidu(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    private Location convertGPSToBaidu(Location location) {
        LatLng sourceLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        Location baiDuLocation = new Location("gps");
        baiDuLocation.setLatitude(desLatLng.latitude);
        baiDuLocation.setLongitude(desLatLng.longitude);
        return baiDuLocation;
    }
}