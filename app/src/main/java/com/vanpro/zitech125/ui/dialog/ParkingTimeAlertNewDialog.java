package com.vanpro.zitech125.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.vanpro.zitech125.R;
import com.vanpro.zitech125.alert.AlertUtil;
import com.vanpro.zitech125.event.ParkAlertTimeEvent;
import com.vanpro.zitech125.manage.NotificationMgr;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.AppDataManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ydc on 2017/8/16.
 */
public class ParkingTimeAlertNewDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView mSave = null;
    private TextView mClose = null;
    private WheelPicker pickHourView, pickMinuteView;
    private int selectedHour, selectedMins;
    List<String> hourList, minsList;

    public ParkingTimeAlertNewDialog(Context context) {
        this(context, R.style.TransparentDialog);
    }

    public ParkingTimeAlertNewDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.dialog_alert_time_layout_new);

        pickHourView = (WheelPicker) findViewById(R.id.pick_hour);
        pickMinuteView = (WheelPicker) findViewById(R.id.pick_minute);
        pickHourView.setItemAlign(WheelPicker.ALIGN_RIGHT);
        pickMinuteView.setItemAlign(WheelPicker.ALIGN_LEFT);
        initWheelView(pickHourView);
        initWheelView(pickMinuteView);
        mSave = (TextView) findViewById(R.id.save);
        mSave.setOnClickListener(this);
        mClose = (TextView) findViewById(R.id.close);
        mClose.setOnClickListener(this);
        initPickView();
    }

    private void initWheelView(WheelPicker wheelPicker) {
        wheelPicker.setItemTextColor(mContext.getResources().getColor(R.color.white));
        wheelPicker.setSelectedItemTextColor(mContext.getResources().getColor(R.color.common_orgin_color));
        wheelPicker.setCyclic(true);
        wheelPicker.setCurved(true);
        wheelPicker.setVisibleItemCount(3);
        wheelPicker.setItemSpace(AndroidUtils.dp2px(mContext, 15));
    }

    private void initPickView() {
        hourList = getHourStrs();
        minsList = getMinuteStrs();
        pickHourView.setData(hourList);
        pickMinuteView.setData(minsList);
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
                int hour = pickHourView.getCurrentItemPosition();
                int mins = pickMinuteView.getCurrentItemPosition();
                if (mins < 1) {
                    dismissDialog();
                    return;
                }

                AlertUtil.set(mContext, hour * 60 + mins);
                NotificationMgr.getInstance().cancelLocationSuccNotity();

                if (mins > 0) {
                    long endTime = System.currentTimeMillis() + (hour * 60 + mins) * 60 * 1000;
                    AppDataManager.getInstance().setData(AppDataManager.KEY.LAST_SET_ALERT_TIME, endTime);
                    EventBus.getDefault().post(new ParkAlertTimeEvent((hour * 60 + mins) * 60));
                }
                dismissDialog();
                break;
        }
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


    private List<String> getHourStrs() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            list.add(i + " hour");
        }
        return list;
    }

    private List<String> getMinuteStrs() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 61; i++) {
            list.add(i + " mins");
        }
        return list;
    }
}
