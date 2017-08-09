package com.vanpro.zitech125.ui.widget.mapview;

import android.content.Context;

import com.vanpro.zitech125.location.ZLocation;

/**
 * Created by Jinsen on 16/12/23.
 */

public abstract class BaseMapView implements ZMapView{

    String TAG = getClass().getSimpleName();

    protected ZLocation mCarLocation;

    protected ZLocation mCurrentLocation;

    //是否是导航
    protected boolean isNavi = false;

    //是否是拖动模式
    protected boolean isDragMode = false;

    //当前地图缩放级别
    protected float mZoomSize = 0;

    protected Context mContext;

    public BaseMapView(Context context){
        mContext = context;
    }
}
