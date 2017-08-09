package com.vanpro.zitech125.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.manage.StatusManage;
import com.vanpro.zitech125.present.LocationMgr;
import com.vanpro.zitech125.ui.extend.BaseActivity;
import com.vanpro.zitech125.ui.fragment.MapFragment;
import com.vanpro.zitech125.util.AndroidUtils;

/**
 * Created by Jinsen on 17/2/12.
 */

public class MapviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        setSwipeEnabled(false);

        AndroidUtils.verifyLocationPermissions(this);

        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_view, new MapFragment());
        ft.commit();

        LocationMgr.getInstance();
        StatusManage.createInstance();
    }

    @Override
    public void initView() {

    }

    @Override
    public void setLisetener() {

    }
}
