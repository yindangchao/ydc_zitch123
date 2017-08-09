package com.vanpro.zitech125.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.manage.StatusManage;
import com.vanpro.zitech125.ui.extend.BaseActivity;
import com.vanpro.zitech125.ui.fragment.StartFragment;
import com.vanpro.zitech125.util.AppDataManager;
import com.vanpro.zitech125.util.AppStateSaveUtil;

/**
 * Created by Jinsen on 16/4/8.
 */
public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //结束你的activity
            finish();
            return;
        }

        setContentView(R.layout.activity_start);

        setSwipeEnabled(false);

        autoOpenBLE();

        if(AppDataManager.getInstance().getBoolean(AppDataManager.KEY.LOCATION_STATE)
                && !AppStateSaveUtil.isSameLan()){
            AppStateSaveUtil.removeDevices();
        }

        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_view, new StartFragment());
        ft.commitAllowingStateLoss();

//        LocationMgr.getInstance();
        StatusManage.createInstance();
    }

    private void autoOpenBLE() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //设置蓝牙可见性，最多300秒
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setLisetener() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
