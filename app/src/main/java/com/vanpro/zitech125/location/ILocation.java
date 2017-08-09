package com.vanpro.zitech125.location;

/**
 * Created by Jinsen on 16/12/26.
 */

public interface ILocation {
    ZLocation getLocation();

    void setLocationListener(ZLocationListener locationListener);

    void onDestroy();
}
