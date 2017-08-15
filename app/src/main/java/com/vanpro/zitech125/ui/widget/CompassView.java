package com.vanpro.zitech125.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vanpro.zitech125.R;

/**
 * Created by Jinsen on 16/7/14.
 */
public class CompassView extends RelativeLayout {

    ImageView mLineBgIv;

    ImageView mArrowLineIv;
    ImageView mArrowSectorIv;
    ImageView mArrowPointIv;

    int mState = 0;

    float mDistance;
    float mRotation;

    AnimationDrawable mArrowSectorAd = null;
//    private RelativeLayout layoutLineIv;
//    private RelativeLayout layoutArrowSeltor;
//    private RelativeLayout layoutPointIv;

    public CompassView(Context context) {
        super(context);
        initView(context);
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        inflate(context, R.layout.view_compass_layout, this);
        mLineBgIv = (ImageView) findViewById(R.id.view_compass_bg_line);
//        layoutLineIv = (RelativeLayout) findViewById(R.id.layout_compass_arrow_line);
//        layoutArrowSeltor = (RelativeLayout) findViewById(R.id.layout_compass_arrow_sector);
//        layoutPointIv = (RelativeLayout) findViewById(R.id.layout_compass_arrow_point);

        mArrowLineIv = (ImageView) findViewById(R.id.view_compass_arrow_line);
        mArrowSectorIv = (ImageView) findViewById(R.id.view_compass_arrow_sector);
        mArrowPointIv = (ImageView) findViewById(R.id.view_compass_arrow_point);
    }

    private void setState(int state){
        if(mState != state) {
            mState = state;
            switch (state) {
                case CompassState.CONNECTED:
//                    hideArrowView();
//                    mLineBgIv.setImageResource(R.drawable.compass_checked_line_bg);
                    resetRotation();
                    showArrowView();
                    mLineBgIv.setImageResource(R.drawable.compass_bg_line);
                    break;

                case CompassState.DIRECTION:
                    showArrowView();
                    mLineBgIv.setImageResource(R.drawable.compass_bg_line);
                    break;

                case CompassState.NEARBY:
                    hideArrowView();
                    mLineBgIv.setImageResource(R.drawable.compass_checked_line_bg);
//                    mLineBgIv.setImageResource(R.drawable.compass_bg_line);
//                    resetRotation();
//                    showArrowView();
                    break;
            }
        }
        directionView();
        setCompassArrow();
    }
    private void resetRotation(){
        mArrowLineIv.setImageResource(R.drawable.compass_arrow_line);
        mArrowSectorIv.setImageResource(R.drawable.compass_arrow_sector_b);
        mArrowSectorAd = (AnimationDrawable) mArrowSectorIv.getDrawable();
        mArrowSectorAd.stop();
            mArrowLineIv.setRotation(0);
            mArrowSectorIv.setRotation(0);
            mArrowPointIv.setRotation(0);
        mArrowPointIv.setVisibility(GONE);
    }
    private void showArrowView(){
        mArrowLineIv.setVisibility(VISIBLE);
        mArrowPointIv.setVisibility(VISIBLE);
        mArrowSectorIv.setVisibility(VISIBLE);
    }

    private void hideArrowView(){
        mArrowLineIv.setVisibility(GONE);
        mArrowPointIv.setVisibility(GONE);
        mArrowSectorIv.setVisibility(GONE);
    }


    private void directionView(){
        if(mState != CompassState.DIRECTION)
            return;

        if(mDistance > 20){
            mArrowLineIv.setImageResource(R.drawable.compass_arrow_line_s);
            if(mSectorDrawableState == 1){
                return;
            }else{
                mSectorDrawableState = 1;
                if(mArrowSectorAd != null)
                    mArrowSectorAd.stop();
                mArrowSectorIv.setImageResource(R.drawable.compass_arrow_sector_s);
                mArrowSectorAd = (AnimationDrawable) mArrowSectorIv.getDrawable();
                mArrowSectorAd.start();

            }
        }else if(mDistance > 5){
            mArrowLineIv.setImageResource(R.drawable.compass_arrow_line);
            if(mSectorDrawableState == 2){
                return;
            }else{
                mSectorDrawableState = 2;

                if(mArrowSectorAd != null)
                    mArrowSectorAd.stop();
                mArrowSectorIv.setImageResource(R.drawable.compass_arrow_sector_b);
                mArrowSectorAd = (AnimationDrawable) mArrowSectorIv.getDrawable();
                mArrowSectorAd.start();
            }
        }else{
            setState(CompassState.NEARBY);
        }
    }

    /**
     * 0 初始状态
     * 1 大区域
     * 2 小区域
     */
    int mSectorDrawableState = 0;


    private void setCompassArrow(){
        if(mState == CompassState.DIRECTION){
//            mArrowLineIv.setRotation(mRotation);
//            mArrowSectorIv.setRotation(mRotation);
//            mArrowPointIv.setRotation(mRotation);
            mArrowLineIv.setRotation(mRotation);
            mArrowSectorIv.setRotation(mRotation);
            mArrowPointIv.setRotation(mRotation);
        }
    }



    /**
     * 连接中
     */
    public void connected(){
        setState(CompassState.CONNECTED);
    }

    /**
     * 寻车中，就离车距大于5米
     */
    public void direction(){
        setState(CompassState.DIRECTION);
    }

    /**
     * 车就在附近
     */
    public void isNearby(){
        setState(CompassState.NEARBY);
    }

    boolean hasRunAnim = false;

    /**
     * 设置罗盘指向
     * @param rotation
     */
    public void updateRotation(float rotation) {

//        mArrowLineIv.clearAnimation();
//        mArrowSectorIv.clearAnimation();
//        mArrowPointIv.clearAnimation();

//        if(hasRunAnim)
//            return;
//
//        hasRunAnim = true;
//
//        RotateAnimation anim = new RotateAnimation(mRotation,rotation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        anim.setDuration(300);
//        anim.setFillAfter(true);
//
//        mArrowLineIv.startAnimation(anim);
//        mArrowSectorIv.startAnimation(anim);
//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                hasRunAnim = false;
//                setCompassArrow();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mArrowPointIv.startAnimation(anim);

//        RotateAnimation ra = new RotateAnimation(currentDegree, -rotation,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        //旋转过程持续时间
//        ra.setDuration(200);
////        //罗盘图片使用旋转动画
//        mArrowLineIv.startAnimation(ra);
//        mArrowSectorIv.startAnimation(ra);
//        mArrowPointIv.startAnimation(ra);

        mRotation = rotation;
        setCompassArrow();
    }

    /**
     * 设置 距离
     * @param distance
     */
    public void setDistance(float distance) {
        mDistance = distance;
        directionView();
    }

    public void setUnit(String unit) {

    }


    public class CompassState {
        /**
         * 连接中
         */
        public static final int CONNECTED = 1;

        /**
         * 找车中 距离车位置 大于5米以上
         */
        public static final int DIRECTION = 2;

        /**
         * 车就在周围 5米以内
         */
        public static final int NEARBY = 3;
    }
}
