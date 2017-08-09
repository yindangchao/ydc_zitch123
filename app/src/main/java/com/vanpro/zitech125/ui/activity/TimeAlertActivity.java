package com.vanpro.zitech125.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.alert.AlertUtil;
import com.vanpro.zitech125.event.ParkAlertTimeEvent;
import com.vanpro.zitech125.event.StatusChangeEvent;
import com.vanpro.zitech125.ui.dialog.CommAlertDialog;
import com.vanpro.zitech125.util.AppDataManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Jinsen on 16/7/17.
 */
public class TimeAlertActivity extends Activity implements View.OnClickListener{

    ImageView mIcon;

    TextView mCountdownTimeTx;

    TextView mDescTx;

    TextView mLeftBtn, mRightBtn;

    long mAlertCountdownTime = 0;

    boolean isBigTenMins = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_alert_layout);

        setTitle("");

        initView();
        setLisetener();
        EventBus.getDefault().register(this);
    }

    public void initView() {
        mIcon = (ImageView) findViewById(R.id.time_alert_icon);

        mCountdownTimeTx = (TextView) findViewById(R.id.time_alert_countdown_time);

        mDescTx = (TextView) findViewById(R.id.time_alert_desc);

        mLeftBtn = (TextView) findViewById(R.id.time_alert_btn_left);

        mRightBtn = (TextView) findViewById(R.id.time_alert_btn_right);

        long endTime = AppDataManager.getInstance().getLong(AppDataManager.KEY.LAST_SET_ALERT_TIME);
        if(endTime > 0) {
            if (endTime > System.currentTimeMillis()) {
                mAlertCountdownTime = (endTime - System.currentTimeMillis()) / 1000;
                mHandler.sendEmptyMessageDelayed(0, 1000);
                showAlertTime();
            } else {
                expired();
            }
        }else{
            finish();
        }
    }

    public void setLisetener() {
        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.time_alert_btn_left:
                CommAlertDialog dialog = new CommAlertDialog(this);
                dialog.setTitle(getString(R.string.setting_park_time_finish_alert));
                dialog.setRightBtn(getString(R.string.sure_remove), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isBigTenMins){
                            AppDataManager.getInstance().remove(AppDataManager.KEY.LAST_SET_ALERT_TIME);
                            EventBus.getDefault().post(new ParkAlertTimeEvent(0));
//                    AlertUtil.clearAlertNofity(this);
                        }else{

                        }
                        AlertUtil.clearAlertNofity(TimeAlertActivity.this);
                        finish();
                    }
                });
                dialog.setLeftBtn(getString(R.string.cancel),null);
                dialog.show();
                break;

            case R.id.time_alert_btn_right:
                if(isBigTenMins){
                    finish();
                }else{
                    AlertUtil.clearAlertNofity(this);
                    finish();
                }
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mAlertCountdownTime > 0) {
                mAlertCountdownTime--;
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
            showAlertTime();
        }
    };


    private void showAlertTime() {
        long hours = mAlertCountdownTime / (60 * 60);
        long mins = mAlertCountdownTime % (60 * 60) / 60;
        long seco = mAlertCountdownTime % 60;
        String time = (hours < 10 ? "0" + hours : hours) + ":" +
                (mins < 10 ? "0" + mins : mins) + ":" +
                (seco < 10 ? "0" + seco : seco);

        mCountdownTimeTx.setText(time);

        if (mAlertCountdownTime > 6) {
            isBigTenMins = true;

            mIcon.setImageResource(R.drawable.time_alert_alert_icon_white);
            mLeftBtn.setBackgroundResource(R.drawable.seletor_time_alert_btn_white);
            mRightBtn.setBackgroundResource(R.drawable.seletor_time_alert_btn_white);
            mRightBtn.setText(R.string.Continue);
            mRightBtn.setTextColor(getResources().getColor(R.color.time_alert_btn_save));
        } else {
            isBigTenMins = false;
            mIcon.setImageResource(R.drawable.time_alert_alert_icon_red);
            mLeftBtn.setVisibility(View.GONE);
            mRightBtn.setBackgroundResource(R.drawable.seletor_time_alert_btn_red);
            mRightBtn.setText(R.string.TurnOff);
            mRightBtn.setTextColor(getResources().getColor(R.color.time_alert_btn_red));
        }

    }

    private void expired(){
        mCountdownTimeTx.setText("00:00:00");
        mRightBtn.setTextColor(getResources().getColor(R.color.time_alert_btn_red));
        mIcon.setImageResource(R.drawable.time_alert_alert_icon_red);
        mLeftBtn.setBackgroundResource(R.drawable.seletor_time_alert_btn_red);
        mRightBtn.setBackgroundResource(R.drawable.seletor_time_alert_btn_red);
        mLeftBtn.setVisibility(View.GONE);
        mRightBtn.setBackgroundResource(R.drawable.seletor_time_alert_btn_red);
        mRightBtn.setText(R.string.TurnOff);
        mRightBtn.setTextColor(getResources().getColor(R.color.time_alert_btn_red));
    }

    public void onEventMainThread(StatusChangeEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        EventBus.getDefault().unregister(this);
    }
}
