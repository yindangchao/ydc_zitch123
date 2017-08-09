package com.vanpro.zitech125.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.present.LocationMgr;
import com.vanpro.zitech125.ui.dialog.CommAlertDialog;
import com.vanpro.zitech125.ui.extend.BaseFragment;

/**
 * Created by Jinsen on 16/4/10.
 */
public class BindBluetoothFragment extends BaseFragment implements View.OnClickListener{
    LocationMgr mLocalPresenter;
    TextView mLocationTv = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_bind_bt_layout;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
        mLocalPresenter = LocationMgr.getInstance();
        if(mLocalPresenter.hasOpenGps()){
            showToast("GPS定位服务已开启");
        }else{
            CommAlertDialog dialog = new CommAlertDialog(getActivity());
            dialog.setTitle("还没有开启GPS？");
            dialog.setRightBtn("设置", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGps();
                }
            });
            dialog.show();
        }

        mLocalPresenter.getLocation();
    }

    private void openGps() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        ZLocation location = mLocalPresenter.getLocation();

        if(location != null){
            mLocationTv.setText("GPS:lat="+location.getLocation().getLatitude() + " long="+location.getLocation().getLongitude());
        }
    }
}
