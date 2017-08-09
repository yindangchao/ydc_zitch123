package com.vanpro.zitech125.present;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.vanpro.zitech125.MyApplication;
import com.vanpro.zitech125.event.LocationEvent;
import com.vanpro.zitech125.location.BaiduLocation;
import com.vanpro.zitech125.location.GoogleLocation;
import com.vanpro.zitech125.location.ZBaseLocation;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.location.ZLocationListener;
import com.vanpro.zitech125.util.AppDataManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Jinsen on 16/4/10.
 */
public class LocationMgr {
    String TAG = LocationMgr.this.getClass().getSimpleName();

    Context mContext;

    LocationManager mLocationManager;

    static LocationMgr _instance;

    ZBaseLocation mLocation = null;

    //上次使用的定位服务
    int mLastType = -1;

    private LocationMgr() {
        this.mContext = MyApplication.getInstance().getApplicationContext();

        //绑定监听状态
        checkPermission(mContext);

        mLocation = initLocation();
        mLocation.setLocationListener(mLocationListener);

        getLocation();
    }

    //初始化选择地图类型
    private ZBaseLocation initLocation() {
        int mapType = AppDataManager.getInstance().getInt(AppDataManager.KEY.MAP_SELECTED_ID);

        if (mapType == 1) {//百度地图
            return new BaiduLocation(MyApplication.getInstance().getApplicationContext());
        } else if (mapType == 2) {//Google地图
            return new GoogleLocation(MyApplication.getInstance().getApplicationContext());
        } else if (isZH()) {//根据语言来识别，如果是中文则使用百度地图，其他语言则使用Google地图
            return new BaiduLocation(MyApplication.getInstance().getApplicationContext());
        }

        return new GoogleLocation(MyApplication.getInstance().getApplicationContext());
    }

    //是否是中文
    private boolean isZH() {
        String able = MyApplication.getInstance().getResources().getConfiguration().locale.getLanguage();
        return "ZH".equals(able.toUpperCase());
    }

    public static LocationMgr getInstance() {
        if (_instance == null)
            _instance = new LocationMgr();

        return _instance;
    }

    public boolean hasOpenGps() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public ZLocation getLocation() {
        if (checkPermission(mContext)) {
            return null;
        }

        return mLocation.getLocation();
    }

    private ZLocationListener mLocationListener = new ZLocationListener() {
        @Override
        public void onLocationChange(ZLocation location) {
//            OthersController.uploadLocationInfo(location);
            LocationEvent event = new LocationEvent();
            event.location = location;
            EventBus.getDefault().post(event);
        }
    };

    public static boolean checkPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    public void destory() {
        mLocationManager.removeGpsStatusListener(null);
        mLocationManager = null;
    }
}