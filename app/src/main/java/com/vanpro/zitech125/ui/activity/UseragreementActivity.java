package com.vanpro.zitech125.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.extend.CustomToolbarActivity;

/**
 * Created by Jinsen on 16/9/25.
 */
public class UseragreementActivity extends CustomToolbarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useragreement);
        setTitle(R.string.setting_about);
    }

    @Override
    public void initView() {
        initData();
    }

    @Override
    public void setLisetener() {
        findViewById(R.id.about_TAC).setOnClickListener(this);
        findViewById(R.id.about_PP).setOnClickListener(this);
        findViewById(R.id.about_Imprint).setOnClickListener(this);
    }

    private void initData(){

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.about_TAC:
                Intent tac = new Intent(this,WebviewActivity.class);
                tac.putExtra("url","file:///android_asset/AGBTermsConditions.htm");
                tac.putExtra("title",getString(R.string.useragreement_tac));
                startActivity(tac);
                break;

            case R.id.about_PP:
                Intent pp = new Intent(this,WebviewActivity.class);
                pp.putExtra("url","file:///android_asset/DatenschutzPearlGmbH.htm");
                pp.putExtra("title",getString(R.string.useragreement_pp));
                startActivity(pp);
                break;

            case R.id.about_Imprint:
                Intent i = new Intent(this,WebviewActivity.class);
                i.putExtra("url","file:///android_asset/RechtlichesPearlGmbH.htm");
                i.putExtra("title",getString(R.string.useragreement_imprint));
                startActivity(i);
                break;
        }
    }

}
