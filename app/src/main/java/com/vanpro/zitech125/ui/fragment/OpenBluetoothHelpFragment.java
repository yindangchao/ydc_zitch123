package com.vanpro.zitech125.ui.fragment;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.extend.BaseFragment;

/**
 * Created by Jinsen on 16/7/19.
 */
public class OpenBluetoothHelpFragment extends BaseFragment {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_open_bluetooth_layout;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.open_bluetooth_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
