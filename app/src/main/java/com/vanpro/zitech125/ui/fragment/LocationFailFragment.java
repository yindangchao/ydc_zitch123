package com.vanpro.zitech125.ui.fragment;

import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.extend.BaseFragment;

/**
 * Created by Jinsen on 16/4/19.
 */
public class LocationFailFragment extends BaseFragment implements View.OnClickListener{

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_location_fail_layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        findViewById(R.id.location_fail_refresh).setOnClickListener(this);
        findViewById(R.id.location_fail_take_pic).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location_fail_refresh:
                refreshLocation();
                break;

            case R.id.location_fail_take_pic:
                toTakePhotoMarks();
                break;
        }
    }

    private void refreshLocation(){

    }

    private void toTakePhotoMarks(){

    }
}
