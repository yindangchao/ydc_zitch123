package com.vanpro.zitech125.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.alert.AlertUtil;
import com.vanpro.zitech125.event.ParkAlertTimeEvent;
import com.vanpro.zitech125.manage.NotificationMgr;
import com.vanpro.zitech125.ui.widget.TimeAlertPricker;
import com.vanpro.zitech125.util.AppDataManager;

import de.greenrobot.event.EventBus;


public class ParkingTimeAlertDialog extends Dialog implements View.OnClickListener {

    Context mContext = null;

    TextView mSave = null;
    TextView mClose = null;

    TextView rb30m, rb45m, rb1h, rb2h, rb3h, rb4h;

    int mParkingTime;

    View mTimeView1, mTimeView2, mTimeView3, mMoreBtn;

    TimeAlertPricker mTimeAlertPicker;

    boolean isMore = false;

    public ParkingTimeAlertDialog(Context context) {
        this(context, R.style.TransparentDialog);
    }

    public ParkingTimeAlertDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.dialog_alert_time_layout);

        mTimeView1 = findViewById(R.id.time_layout_1);
        mTimeView2 = findViewById(R.id.time_layout_2);
        mTimeView3 = findViewById(R.id.time_layout_3);

        mTimeAlertPicker = (TimeAlertPricker) findViewById(R.id.time_picker);

        rb30m = (TextView) findViewById(R.id.rb_30minutes);
        rb30m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParkingTime = 30;
                setSetTime(rb30m);
            }
        });


        rb45m = (TextView) findViewById(R.id.rb_45minutes);
        rb45m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParkingTime = 45;
                setSetTime(rb45m);
            }
        });

        rb1h = (TextView) findViewById(R.id.rb_1hours);
        rb1h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParkingTime = 60;
                setSetTime(rb1h);
            }
        });
        rb2h = (TextView) findViewById(R.id.rb_2hours);
        rb2h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParkingTime = 120;
                setSetTime(rb2h);
            }
        });

        rb3h = (TextView) findViewById(R.id.rb_3hours);
        rb3h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParkingTime = 180;
                setSetTime(rb3h);
            }
        });
        rb4h = (TextView) findViewById(R.id.rb_4hours);
        rb4h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParkingTime = 240;
                setSetTime(rb4h);
            }
        });

        mMoreBtn = findViewById(R.id.more_btn);
        mMoreBtn.setOnClickListener(this);


        mSave = (TextView) findViewById(R.id.save);
        mSave.setVisibility(View.VISIBLE);
        mSave.setOnClickListener(this);

        mClose = (TextView) findViewById(R.id.close);
        mClose.setOnClickListener(this);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setDefaultView();
    }

    private void setSetTime(TextView rb) {
        rb30m.setSelected(false);
        rb45m.setSelected(false);
        rb1h.setSelected(false);
        rb2h.setSelected(false);
        rb3h.setSelected(false);
        rb4h.setSelected(false);

        rb.setSelected(true);
    }

    private void setDefaultView(){
        mTimeView1.setVisibility(View.VISIBLE);
        mTimeView2.setVisibility(View.VISIBLE);
        mTimeView3.setVisibility(View.VISIBLE);

        mMoreBtn.setVisibility(View.VISIBLE);

        mTimeAlertPicker.setVisibility(View.GONE);
    }

    private void setMoreView(){
        mTimeView1.setVisibility(View.GONE);
        mTimeView2.setVisibility(View.GONE);
        mTimeView3.setVisibility(View.GONE);

        mMoreBtn.setVisibility(View.GONE);

        mTimeAlertPicker.setVisibility(View.VISIBLE);

        isMore = true;
    }

    /**
     * 设置dialog的标题和内容
     *
     * @param title
     * @param msg
     */
    public ParkingTimeAlertDialog setTitleAndMessage(String title, String msg) {
        setTitle(title);
        setMessage(msg);
        return this;
    }

    /**
     * 设置dialog的标题
     * 设置标题可见
     *
     * @param title
     */
    public ParkingTimeAlertDialog setTitle(String title) {
        return this;
    }

    /**
     * 设置dialog的内容
     * 设置内容可见
     *
     * @param msg
     */
    public ParkingTimeAlertDialog setMessage(String msg) {
        return this;
    }

    /**
     * 设置确认按钮的名字和事件处理
     * 如果不设置 则显示默认的名字和默认的事件处理
     *
     * @param name
     * @param listener
     */
    public ParkingTimeAlertDialog setRightBtn(String name, View.OnClickListener listener) {
        return this;
    }

    /**
     * 设置取消按钮的名字和事件处理
     * 如果不设置，则显示默认的名字和默认的事件处理
     *
     * @param name
     * @param listener
     */
    public ParkingTimeAlertDialog setLeftBtn(String name, View.OnClickListener listener) {
//        mClose.setVisibility(View.VISIBLE);
//        mClose.setText(name);
//        if (listener != null)
//            mClose.setOnClickListener(listener);
        return this;
    }

    public void destory() {
        mContext = null;
    }

    public boolean dismissDialog() {
        if (isShowing()) {
            try {
                dismiss();
            } catch (Exception e) {
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                AppDataManager.getInstance().remove(AppDataManager.KEY.LAST_SET_ALERT_TIME);
                EventBus.getDefault().post(new ParkAlertTimeEvent(0));
                AlertUtil.clearAlertNofity(mContext);
                dismissDialog();
                break;
            case R.id.save:
                int mins = 0;
                if(isMore){
                    mins = mTimeAlertPicker.getSetTimeValue();
                }else{
                    mins = mParkingTime;
                }

                if(mins < 1){
                    return;
                }

                AlertUtil.set(mContext,mins);
                NotificationMgr.getInstance().cancelLocationSuccNotity();

                if(mins > 0) {
                    long endTime = System.currentTimeMillis() + mins * 60 * 1000;
                    AppDataManager.getInstance().setData(AppDataManager.KEY.LAST_SET_ALERT_TIME, endTime);
                    EventBus.getDefault().post(new ParkAlertTimeEvent(mins * 60));
                }
                dismissDialog();
                break;
            case R.id.more_btn:
                setMoreView();
                break;
        }
    }


    public TextView getRightBtn() {
        return mSave;
    }

    public TextView getLeftBtn() {
        return mClose;
    }

    public int getSetTime() {
        return mParkingTime;
    }

}
