package com.vanpro.zitech125.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.extend.CustomToolbarActivity;

/**
 * Created by Jinsen on 16/10/18.
 */

public class CommonProblemDetailActivity extends CustomToolbarActivity {

    private int mIndex = 1;

    private TextView mTitleTv,mContentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_problem_detail);
        setTitle(R.string.setting_common_problem);

        mIndex = getIntent().getIntExtra("index",1);
    }

    @Override
    public void initView() {
        mTitleTv = (TextView) findViewById(R.id.common_problem_detail_title);
        mContentTv = (TextView) findViewById(R.id.common_problem_detail_content);

        setDate();
    }

    private void setDate(){
        switch (mIndex){
            case 1:
                mTitleTv.setText(R.string.common_problem_1_title);
                mContentTv.setText(R.string.common_problem_1_content);
                break;

            case 2:
                mTitleTv.setText(R.string.common_problem_2_title);
                mContentTv.setText(R.string.common_problem_2_content);
                break;

        }
    }

    @Override
    public void setLisetener() {

    }


}
