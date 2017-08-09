package com.vanpro.zitech125.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.constants.Constants;
import com.vanpro.zitech125.constants.UnitType;
import com.vanpro.zitech125.event.LocationChangeEvent;
import com.vanpro.zitech125.event.SensorRorationChangeEvent;
import com.vanpro.zitech125.event.StatusChangeEvent;
import com.vanpro.zitech125.event.UnitChangeEvent;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.manage.StatusManage;
import com.vanpro.zitech125.ui.extend.BaseFragment;
import com.vanpro.zitech125.ui.widget.mapview.BaiduMapView;
import com.vanpro.zitech125.ui.widget.mapview.BaseMapView;
import com.vanpro.zitech125.ui.widget.mapview.GoogleMapView;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.AppDataManager;
import com.vanpro.zitech125.util.LogUtil;

/**
 * Created by Jinsen on 16/7/1.
 */
public class MapFragment extends BaseFragment {

    TextView mNaviAlertMsgTv;

    private ZLocation mCarLocation;

    private ZLocation mCurrentLocation;

    //是否是导航
    private boolean isNavi = false;

    ImageView mLocatonIv;

    boolean isMeterUnitType = true;

    //当前地图缩放级别
    float mZoomSize = 14;

    BaseMapView mMapView;

    RelativeLayout mMapViewContainer;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_map_layout;
    }

    @Override
    protected void initView() {
        mLocatonIv = (ImageView) findViewById(R.id.map_location);

        mNaviAlertMsgTv = (TextView) findViewById(R.id.map_alert_info);

        mMapViewContainer = (RelativeLayout) findViewById(R.id.map_mapview_parent);

        mMapView = initMapView();
        mMapViewContainer.addView(mMapView.getMapView());
    }

    @Override
    protected void initListener() {
        mLocatonIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentLocation = StatusManage.getInstance().getCurLocation();
                mMapView.showMyLocation(mCurrentLocation);
            }
        });
    }

    @Override
    protected void initData() {
        isMeterUnitType = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.UNIT_TYPE_KEY, true);

        stateView();

        if (StatusManage.getInstance().getStatus() >= 0) {
            setCurLocation(StatusManage.getInstance().getCurLocation());
        }
    }

    //初始化选择地图类型
    private BaseMapView initMapView() {
        int mapType = AppDataManager.getInstance().getInt(AppDataManager.KEY.MAP_SELECTED_ID);

        if (mapType == 1) {//百度地图
            return new BaiduMapView(getContext());
        } else if (mapType == 2) {//Google地图
            return new GoogleMapView(getContext());
        } else if (isZH()) {//根据语言来识别，如果是中文则使用百度地图，其他语言则使用Google地图
            return new BaiduMapView(getContext());
        }

        return new GoogleMapView(getContext());
    }

    //是否是中文
    private boolean isZH() {
        return "ZH".equals(AndroidUtils.getLanguage(getContext()));
    }

    @Override
    public int getTitle() {
        return R.string.nav_map;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && mMapView != null){
            mCurrentLocation = StatusManage.getInstance().getCurLocation();
            mMapView.showMyLocation(mCurrentLocation);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.i("MapFragment", "onActivityCreated");
        if (mMapView != null)
            mMapView.onCreate(savedInstanceState, false);
    }

    public void onResume() {
        super.onResume();
        LogUtil.i("MapFragment", "onResume");
        mMapView.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
        LogUtil.i("MapFragment", "onDestroy");
        mMapView.onDestroy();
    }

    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.i("MapFragment", "onLowMemory");
        mMapView.onLowMemory();
    }

    //正在进行连接
    private void connectingView(){
        setNaviMode(false);
        mNaviAlertMsgTv.setBackgroundResource(R.drawable.map_alert_bg_2);
        mNaviAlertMsgTv.setText(R.string.ble_connecting_str);

        mMapView.setCarMarkerVisible(false);
    }

    //连接状态中
    private void connectedView() {
        setNaviMode(false);
        mNaviAlertMsgTv.setBackgroundResource(R.drawable.map_alert_bg_2);
        mNaviAlertMsgTv.setText(R.string.map_ble_connected);

        mMapView.setCarMarkerVisible(false);
    }

    private void disconnectView() {
        mNaviAlertMsgTv.setBackgroundResource(R.drawable.map_alert_bg_2);
        mNaviAlertMsgTv.setText(R.string.map_ble_disconnected);
    }

    public void setCurLocation(ZLocation location) {
        if ( mMapView != null && location != null) {
            mCurrentLocation = location;
            if (isNavi) {
                showMapViewInNavi();
            }
            mMapView.updateCurLocation(location);
        }
    }

    //marker my car position
    public void setCarLocation(ZLocation location) {
        if (location != null) {
            mCarLocation = location;
            setNaviMode(true);
            disconnectView();

            mMapView.setCarLocation(location);
        }
    }

    //set view status, is navigation mode?
    public void setNaviMode(boolean isNavi) {
        this.isNavi = isNavi;
        mMapView.setCarMarkerVisible(isNavi);
    }

    private void showMapViewInNavi() {
        if (mCarLocation == null || mCurrentLocation == null)
            return;

        float dis = StatusManage.getInstance().getDistance();
        String alertMsg = null;
        if (dis < Constants.NEARBYSIZE) {
            alertMsg = getResources().getString(R.string.car_is_nearby);
        } else {
            String unit = isMeterUnitType ? getString(R.string.setting_unit_meters) : getString(R.string.setting_unit_feet);
            int len = (int) (isMeterUnitType ? dis : dis * UnitType.METER_TO_FEET);
            alertMsg = len + " " + unit;
        }

        if (dis > Constants.NEARBYSIZE) {
            mNaviAlertMsgTv.setBackgroundResource(R.drawable.map_alert_bg_1);
        } else {
            mNaviAlertMsgTv.setBackgroundResource(R.drawable.map_alert_bg_2);
        }

        mNaviAlertMsgTv.setText(alertMsg);
    }

    public void onEventMainThread(LocationChangeEvent event) {
        ZLocation location = StatusManage.getInstance().getCurLocation();
        if (location != null) {
            setCurLocation(location);
        }
    }

    public void onEventMainThread(SensorRorationChangeEvent event) {
        mMapView.updateDirection();
    }

    private void loctionNavi() {
        boolean isNaviMode = getCarLocation();
        if (!isNaviMode) {
            picNavi();
        }
    }

    private boolean getCarLocation() {
        boolean isNaviMode = false;
        ZLocation carLocation = StatusManage.getInstance().getCarLocation();
        if (carLocation != null) {
            setCarLocation(carLocation);
            isNaviMode = true;

        }
        return isNaviMode;
    }

    private void picNavi() {
        mNaviAlertMsgTv.setBackgroundResource(R.drawable.map_alert_bg_2);
        mNaviAlertMsgTv.setText(R.string.compass_loaction_fail);
    }

    public void onEventMainThread(UnitChangeEvent event) {
        isMeterUnitType = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.UNIT_TYPE_KEY, true);
        if (!isNavi) {
            showMapViewInNavi();
        }
    }

    public void onEventMainThread(StatusChangeEvent event) {
        stateView();
    }

    private void stateView() {
        int status = StatusManage.getInstance().getStatus();
        switch (status) {
            case StatusManage.Status.CONTECTED:
                connectedView();
                break;

            case StatusManage.Status.LOCATION_SUCC:
                disconnectView();
                loctionNavi();
                setCurLocation(StatusManage.getInstance().getCurLocation());
//                if (addPeopleMarker() != null) {
//                    mPeopleMarker.setRotation(StatusManage.getInstance().getBearing());
//                }
//
//                cameraLocation(mCurrentLocation,mZoomSize);
                break;

            case StatusManage.Status.LOCATION_FAIL:
                disconnectView();
                picNavi();
                break;

            case StatusManage.Status.CONTECTING:
                connectingView();
                break;
        }
    }

}
