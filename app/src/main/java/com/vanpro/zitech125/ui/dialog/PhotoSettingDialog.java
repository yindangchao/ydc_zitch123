package com.vanpro.zitech125.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.event.PhotoMenuActionEvent;
import com.vanpro.zitech125.util.AppDataManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Jinsen on 16/7/6.
 */
public class PhotoSettingDialog extends Dialog implements View.OnClickListener {

    View mSettingTipsView, mRetakeView, mDeleteView, mCancelView;

    View mTipsView;

    EditText mTipsInputEt;

    Context mContext;

    public PhotoSettingDialog(Context context) {
        this(context,R.style.dialogButtom);
    }

    public PhotoSettingDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog(context);
    }

    private void initDialog(Context context) {
        mContext = context;
        setContentView(R.layout.dialog_photo_menu_dialog);
        initView();
        initListener();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(-1, -2);
    }

    private void initView() {
        mTipsView = findViewById(R.id.photo_menu_tips_layout);
        mTipsView.setVisibility(View.GONE);
        mTipsInputEt = (EditText) findViewById(R.id.photo_menu_tips_editText);

        mSettingTipsView = findViewById(R.id.photo_menu_set_tips);
        mSettingTipsView.setVisibility(View.GONE);
        mRetakeView = findViewById(R.id.photo_menu_retake);
        mDeleteView = findViewById(R.id.photo_menu_delete);
        mCancelView = findViewById(R.id.photo_menu_cancel);
    }

    private void initListener() {
        mSettingTipsView.setOnClickListener(this);
        mRetakeView.setOnClickListener(this);
        mDeleteView.setOnClickListener(this);
        mCancelView.setOnClickListener(this);

        findViewById(R.id.photo_menu_tips_input_done).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_menu_retake:
                dismiss();
                EventBus.getDefault().post(new PhotoMenuActionEvent(PhotoMenuActionEvent.ACTION_RETAKE));

                break;

            case R.id.photo_menu_delete:
                dismiss();
                EventBus.getDefault().post(new PhotoMenuActionEvent(PhotoMenuActionEvent.ACTION_DELETE));

                break;

            case R.id.photo_menu_cancel:
                dismiss();
                break;

            case R.id.photo_menu_set_tips:
                showInputTipsView();
                break;

            case R.id.photo_menu_tips_input_done:
                String tips = mTipsInputEt.getText().toString();
                AppDataManager.getInstance().setData(AppDataManager.KEY.PHOTO_TIPS_PTAH,tips);
                dismiss();
                EventBus.getDefault().post(new PhotoMenuActionEvent(PhotoMenuActionEvent.ACTION_TIPS));
                break;
        }
    }

    private void showInputTipsView(){
        mTipsView.setVisibility(View.VISIBLE);
        mSettingTipsView.setVisibility(View.GONE);;
        mRetakeView.setVisibility(View.GONE);
        mDeleteView.setVisibility(View.GONE);
        mCancelView.setVisibility(View.GONE);

        String tips = AppDataManager.getInstance().getString(AppDataManager.KEY.PHOTO_TIPS_PTAH);
        mTipsInputEt.setText(tips);
    }
}
