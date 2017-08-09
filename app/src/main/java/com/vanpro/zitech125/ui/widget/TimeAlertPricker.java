package com.vanpro.zitech125.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.vanpro.zitech125.R;

/**
 * Created by Jinsen on 16/6/15.
 */
public class TimeAlertPricker extends FrameLayout {

    NumberPicker mHoursPicker;
    NumberPicker mMinutePicker;

    String[] HOURS_DISPLAY = new String[]{"0 hours","1 hours","2 hours","3 hours","4 hours","5 hours","6 hours","7 hours"
            ,"8 hours","9 hours","10 hours","11 hours","12 hours"};

    String[] MINUTE_DISPLAY = new String[]{"0 mins", "5 mins", "10 mins", "15 mins", "20 mins", "25 mins", "30 mins"
            , "35 mins", "40 mins", "45 mins", "50 mins", "55 mins"};

    private int mHours, mMinutes;

    public TimeAlertPricker(Context context) {
        this(context,null);
    }

    public TimeAlertPricker(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TimeAlertPricker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.view_alert_time_layout,this);

        HOURS_DISPLAY = getResources().getStringArray(R.array.setting_park_time_hours_arr);
        MINUTE_DISPLAY = getResources().getStringArray(R.array.setting_park_time_min_arr);

        initView();
    }

    private void initView(){
        mHoursPicker = (NumberPicker) findViewById(R.id.hours_picker);
        mHoursPicker.setOnValueChangedListener(onChangeListener);

        mMinutePicker = (NumberPicker) findViewById(R.id.minute_picker);
        mMinutePicker.setOnValueChangedListener(onChangeListener);

        mHoursPicker.setDisplayedValues(HOURS_DISPLAY);
        mHoursPicker.setMaxValue(HOURS_DISPLAY.length-1);
        mMinutePicker.setDisplayedValues(MINUTE_DISPLAY);
        mMinutePicker.setMaxValue(MINUTE_DISPLAY.length-1);
    }


    NumberPicker.OnValueChangeListener onChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (picker == mHoursPicker) {
                mHours = newVal;
            } else {
                mMinutes = newVal;
            }

        }
    };


    public int getSetTimeValue(){
        return mHours * 60 + mMinutes * 5;
    }


}
