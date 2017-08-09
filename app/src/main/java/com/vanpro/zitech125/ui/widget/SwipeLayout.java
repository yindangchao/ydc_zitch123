package com.vanpro.zitech125.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.util.AndroidUtils;


/**
 * 手势滑动返回
 * <p/>
 * 只支持滑动左侧 返回
 * <p/>
 * Created by Jinsen on 16/1/13.
 */
public class SwipeLayout extends FrameLayout {

    //private View backgroundLayer;用来设置滑动时的背景色
    Drawable mLeftShadow;

    //触摸起始位置
    float mStartX = 0;
    float mStartY = 0;

    boolean mCanSwipe = false;
    /**
     * 超过了touchslop仍然没有达到没有条件，则忽略以后的动作
     */
    boolean mIgnoreSwipe = false;
    View mContent;
    Activity mActivity;
    /**
     * 左侧侧滑区域
     * 默认25DP
     */
    int mSideWidthInDP = 25;
    int mSideWidth = 72;
    int mScreenWidth = 1080;
    VelocityTracker mTracker;

    float downX;
    float downY;
    float lastX;
    float currentX;
    float currentY;

    int mTouchSlopDP = 30;
    int mTouchSlop = 60;

    /**
     * 是否支持触摸全屏支持手势
     * <p/>
     * 默认只支持左侧边缘
     */
    boolean mSwipeAnyWhere = false;

    /**
     * 是否支持手势滑动
     */
    boolean mSwipeEnabled = true;

    /**
     * 返回操作是否完成
     */
    boolean mSwipeFinished = false;

    private final int mDuration = 200;
    boolean hasIgnoreFirstMove;
    ObjectAnimator mAnimator;


    public SwipeLayout(Context context) {
        super(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmSwipeAnyWhere(boolean mSwipeAnyWhere) {
        this.mSwipeAnyWhere = mSwipeAnyWhere;
    }

    public boolean ismSwipeAnyWhere() {
        return mSwipeAnyWhere;
    }

    public void setmSwipeEnabled(boolean mSwipeEnabled) {
        this.mSwipeEnabled = mSwipeEnabled;
    }

    public boolean ismSwipeEnabled() {
        return mSwipeEnabled;
    }

    public boolean ismSwipeFinished() {
        return mSwipeFinished;
    }

    public void replaceLayer(Activity activity) {

        mLeftShadow = activity.getResources().getDrawable(R.drawable.shadow_left);
        mTouchSlop = (int) (mTouchSlopDP * activity.getResources().getDisplayMetrics().density);
        mSideWidth = (int) (mSideWidthInDP * activity.getResources().getDisplayMetrics().density);
        mActivity = activity;
        mScreenWidth = AndroidUtils.getScreenWidth(activity);

        setClickable(true);
        attchToActivity(activity);
    }

    //把Swipelayut添加到activity
    private void attchToActivity(Activity activity) {
        final ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
        mContent = root.getChildAt(0);
        ViewGroup.LayoutParams params = mContent.getLayoutParams();
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(-1, -1);
        root.removeView(mContent);
        this.addView(mContent, params2);
        root.addView(this, params);
    }

    @Override
    protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        final int shadowWidth = mLeftShadow.getIntrinsicWidth();
        int left = (int) (getContentX()) - shadowWidth;
        mLeftShadow.setBounds(left, child.getTop(), left + shadowWidth, child.getBottom());
        mLeftShadow.draw(canvas);
        return result;
    }


    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (mSwipeEnabled && !mCanSwipe && !mIgnoreSwipe) {
            if (mSwipeAnyWhere) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = ev.getX();
                        downY = ev.getY();
                        currentX = downX;
                        currentY = downY;
                        lastX = downX;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = ev.getX() - downX;
                        float dy = ev.getY() - downY;
                        if (dx * dx + dy * dy > mTouchSlop * mTouchSlop) {
                            if (dy == 0f || Math.abs(dx / dy) > 1) {
                                downX = ev.getX();
                                downY = ev.getY();
                                currentX = downX;
                                currentY = downY;
                                lastX = downX;
                                mCanSwipe = true;
                                mTracker = VelocityTracker.obtain();
                            } else {
                                mIgnoreSwipe = true;
                            }
                        }
                        break;
                }
            } else {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    mStartX = ev.getX();
                    mStartY = ev.getY();
                    downX = ev.getX();
                    downY = ev.getY();
                    currentX = downX;
                    currentY = downY;
                    lastX = downX;
                }

                if (mStartX < mSideWidth) {
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            float dx = ev.getX() - downX;
                            float dy = ev.getY() - downY;
                            if (dx * dx + dy * dy > mTouchSlop * mTouchSlop) {
                                if (dy == 0f || Math.abs(dx / dy) > 1) {
                                    downX = ev.getX();
                                    downY = ev.getY();
                                    currentX = downX;
                                    currentY = downY;
                                    lastX = downX;
                                    mCanSwipe = true;
                                    mTracker = VelocityTracker.obtain();
                                } else {
                                    mIgnoreSwipe = true;
                                }
                            }
                            break;
                    }
                }
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            mIgnoreSwipe = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mCanSwipe || super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (mCanSwipe) {
            mTracker.addMovement(event);
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    currentX = downX;
                    currentY = downY;
                    lastX = downX;
                    break;
                case MotionEvent.ACTION_MOVE:
                    currentX = event.getX();
                    currentY = event.getY();
                    float dx = currentX - lastX;
                    if (dx != 0f && !hasIgnoreFirstMove) {
                        hasIgnoreFirstMove = true;
                        dx = dx / dx;
                    }
                    if (getContentX() + dx < 0) {
                        setContentX(0);
                    } else {
                        setContentX(getContentX() + dx);
                    }
                    lastX = currentX;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mTracker.computeCurrentVelocity(10000);
                    mTracker.computeCurrentVelocity(1000, 20000);
                    mCanSwipe = false;
                    hasIgnoreFirstMove = false;
                    int mv = mScreenWidth / 200 * 1000;
                    if (Math.abs(mTracker.getXVelocity()) > mv) {
                        animateFromVelocity(mTracker.getXVelocity());
                    } else {
                        if (getContentX() > mScreenWidth / 3) {
                            animateFinish(false);
                        } else if (getContentX() > 0) {
                            animateBack(false);
                        } else {
                            super.onTouchEvent(event);
                        }
                    }
                    mTracker.recycle();
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }


    public void cancelPotentialAnimation() {
        if (mAnimator != null) {
            mAnimator.removeAllListeners();
            mAnimator.cancel();
        }
    }

    public void setContentX(float x) {
        int ix = (int) x;
        mContent.setX(ix);
        invalidate();
    }

    public float getContentX() {
        return mContent.getX();
    }


    /**
     * 弹回，不关闭，因为left是0，所以setX和setTranslationX效果是一样的
     *
     * @param withVel 使用计算出来的时间
     */
    private void animateBack(boolean withVel) {
        cancelPotentialAnimation();
        mAnimator = ObjectAnimator.ofFloat(this, "contentX", getContentX(), 0);
        int tmpDuration = withVel ? ((int) (mDuration * getContentX() / mScreenWidth)) : mDuration;
        if (tmpDuration < 100) {
            tmpDuration = 100;
        }
        mAnimator.setDuration(tmpDuration);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.start();
    }

    private void animateFinish(boolean withVel) {
        cancelPotentialAnimation();
        mAnimator = ObjectAnimator.ofFloat(this, "contentX", getContentX(), mScreenWidth);
        int tmpDuration = withVel ? ((int) (mDuration * (mScreenWidth - getContentX()) / mScreenWidth)) : mDuration;
        if (tmpDuration < 100) {
            tmpDuration = 100;
        }
        mAnimator.setDuration(tmpDuration);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!mActivity.isFinishing()) {
                    mSwipeFinished = true;
                    mActivity.finish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        mAnimator.start();
    }


    private void animateFromVelocity(float v) {
        if (v > 0) {
            if (getContentX() < mScreenWidth / 2 && v * mDuration / 1000 + getContentX() < mScreenWidth / 2) {
                animateBack(false);
            } else {
                animateFinish(true);
            }
        } else {
            if (getContentX() > mScreenWidth / 2 && v * mDuration / 1000 + getContentX() > mScreenWidth / 2) {
                animateFinish(false);
            } else {
                animateBack(true);
            }
        }

    }
}
