package com.vanpro.zitech125.ui.extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.widget.SwipeLayout;


/**
 * 滑动返回基类
 */
public class SwipeActivity extends AppCompatActivity {

    private SwipeLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeLayout = new SwipeLayout(this);
    }

    public void setSwipeAnyWhere(boolean swipeAnyWhere) {
        swipeLayout.setmSwipeAnyWhere(swipeAnyWhere);
    }

    public boolean isSwipeAnyWhere() {
        return swipeLayout.ismSwipeAnyWhere();
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        swipeLayout.setmSwipeEnabled(swipeEnabled);
    }

    public boolean isSwipeEnabled() {
        return swipeLayout.ismSwipeEnabled();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        swipeLayout.replaceLayer(this);
    }


    @Override
    public void finish() {
        super.finish();
        if(isSwipeEnabled()) {
            if (swipeLayout.ismSwipeFinished()) {
                overridePendingTransition(0, 0);
            } else {
                swipeLayout.cancelPotentialAnimation();
                overridePendingTransition(0, R.anim.activity_right_out);
            }
        }else{
            overridePendingTransition(0, R.anim.activity_right_out);
        }
    }


}