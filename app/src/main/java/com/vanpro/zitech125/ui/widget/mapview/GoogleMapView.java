package com.vanpro.zitech125.ui.widget.mapview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

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
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.manage.StatusManage;
import com.vanpro.zitech125.util.LogUtil;

/**
 * Created by Jinsen on 16/12/23.
 */

public class GoogleMapView extends BaseMapView implements OnMapReadyCallback {
    MapView mMapView;
    GoogleMap mMap;

    public GoogleMapView(Context context) {
        super(context);
        mMapView = (MapView) LayoutInflater.from(mContext).inflate(R.layout.view_mapview_google, null);
        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtil.e(TAG,"setOnTouchListener");
                isDragMode = true;
                return false;
            }
        });
    }

    @Override
    public View getMapView() {
        return mMapView;
    }

    @Override
    public void onCreate(Bundle paramBundle, boolean paramBoolean) {
        mMapView.onCreate(paramBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
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
        mCurrentLocation = StatusManage.getInstance().getCurLocation();
        cameraLocation(mCurrentLocation, 18);
    }

    @Override
    public void updateDirection() {
        cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 18);
    }

    //marker my car position
    public void setCarLocation(ZLocation location) {
        if (location != null) {
            mCarLocation = location;
            isNavi = true;

            if (addCarMarker() != null) {
                LatLng ll = new LatLng(mCarLocation.getLocation().getLatitude(), mCarLocation.getLocation().getLongitude());
                addCarMarker().setPosition(ll);
            }

            if (addPeopleMarker() != null) {
                mPeopleMarker.setRotation(StatusManage.getInstance().getBearing());
            }
        }
    }

    @Override
    public void updateCurLocation(ZLocation location) {
        mCurrentLocation = location;
        carNaviInMapCompass();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        setCarLocation(mCarLocation);
        mCurrentLocation = StatusManage.getInstance().getCurLocation();
        updateCurLocation(mCurrentLocation);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LogUtil.e(TAG,"setOnMapClickListener");
                isDragMode = true;
            }
        });
    }

    private void cameraLocation(ZLocation location, float zoom) {
        if(isDragMode)
            return;

        if (location != null && mMap != null) {
            mZoomSize = zoom > 0 ? zoom : mZoomSize;
            mMap.stopAnimation();

            CameraPosition.Builder builder = new CameraPosition.Builder();
            builder.target(new LatLng(location.getLocation().getLatitude(), location.getLocation().getLongitude()));
            builder.bearing(StatusManage.getInstance().getPhoneDigree());
//            if(zoom > 0)
//                builder.zoom(mZoomSize);
            builder.zoom(mZoomSize);


            CameraUpdate position = CameraUpdateFactory.newCameraPosition(builder.build());
            mMap.animateCamera(position);
        }
    }


    Marker mPeopleMarker = null;

    //当前人的位置
    private Marker addPeopleMarker() {
        if (mMap == null)
            return null;

        if (mPeopleMarker == null) {

            MarkerOptions options = new MarkerOptions();
            options.flat(true).anchor(0.5F, 0.5F);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start_point));

            if (mCurrentLocation != null) {
                LatLng ll = new LatLng(mCurrentLocation.getLocation().getLatitude(), mCurrentLocation.getLocation().getLongitude());
                options.position(ll);
            } else {
                LatLng ll = new LatLng(0, 0);
                options.position(ll);
            }

            mPeopleMarker = mMap.addMarker(options);

        }
        return mPeopleMarker;
    }

    Marker mCarMarker;

    private Marker addCarMarker() {
        if (mMap == null)
            return null;

        if (mCarLocation == null)
            return null;

        if (mCarMarker == null) {
            LatLng ll = new LatLng(mCarLocation.getLocation().getLatitude(), mCarLocation.getLocation().getLongitude());
            MarkerOptions options = new MarkerOptions();
            options.position(ll);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_car_dripping));
            mCarMarker = mMap.addMarker(options);
        }

        return mCarMarker;
    }

    private void carNaviInMapCompass() {
        if (addPeopleMarker() != null && mCurrentLocation != null) {
            LatLng ll = new LatLng(mCurrentLocation.getLocation().getLatitude(), mCurrentLocation.getLocation().getLongitude());
            addPeopleMarker().setPosition(ll);
        }

        if (mCarLocation != null && mCurrentLocation != null) {
            int distance = (int) mCurrentLocation.distanceTo(mCarLocation);
            if (distance < 100) {//in 100 meters
                cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 18);
            } else if (distance < 3000) {// in 3000 meters
                cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 13);
            } else {// > 3000 meters
                cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 10);
            }
        } else if (mCurrentLocation != null) {
            cameraLocation(mCurrentLocation, mZoomSize > 0 ? -1 : 18);
        }
    }
}
