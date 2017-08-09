package com.vanpro.zitech125.ui.widget.mapview;

import android.os.Bundle;
import android.view.View;

import com.vanpro.zitech125.location.ZLocation;

/**
 * Created by Jinsen on 16/12/23.
 */

public interface ZMapView {

    View getMapView();

    void onCreate(Bundle bundle, boolean save);

    void onDestroy();

    void onLowMemory();

    void onPause();

    void onResume();

    void onSaveInstanceState(Bundle state);

    void setCarMarkerVisible(boolean visible);

    void showMyLocation(ZLocation location);

    void updateDirection();

    void setCarLocation(ZLocation location);

    void updateCurLocation(ZLocation location);
}
