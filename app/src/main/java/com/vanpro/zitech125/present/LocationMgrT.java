package com.vanpro.zitech125.present;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.vanpro.zitech125.MyApplication;
import com.vanpro.zitech125.event.LocationEvent;
import com.vanpro.zitech125.util.LogUtil;

import de.greenrobot.event.EventBus;


/**
 * Created by Jinsen on 16/8/25.
 */
public class LocationMgrT implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = false;

    private Context mContext;

    static LocationMgrT _instance;

    private LocationMgrT() {
        this.mContext = MyApplication.getInstance().getApplicationContext();

        buildGoogleApiClient();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            requestLocationUpdates();
        }

    }

    public static LocationMgrT getInstance() {
        if (_instance == null)
            _instance = new LocationMgrT();

        return _instance;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void requestLocationUpdates(){
        if(!checkPermission(mContext))
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mRequestingLocationUpdates = true;
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        LogUtil.d("GettingUpdatedLocation", "onConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        LogUtil.d("GettingUpdatedLocation", "" + location.toString());
        Toast.makeText(mContext, "Location Updated", Toast.LENGTH_SHORT).show();
        if(location != null) {
            LocationEvent event = new LocationEvent();
//            event.location = location;
            EventBus.getDefault().post(event);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtil.d("GettingUpdatedLocation", "onConnectionFailed");
    }

    public static boolean checkPermission(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }


    public Location getLocation() {
        if (checkPermission(mContext)) {
            return null;
        }
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }


    public boolean hasOpenGps() {
        LocationManager mLocationManager = (LocationManager) mContext.
                getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void destory(){
    }

}
