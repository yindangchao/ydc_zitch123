package com.vanpro.zitech125.ui.extend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.vanpro.zitech125.AppManager;
import com.vanpro.zitech125.R;


/**
 * activity 的基类
 * Created by Jinsen on 2015/8/23.
 */
public abstract class BaseActivity extends SwipeActivity{

    public abstract void initView();

    public abstract void setLisetener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initView();
        setLisetener();
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AppManager.getInstance().finishActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 按下键盘上返回按钮
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            onBackTransition();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    protected void onGoTransition() {
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    protected void onBackTransition() {
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onGoTransition();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        onGoTransition();
    }

//    @Override
//    public void onBackPressed() {
//        setResult(RESULT_OK);
//        finish();
//    }

    public void showToast(int resId){
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 结束当前activity
     */
    @Override
    public void finish() {
        Activity activity = AppManager.getInstance().getRootActivity(this);
        if (activity == this)
            super.finish();
        else
            activity.finish();
        System.gc();
    }
}
