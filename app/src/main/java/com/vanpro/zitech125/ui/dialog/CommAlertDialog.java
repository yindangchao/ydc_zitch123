package com.vanpro.zitech125.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanpro.zitech125.R;


/**
 * 通用提醒框
 * <p/>
 * Created by Jinsen on 2015/8/21.
 */
public class CommAlertDialog extends Dialog implements View.OnClickListener {

    Context mContext = null;

    TextView mTitle = null;
    TextView mMessage = null;

    TextView mRightBtn = null;
    TextView mLeftBtn = null;

    public enum Style {
        ONE_BUTTON,
        TWO_BUTTON
    }

    public CommAlertDialog(Context context) {
        this(context, R.style.TransparentDialog);
    }

    public CommAlertDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.dialog_alert_layout);

        mTitle = (TextView) findViewById(R.id.com_alert_title);
        mTitle.setVisibility(View.GONE);

        mMessage = (TextView) findViewById(R.id.com_alert_messge);
        mMessage.setVisibility(View.GONE);

        mRightBtn = (TextView) findViewById(R.id.com_alert_ok);
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setOnClickListener(this);

        mLeftBtn = (TextView) findViewById(R.id.com_alert_cancel);
        mLeftBtn.setVisibility(View.GONE);
        mLeftBtn.setOnClickListener(this);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 设置dialog的标题和内容
     *
     * @param title
     * @param msg
     */
    public CommAlertDialog setTitleAndMessage(String title, String msg) {
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
    public CommAlertDialog setTitle(String title) {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title);
        return this;
    }

    /**
     * 设置dialog的内容
     * 设置内容可见
     *
     * @param msg
     */
    public CommAlertDialog setMessage(String msg) {
        mMessage.setVisibility(View.VISIBLE);
        mMessage.setText(msg);
        return this;
    }

    /**
     * 设置确认按钮的名字和事件处理
     * 如果不设置 则显示默认的名字和默认的事件处理
     *
     * @param name
     * @param listener
     */
    public CommAlertDialog setRightBtn(String name, View.OnClickListener listener) {
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText(name);
        if (listener != null)
            mRightBtn.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置取消按钮的名字和事件处理
     * 如果不设置，则显示默认的名字和默认的事件处理
     *
     * @param name
     * @param listener
     */
    public CommAlertDialog setLeftBtn(String name, View.OnClickListener listener) {
        mLeftBtn.setVisibility(View.VISIBLE);
        mLeftBtn.setText(name);
        if (listener != null)
            mLeftBtn.setOnClickListener(listener);
        return this;
    }

    public boolean showDialog(Style style) {
        if (isShowing() || ((Activity) mContext).isFinishing()) {
            return false;
        }
        switch (style) {
            case ONE_BUTTON: {
                mLeftBtn.setVisibility(View.GONE);
                break;
            }
            case TWO_BUTTON: {
                mLeftBtn.setVisibility(View.VISIBLE);
                break;
            }
            default: {
                break;
            }
        }

        show();
        return true;
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
            case R.id.com_alert_ok:
                dismissDialog();
                break;
            case R.id.com_alert_cancel:
                dismissDialog();
                break;
        }
    }

    public TextView getTitle() {
        return mTitle;
    }

    public TextView getRightBtn() {
        return mRightBtn;
    }

    public TextView getLeftBtn() {
        return mLeftBtn;
    }
}
