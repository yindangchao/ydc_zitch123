package com.vanpro.zitech125.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.extend.BaseActivity;

/**
 * Created by Jinsen on 16/4/8.
 */
public class TurnOnBLEHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_open_bluetooth_layout);

        setSwipeEnabled(false);

    }

    @Override
    public void initView() {
        findViewById(R.id.open_bluetooth_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void setLisetener() {

    }

}
