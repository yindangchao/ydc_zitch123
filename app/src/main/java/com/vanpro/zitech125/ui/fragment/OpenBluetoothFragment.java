package com.vanpro.zitech125.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.activity.TurnOnBLEHelpActivity;
import com.vanpro.zitech125.ui.extend.BaseFragment;

/**
 * Created by Jinsen on 16/7/19.
 */
public class OpenBluetoothFragment extends BaseFragment {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_turn_on_ble_layout;
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

        findViewById(R.id.turn_on_ble_locator_accuracy_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TurnOnBLEHelpActivity.class));
            }
        });
    }

    @Override
    protected void initData() {

    }
}
