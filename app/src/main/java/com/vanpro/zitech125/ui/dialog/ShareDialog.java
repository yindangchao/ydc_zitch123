package com.vanpro.zitech125.ui.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.present.LocationMgr;
import com.vanpro.zitech125.util.UIHelper;

/**
 * Created by Jinsen on 16/7/6.
 */
public class ShareDialog extends Dialog implements View.OnClickListener {

    Context mContext;

    String mShareContent = null;

    public ShareDialog(Context context) {
        this(context, R.style.dialogButtom);
    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog(context);

        mShareContent = mContext.getResources().getString(R.string.share_app_text);
    }

    private void initDialog(Context context) {
        mContext = context;
        setContentView(R.layout.dialog_share_layout);
        initView();
        initListener();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(-1, -2);
    }

    private void initView() {

    }

    private void initListener() {

        findViewById(R.id.share_item_message).setOnClickListener(this);
        findViewById(R.id.share_item_email).setOnClickListener(this);
        findViewById(R.id.share_item_more).setOnClickListener(this);

        findViewById(R.id.share_item_copy).setOnClickListener(this);

        findViewById(R.id.share_btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_item_message:
                sendMessage();
                break;

            case R.id.share_item_email:
                sendEmailIntent();
                break;

            case R.id.share_item_more:

                break;

            case R.id.share_item_copy:
                copy();
                break;

            case R.id.share_btn_cancel:
                break;
        }

        dismiss();
    }

    private void sendMessage(){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", mShareContent);
        sendIntent.setType("vnd.android-dir/mms-sms");
        mContext.startActivity(sendIntent);
    }

    private void sendEmailIntent(){
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto: "));
//        data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
        data.putExtra(Intent.EXTRA_TEXT, mShareContent);
        mContext.startActivity(data);
    }

    private void openMap() {
        Uri mUri = null;
        ZLocation location = LocationMgr.getInstance().getLocation();
        if (location == null) {
            return;
        } else {
            mUri = Uri.parse("geo:" + location.getLocation().getLatitude() + "," + location.getLocation().getLongitude());
        }
        Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
        mContext.startActivity(mIntent);

//        try {
//            Intent in = new Intent();
//            in.setAction(Intent.ACTION_MAIN);
//            in.addCategory(Intent.CATEGORY_APP_MAPS);
//            mContext.startActivity(in);
//        }catch (Exception e){
//            LogUtil.e("excption", e.getLocalizedMessage());
//        }
    }


    public void copy() {
        ClipboardManager myClipboard = (ClipboardManager) mContext.getSystemService(mContext.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", mShareContent);
        myClipboard.setPrimaryClip(myClip);
        UIHelper.toastMessage(mContext, "Copied to the clipboard");
    }

}
