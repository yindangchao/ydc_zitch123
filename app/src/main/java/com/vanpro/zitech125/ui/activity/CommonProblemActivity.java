package com.vanpro.zitech125.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.extend.CustomToolbarActivity;

/**
 * Created by Jinsen on 16/10/18.
 */

public class CommonProblemActivity extends CustomToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_problem);

        setTitle(R.string.setting_common_problems);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setLisetener() {
        findViewById(R.id.common_problem_1).setOnClickListener(this);
        findViewById(R.id.common_problem_2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.common_problem_1:
                problemDetail(1);
                break;

            case R.id.common_problem_2:
                problemDetail(2);
                break;
        }
    }

    private void problemDetail(int index){
        Intent intent = new Intent(this,CommonProblemDetailActivity.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }
}
