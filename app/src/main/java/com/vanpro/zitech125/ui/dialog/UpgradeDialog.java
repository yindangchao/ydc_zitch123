package com.vanpro.zitech125.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.entity.UpgradeEntity;
import com.vanpro.zitech125.util.StringUtil;


/**
 * 通用提醒框
 * <p>
 * Created by Jinsen on 2015/8/21.
 */
public class UpgradeDialog extends Dialog implements View.OnClickListener {

    Context mContext = null;

    TextView mNoteTv = null;

    TextView mVersionNameTv = null;
    TextView mApkSizeTv = null;

    TextView mOkBtn;


    public UpgradeDialog(Context context) {
        this(context, R.style.TransparentDialog);
    }

    public UpgradeDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.dialog_upgrade_layout);

        mOkBtn = (TextView) findViewById(R.id.upgrade_update_btn);

        mNoteTv = (TextView) findViewById(R.id.upgrade_notes);
        mVersionNameTv = (TextView) findViewById(R.id.upgrade_version_name);
        mApkSizeTv = (TextView) findViewById(R.id.upgrade_apk_size);


        setListener();

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void setListener(){
        findViewById(R.id.upgrade_update_btn).setOnClickListener(this);
        findViewById(R.id.upgrade_cancel_btn).setOnClickListener(this);
    }

    public void setUpgradeData(UpgradeEntity data){
        mNoteTv.setText(data.getReleaseNotes());
        mVersionNameTv.setText(mContext.getText(R.string.setting_version_title) + data.getVersionName());
        String sizeName = Math.round(100*data.getFilesize()/1024/1024)/100.0 + "M";
        mApkSizeTv.setText(sizeName);
    }

    /**
     * 如果不设置 则显示默认的名字和默认的事件处理
     *
     * @param listener
     */
    public UpgradeDialog setOkBtn(String name, View.OnClickListener listener) {
        if(StringUtil.isNotEmpty(name)){
            mOkBtn.setText(name);
        }

        if (listener != null)
            mOkBtn.setOnClickListener(listener);
        return this;
    }

    public void setCancelBtn(View.OnClickListener listener){
        if(listener != null)
            findViewById(R.id.upgrade_cancel_btn).setOnClickListener(listener);
    }

    public void destory() {
        mContext = null;
        dismiss();
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
            case R.id.upgrade_update_btn:
                dismissDialog();
                break;
            case R.id.upgrade_cancel_btn:
                dismissDialog();
                break;
        }
    }
}
