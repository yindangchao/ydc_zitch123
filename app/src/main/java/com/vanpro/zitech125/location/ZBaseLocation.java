package com.vanpro.zitech125.location;

import android.content.Context;

/**
 * Created by Jinsen on 16/12/26.
 */

public abstract class ZBaseLocation implements ILocation{

    String TAG = this.getClass().getSimpleName();

    Context mContext;

    ZLocationListener mZLListener;

    public ZBaseLocation(Context context){
        this.mContext = context;
    }

    @Override
    public void setLocationListener(ZLocationListener locationListener) {
        this.mZLListener = locationListener;
    }
}
