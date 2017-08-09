package com.vanpro.zitech125.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.event.UnitChangeEvent;
import com.vanpro.zitech125.ui.extend.CustomToolbarActivity;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.AppDataManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Jinsen on 16/9/25.
 */
public class GlobalSettingActivity extends CustomToolbarActivity {

    ImageView mSoundSwitchIv, mNotifySwitchIv;

    TextView mUnitMTv, mUnitFeetTv;

    ImageView mUnitSwtich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_setting);
    }

    @Override
    public void initView() {
        mSoundSwitchIv = (ImageView) findViewById(R.id.setting_sound_switch);
        mNotifySwitchIv = (ImageView) findViewById(R.id.setting_notify_switch);

        mUnitMTv = (TextView) findViewById(R.id.global_setting_unit);
        mUnitFeetTv = (TextView) findViewById(R.id.global_setting_feet);

        mUnitSwtich = (ImageView) findViewById(R.id.global_setting_unit_switch);

        initData();
    }

    @Override
    public void setLisetener() {
        mSoundSwitchIv.setOnClickListener(this);
        mNotifySwitchIv.setOnClickListener(this);

        mUnitSwtich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = !v.isSelected();
                showUnitView(!isChecked);
                AppDataManager.getInstance().setData(AppDataManager.KEY.UNIT_TYPE_KEY,!isChecked);
                EventBus.getDefault().post(new UnitChangeEvent());
            }
        });


    }

    private void initData(){
        boolean sound = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_SOUND_NOTIFY,true);
        mSoundSwitchIv.setSelected(sound);

        boolean notify = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_NOTIFY_SWITCH,true);
        mNotifySwitchIv.setSelected(notify);

        boolean isMeter = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.UNIT_TYPE_KEY,true);
        showUnitView(isMeter);
    }

    private void showUnitView(boolean checked){
        mUnitSwtich.setSelected(!checked);
        mUnitFeetTv.setText(checked ? R.string.setting_unit_meters : R.string.setting_unit_feet);
    }

    private void showMapView(boolean checked){

    }
//    //初始化选择地图类型
//    private BaseMapView initMapView() {
//        int mapType = AppDataManager.getInstance().getInt(AppDataManager.KEY.MAP_SELECTED_ID);
//
//        if (mapType == 1) {//百度地图
//            return new BaiduMapView(getContext());
//        } else if (mapType == 2) {//Google地图
//            return new GoogleMapView(getContext());
//        } else if (isZH()) {//根据语言来识别，如果是中文则使用百度地图，其他语言则使用Google地图
//            return new BaiduMapView(getContext());
//        }
//
//        return new GoogleMapView(getContext());
//    }

    //是否是中文
    private boolean isZH() {
        return "ZH".equals(AndroidUtils.getLanguage(this));
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.setting_sound_switch:
                switchSound();
                break;

            case R.id.setting_notify_switch:
                switchNotify();
                break;
        }
    }




    private void switchSound(){
        boolean isOpen = mSoundSwitchIv.isSelected();
        isOpen = !isOpen;
        mSoundSwitchIv.setSelected(isOpen);
        AppDataManager.getInstance().setData(AppDataManager.KEY.IS_SOUND_NOTIFY,isOpen);
    }

    private void switchNotify(){
        boolean isOpen = mNotifySwitchIv.isSelected();
        isOpen = !isOpen;
        mNotifySwitchIv.setSelected(isOpen);
        AppDataManager.getInstance().setData(AppDataManager.KEY.IS_NOTIFY_SWITCH,isOpen);
    }

}
