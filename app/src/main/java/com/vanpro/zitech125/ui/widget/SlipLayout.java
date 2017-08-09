package com.vanpro.zitech125.ui.widget;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vanpro.zitech125.R;
import com.vanpro.zitech125.util.AndroidUtils;

/**
 * Created by Jinsen on 16/4/26.
 */
public class SlipLayout extends LinearLayout {

    Context mContext;



    int mScreenHeight = 0;
    int mCompassNaviViewHeight;

    LinearLayout mCompassNaviView;
    RelativeLayout mMapNaviView;

    private Location mCarLocation;

    private Location mCurrentLocation;

    NavigationView mNavigationView;
    private ImageView mCarMarkIcon;
    private TextView mDistanceFromCarTv;
    private TextView mUnitTv;
    private TextView mLocatorAccuracyTv;

    private TextView mParkLongTv;
    private ImageView mSetAlertIv;


    GoogleMap mMap = null;
    MapView mMapView = null;
    private ImageView mTypeSwitchIv;
    private TextView mNaviAlertMsgTv;

    //是否是导航
    private boolean isNavi = false;

    private boolean isCompassView = true;


    public static final CameraPosition SYDNEY =
            new CameraPosition.Builder().target(new LatLng(-33.87365, 151.20689))
                    .zoom(18f)
                    .bearing(10)
                    .tilt(25)
                    .build();

    public SlipLayout(Context context) {
        this(context, null);
    }

    public SlipLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlipLayout(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        setOrientation(VERTICAL);


        LayoutInflater layoutInflater = LayoutInflater.from(context);

        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels - AndroidUtils.getStatusBarHeight(context);
        mCompassNaviViewHeight = mScreenHeight - context.getResources().getDimensionPixelSize(R.dimen.dp_80);

        mCompassNaviView = (LinearLayout) layoutInflater.inflate(R.layout.fragment_navigation_by_compass_layout, null);
        addView(mCompassNaviView);



        mMapNaviView = (RelativeLayout) layoutInflater.inflate(R.layout.fragment_navigation_by_map_layout, null);
        addView(mMapNaviView);

        mCompassNaviView.getLayoutParams().height = mCompassNaviViewHeight;
        mMapNaviView.getLayoutParams().height = mScreenHeight;

        initCompassView(mCompassNaviView);
        initMapView(mMapNaviView);

        connectedView();

    }

    private void initCompassView(View parent){

        mNavigationView = (NavigationView) parent.findViewById(R.id.navigation_view);
        mNavigationView.setNavigation(true);

        mCarMarkIcon = (ImageView) parent.findViewById(R.id.navi_compass_show_car_icon);
        mDistanceFromCarTv = (TextView) parent.findViewById(R.id.navi_compass_distance_to_car);
        mUnitTv = (TextView) parent.findViewById(R.id.navi_compass_unit);
        mLocatorAccuracyTv = (TextView) parent.findViewById(R.id.navi_compass_locator_accuracy_textview);

        mParkLongTv = (TextView) parent.findViewById(R.id.navi_compass_parking_time);
        mSetAlertIv = (ImageView) parent.findViewById(R.id.navi_compass_parking_alert_set);

    }

    private void initMapView(View parent){
        mTypeSwitchIv = (ImageView) parent.findViewById(R.id.navi_by_map_switch);
        mTypeSwitchIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int height = 1;
                if(isCompassView){
                    isCompassView = false;
                    height = 1;
                }else{
                    isCompassView = true;
                    height = mCompassNaviViewHeight;
                }
                ViewGroup.LayoutParams ll = mCompassNaviView.getLayoutParams();
                ll.height = height;
                mCompassNaviView.setLayoutParams(ll);
            }
        });
        mNaviAlertMsgTv = (TextView) parent.findViewById(R.id.navi_by_map_alert_info);

        mMapView = (MapView) parent.findViewById(R.id.navi_by_map_mapview);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);

                setCurLocation(mCurrentLocation);
                setCarLocation(mCarLocation);
            }
        });
    }

    Marker mPeopleMarker = null;

    private Marker addPeopleMarker(){
        if(mMap == null)
            return null;

        if(mPeopleMarker == null) {

            MarkerOptions options = new MarkerOptions();
            options.flat(true).anchor(0.5F, 0.5F);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_people));

            if(mCurrentLocation != null) {
                LatLng ll = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                options.position(ll);
            }else{
                LatLng ll = new LatLng(0, 0);
                options.position(ll);
            }

            mPeopleMarker = mMap.addMarker(options);
        }
        return mPeopleMarker;
    }


    Marker mCarMarker;

    private Marker addCarMarker(){
        if(mMap == null)
            return null;

        if(mCarLocation == null)
            return null;

        if(mCarMarker == null) {
            LatLng ll = new LatLng(mCarLocation.getLatitude(), mCarLocation.getLongitude());
            MarkerOptions options = new MarkerOptions();
            options.position(ll);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_car_dripping_temp));
            mCarMarker = mMap.addMarker(options);
        }
        return mCarMarker;
    }



    public void updateDirection(float d){
        if(addPeopleMarker() != null)
            addPeopleMarker().setRotation(d);

//        if(isNavi){
            mNavigationView.updateDirection(d);
//        }
    }

    //连接状态中
    private void connectedView(){
        mCarMarkIcon.setVisibility(GONE);
        mDistanceFromCarTv.setText("Zitech");
        mUnitTv.setText("is connected");
        mLocatorAccuracyTv.setVisibility(GONE);
        mParkLongTv.setVisibility(GONE);

        mSetAlertIv.setVisibility(GONE);

        mNaviAlertMsgTv.setVisibility(VISIBLE);
        mNaviAlertMsgTv.setText("Zitech is connected");
    }

    private void disconnectView(){
        mCarMarkIcon.setVisibility(VISIBLE);
        mSetAlertIv.setVisibility(VISIBLE);
        mNaviAlertMsgTv.setText("Zitech is disconnected");
        mParkLongTv.setVisibility(VISIBLE);
        mLocatorAccuracyTv.setVisibility(VISIBLE);
        mUnitTv.setText("METERS");

    }

    //marker my car position
    public void setCarLocation(Location location){
        if(location != null){

            mCarLocation = location;
            setNaviMode(true);
            disconnectView();
            showCompassInNavi();

            if(addCarMarker() != null) {
                LatLng ll = new LatLng(mCarLocation.getLatitude(),mCarLocation.getLongitude());
                addCarMarker().setPosition(ll);
            }
        }
    }

    //set view status, is navigation mode?
    public void setNaviMode(boolean isNavi){
        this.isNavi = isNavi;
    }


    public void setCurLocation(Location location){
        if(location != null){
            mCurrentLocation = location;
            if(isNavi){
                showMapViewInNavi();
                showCompassInNavi();
            }else{
                showMapViewInConnected();
                showCompassInConnected();
            }
        }
    }

    private void showCompassInConnected(){
        mCarMarkIcon.setVisibility(GONE);
        mDistanceFromCarTv.setText("Zitech");
        mUnitTv.setText("is connected");
        mLocatorAccuracyTv.setVisibility(GONE);
        mParkLongTv.setVisibility(GONE);
        mSetAlertIv.setVisibility(GONE);
    }

    private void showCompassInNavi(){
        if(mCarLocation == null || mCurrentLocation == null)
            return;

        float distance = mCarLocation.distanceTo(mCurrentLocation);
        if(distance < 5){
            mCarMarkIcon.setVisibility(VISIBLE);
            mDistanceFromCarTv.setVisibility(GONE);
            mUnitTv.setText(R.string.car_is_nearby);
            mLocatorAccuracyTv.setText("Within 5 Meters");
        }else{
            mCarMarkIcon.setVisibility(GONE);
            mDistanceFromCarTv.setVisibility(VISIBLE);
            mDistanceFromCarTv.setText(String.valueOf((int)distance));
            String disStr = getResources().getString(R.string.location_distance_str);
            String unit = "Meters";
            mLocatorAccuracyTv.setText(String.format(disStr,String.valueOf(mCurrentLocation.getAccuracy()),unit));
        }
    }


    private void showMapViewInConnected(){
        carNaviInMapCompass();
    }

    private void showMapViewInNavi(){
        carNaviInMapCompass();

        if(mCarLocation == null || mCurrentLocation == null)
            return;

        float dis = mCarLocation.distanceTo(mCurrentLocation);
        String alertMsg = null;
        if(dis < 5){
            alertMsg = getResources().getString(R.string.car_is_nearby);
        }else{
            alertMsg = (int) dis + " Meters";
        }
        mNaviAlertMsgTv.setText(alertMsg);
    }

    private void carNaviInMapCompass(){
        if(addPeopleMarker() != null){
            LatLng ll = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            addPeopleMarker().setPosition(ll);
        }

        if(mCarLocation != null && mCurrentLocation != null){
            int distance = (int) mCurrentLocation.distanceTo(mCarLocation);
            if(distance < 100){//in 100 meters
                cameraLocation(mCurrentLocation,18);
            }else if(distance < 3000){// in 3000 meters
                cameraLocation(mCurrentLocation,19);
            }else{// > 3000 meters
                cameraLocation(mCurrentLocation,19);
            }
        }else if(mCurrentLocation != null){
            cameraLocation(mCurrentLocation,18);
        }


    }

    private void cameraLocation(Location location, float zoom){
        if(location != null && mMap != null) {
            mMap.stopAnimation();
            CameraUpdate position = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(zoom).build());
            mMap.animateCamera(position);
        }

    }

    public void setAlertOnClickListener(View.OnClickListener listener){
        if(mSetAlertIv != null)
            mSetAlertIv.setOnClickListener(listener);
    }


    public void setParkingLongTime(String time){
        if(mParkLongTv != null)
            mParkLongTv.setText(time);
    }

    public void onCreate(Bundle data) {
        if (mMapView != null)
            mMapView.onCreate(data);
    }

    public void onResume() {
        mMapView.onResume();
    }

    public void onDestroy() {
        mMapView.onDestroy();
    }

    public void onLowMemory() {
        mMapView.onLowMemory();
    }

}
