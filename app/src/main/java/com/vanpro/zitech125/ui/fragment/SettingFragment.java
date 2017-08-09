package com.vanpro.zitech125.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.event.DeviceRemoveEvent;
import com.vanpro.zitech125.event.UnitChangeEvent;
import com.vanpro.zitech125.ui.activity.CommonProblemActivity;
import com.vanpro.zitech125.ui.activity.ContactUsActivity;
import com.vanpro.zitech125.ui.activity.UseDescActivity;
import com.vanpro.zitech125.ui.activity.UseragreementActivity;
import com.vanpro.zitech125.ui.dialog.CommAlertDialog;
import com.vanpro.zitech125.ui.dialog.ShareDialog;
import com.vanpro.zitech125.ui.extend.BaseFragment;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.AppDataManager;
import com.vanpro.zitech125.util.AppStateSaveUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by Jinsen on 16/7/1.
 */
public class SettingFragment extends BaseFragment implements OnClickListener {


    TextView mVersionTextTv;
    TextView mRemoveBtn;

    ImageView mSoundSwitchIv, mNotifySwitchIv;

    TextView mUnitMTv, mUnitMeterTv,mUnitFeetTv;

    ImageView mUnitSwtich;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_setting_layout;
    }

    @Override
    protected void initView() {
        mSoundSwitchIv = (ImageView) findViewById(R.id.setting_sound_switch);
        mNotifySwitchIv = (ImageView) findViewById(R.id.setting_notify_switch);

        mUnitMTv = (TextView) findViewById(R.id.global_setting_unit);
        mUnitFeetTv = (TextView) findViewById(R.id.global_setting_feet);
        mUnitMeterTv =  (TextView) findViewById(R.id.global_setting_meter);

        mUnitSwtich = (ImageView) findViewById(R.id.global_setting_unit_switch);
        mVersionTextTv = (TextView) findViewById(R.id.setting_version_text);
        mRemoveBtn = (TextView) findViewById(R.id.setting_remove_btn);
    }

    @Override
    protected void initListener() {
//        findViewById(R.id.setting_global_setting).setOnClickListener(this);
        findViewById(R.id.setting_About).setOnClickListener(this);
        findViewById(R.id.setting_support).setOnClickListener(this);
        findViewById(R.id.setting_about_use).setOnClickListener(this);
        findViewById(R.id.setting_common_problem).setOnClickListener(this);
        findViewById(R.id.setting_contact_us).setOnClickListener(this);
        mRemoveBtn.setOnClickListener(this);
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

    @Override
    protected void initData() {
        String version = getString(R.string.setting_version, AndroidUtils.getAppVersionName(getActivity()));
        mVersionTextTv.setText(version);

        boolean sound = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_SOUND_NOTIFY,true);
        mSoundSwitchIv.setSelected(sound);

        boolean notify = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_NOTIFY_SWITCH,true);
        mNotifySwitchIv.setSelected(notify);

        boolean isMeter = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.UNIT_TYPE_KEY,true);
        showUnitView(isMeter);
    }

    private void showUnitView(boolean checked){
        mUnitSwtich.setSelected(!checked);
        mUnitFeetTv.setSelected(!checked);
        mUnitMeterTv.setSelected(checked);
    }

    @Override
    public int getTitle() {
        return R.string.nav_set_up;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_About:
                startActivity(new Intent(getContext(), UseragreementActivity.class));
                break;

            case R.id.setting_about_use:
                useDesc();
                break;

            case R.id.setting_contact_us:
                startActivity(new Intent(getContext(), ContactUsActivity.class));
                break;

            case R.id.setting_remove_btn:
                remove();
                break;

            case R.id.setting_common_problem:
                startActivity(new Intent(getContext(), CommonProblemActivity.class));
                break;
            case R.id.setting_sound_switch:
                switchSound();
                break;

            case R.id.setting_notify_switch:
                switchNotify();
                break;
        }
    }

    private void shareDialog(){
        ShareDialog shareDialog = new ShareDialog(getContext());
        shareDialog.show();
    }

    private void remove() {
        final CommAlertDialog dialog = new CommAlertDialog(getActivity());
        dialog.setTitle(getString(R.string.setting_remove_title));
        dialog.setMessage(getString(R.string.setting_remove_content));
        dialog.setLeftBtn(getString(R.string.cancel), null);
        dialog.setRightBtn(getString(R.string.sure), new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AppStateSaveUtil.removeDevices();
                EventBus.getDefault().post(new DeviceRemoveEvent());
            }
        });
        dialog.show();
    }


    private void useDesc() {
        startActivity(new Intent(getContext(), UseDescActivity.class));
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

    @Override
    public void onResume() {
        super.onResume();
//        Location l = LocationMgr.getInstance().getLocation().getLocation();
//        LogUtil.e("xxxx location", l == null ? "location fail" : l.getLatitude() + "," + l.getLongitude());
    }
}
