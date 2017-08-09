package com.vanpro.zitech125.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vanpro.zitech125.R;


/**
 * Toolbar helper
 *
 * Created by Jinsen on 16/1/8.
 */
public class ToolbarHelper {

    private Context mContext;

    RelativeLayout mContentView;

    Toolbar mToolbar;

    View mUserView;

    ImageView mBottomLine;

    int mToolbarHeight = 0;

    public ToolbarHelper(Context context, int layoutResId){
        this.mContext = context;
        mToolbarHeight = mContext.getResources().getDimensionPixelSize(R.dimen.title_bar_height);
        buildContentView();
        createUserView(layoutResId);
        addTitleBottomLine();
        createToolbar();
    }

    private void buildContentView(){
        mContentView = new RelativeLayout(mContext);
//        mContentView.setFitsSystemWindows(true);
        mContentView.setBackgroundResource(R.color.common_bg);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
    }

    private void createToolbar(){
        mToolbar = new Toolbar(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                mToolbarHeight);
        mContentView.addView(mToolbar, params);
    }

    private void createUserView(int layoutResId){
        mUserView = LayoutInflater.from(mContext).inflate(layoutResId,null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.topMargin = mToolbarHeight;
        mContentView.addView(mUserView,params);
    }

    private void addTitleBottomLine(){
//        mBottomLine = new ImageView(mContext);
//        mBottomLine.setBackgroundResource(R.color.comm_divider);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                mContext.getResources().getDimensionPixelSize(R.dimen.dp_2));
//        params.topMargin = mToolbarHeight;
//        mContentView.addView(mBottomLine,params);
    }

    public void setTitleBottomLineHeight(int height){
        if(mBottomLine != null){
            mBottomLine.getLayoutParams().height = height;
        }
    }

    public void setTitleBottomLineColor(int color){
        if(mBottomLine != null){
            mBottomLine.setBackgroundColor(color);
        }
    }

    public View getContentVew(){
        return mContentView;
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public final void resetTitleTop(int top){
        ((RelativeLayout.LayoutParams)mToolbar.getLayoutParams()).topMargin = top;
        if(mBottomLine != null) {
            RelativeLayout.LayoutParams bottomlineLp = (RelativeLayout.LayoutParams) mBottomLine.getLayoutParams();
            bottomlineLp.topMargin = bottomlineLp.topMargin + top;
        }
        if(mUserView != null) {
            RelativeLayout.LayoutParams userViewLp = (RelativeLayout.LayoutParams) mUserView.getLayoutParams();
            userViewLp.topMargin = userViewLp.topMargin + top;
        }
    }

}