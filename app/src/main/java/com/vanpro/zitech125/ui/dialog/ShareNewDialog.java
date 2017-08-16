package com.vanpro.zitech125.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.util.umengsdk.UMengShareListener;
import com.vanpro.zitech125.util.umengsdk.UMengUtils;

/**
 * Created by ydc on 2017/8/16.
 */
public class ShareNewDialog extends Dialog implements View.OnClickListener {
    Context mContext;

    private String shareUrl;

    public ShareNewDialog(Context context, String shareUrl) {
        this(context, R.style.dialogButtom);
        this.shareUrl = shareUrl;
    }

    public ShareNewDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog(context);
    }

    private void initDialog(Context context) {
        mContext = context;
        setContentView(R.layout.dialog_share);
        initView();
        initListener();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(-1, -2);
    }

    private void initView() {

    }

    private void initListener() {
        findViewById(R.id.layout_email).setOnClickListener(this);
        findViewById(R.id.layout_message).setOnClickListener(this);
        findViewById(R.id.layout_whatsapp).setOnClickListener(this);
        findViewById(R.id.share_btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        UMengUtils.SHARE_PLATFORM platform = null;
        switch (v.getId()) {
            case R.id.share_btn_cancel:
                dismiss();
                return;
            case R.id.layout_email:
                platform = UMengUtils.SHARE_PLATFORM.EMAIL;
                break;

            case R.id.layout_message:
                platform = UMengUtils.SHARE_PLATFORM.SMS;
                break;

            case R.id.layout_whatsapp:
                platform = UMengUtils.SHARE_PLATFORM.WHATSAPP;
                break;
        }
        UMengUtils.share((Activity) mContext, platform, "i'm here",
                "i'm here",
                shareUrl, R.drawable.logo,
                new UMengShareListener() {
                    @Override
                    public void onStart(UMengUtils.SHARE_PLATFORM platform) {

                    }

                    @Override
                    public void onResult(UMengUtils.SHARE_PLATFORM platform) {
                        Toast.makeText(mContext, R.string.share_success, Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onError(UMengUtils.SHARE_PLATFORM platform, Throwable throwable) {
                    }

                    @Override
                    public void onCancel(UMengUtils.SHARE_PLATFORM platform) {

                    }
                });
        dismiss();
    }
}
