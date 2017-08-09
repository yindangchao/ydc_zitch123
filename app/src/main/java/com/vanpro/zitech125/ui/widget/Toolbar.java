package com.vanpro.zitech125.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.vanpro.zitech125.R;


/**
 * 标题栏
 *
 * Created by Jinsen on 16/1/7.
 */
public class Toolbar extends android.support.v7.widget.Toolbar{

    ImageView mBackIv,mCloseIv;
    TextView mBackTx, mCloseTx;

    TextView mTitleTx;

    ImageView mActionIv;
    TextView mActionTx;


    public Toolbar(Context context) {
        this(context, null, 0);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.base_toolbar_layout, this);

//        setBackgroundResource(R.color.common_toolbar_bg);
        initView();
    }

    private void initView(){
        mBackIv = (ImageView) findViewById(R.id.toolbar_back_btn);
        mBackTx = (TextView) findViewById(R.id.toolbar_back_tx);

        mCloseIv = (ImageView) findViewById(R.id.toolbar_close_btn);
        mCloseTx = (TextView) findViewById(R.id.toolbar_close_tx);

        mTitleTx = (TextView) findViewById(R.id.toolbar_title);

        mActionIv = (ImageView) findViewById(R.id.toolbar_action_btn);
        mActionTx = (TextView) findViewById(R.id.toolbar_action_tx);

    }

    /**
     * 设置标题栏
     * @param title
     */
    public final void setTitle(String title){
        mTitleTx.setText(title);
    }

    /**
     * 设置标题栏
     * @param strRes
     */
    public final void setTitle(int strRes){
        mTitleTx.setText(strRes);
    }

    /**
     * 普通的标题栏 只有标题和返回按钮
     * @param title
     */
    public final void baseTitle(CharSequence title){
        mTitleTx.setText(title);
        mBackIv.setVisibility(VISIBLE);
    }

    public ImageView getBackBtn(){
        return mBackIv;
    }

    public TextView getTitleView(){
        return mTitleTx;
    }

    public TextView getActionTextView(){
        return mActionTx;
    }

    public void setBackText(String back){
        mBackIv.setVisibility(GONE);
        mBackTx.setVisibility(VISIBLE);
        mBackTx.setText(back);
    }

    public void setActionText(CharSequence text){
        mActionTx.setVisibility(VISIBLE);
        mActionIv.setVisibility(GONE);
        mActionTx.setText(text);
    }

    public void setActionText(int resId){
        mActionTx.setVisibility(VISIBLE);
        mActionIv.setVisibility(GONE);
        mActionTx.setText(resId);
    }

    public void setOnClickListener(OnClickListener linsener){
        mBackTx.setOnClickListener(linsener);
        mBackIv.setOnClickListener(linsener);
        mTitleTx.setOnClickListener(linsener);
        mCloseIv.setOnClickListener(linsener);
        mCloseTx.setOnClickListener(linsener);
        mActionIv.setOnClickListener(linsener);
        mActionTx.setOnClickListener(linsener);
    }

}
