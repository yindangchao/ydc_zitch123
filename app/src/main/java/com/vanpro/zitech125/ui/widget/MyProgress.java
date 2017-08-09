package com.vanpro.zitech125.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RemoteViews.RemoteView;

@RemoteView
public class MyProgress extends View {
    // ==========================================================================
    // Constants
    // ==========================================================================
    private  static final int DEFAULT_MAX_VALUE = 100;							// 默认进度条最大值
    private  static final int DEFAULT_PAINT_WIDTH = 10;							// 默认画笔宽度
    private  static final int DEFAULT_PROGRESS_PAINT_COLOR = 0xFF6430;		// 默认增量进度条画笔颜色
    private  static final int DEFAULT_INITIALPROGRESS_PAINT_COLOR = 0xFF6430;	// 默认本包进度条颜色
    private  static final int DEFAULT_TEXT_PAINT_COLOR = 0xff485258;			// 默认文本颜色
    private  static final boolean DEFAULT_FILL_MODE = false;					// 默认填充模式
    private  static final int DEFAULT_INSIDE_VALUE = 10;						// 默认缩进距离,缩进距离最好等于画笔宽度
    private  static final int DEFAULT_DRAW_DEGREE = -180;						// 默认起始的角度
    private  static final int DEFAULT_TEXT_SIZE = 16;							// 默认文本大小
    private static final int MAX_SMOOTH_ANIM_DURATION = 2000;					// 默认整个进度条平滑转完的时间
    public static final int STYLE_HORIZONTAL = 0;                               // 默认水平样式
    public static final int STYLE_CIRCLE = 1;                                   // 圆形样式
    // ==========================================================================
    // Fields
    // ==========================================================================
    private long mThreadId;						//当前线程

    private int mResBackground;					//背景资源id
    private Drawable mDrbBackground;			//背景Drawable
    private int mResForeground;					//前景资源id
    private Drawable mDrbForeground;			//前景Drawable

    private String mText;										//文本内容
    private Paint mTextPaint;									//文本画笔
    private int mTextSize = DEFAULT_TEXT_SIZE;					//文本大小
    private ColorStateList mTextColorStateList;					//文本颜色选择器
    private int mTextColor = DEFAULT_TEXT_PAINT_COLOR;			//文本颜色
    private Typeface mTypeface = Typeface.DEFAULT;				//文本字体
    private boolean mTextVisible = true;						//文本是否可见开关


    private int mMaxProgress = DEFAULT_MAX_VALUE;		// 进度条最大值
    private float mProgress = 0;						// 进度条当前值
    private float mInitialProgress = 0;					// 原始包进度值
    private float mSmoothProgress = 0;					// 平滑进度值
    private float mStartProgress = 0;					// 起始进度值
    private long mProgressSetTime;						// 平滑进度起始的时间
    private int mSmoothAnimDuration;					// 平滑时间
    private RectF mRawProgressBounds = new RectF();		// 圆形进度条所在的区域
    private StringBuilder mSb = new StringBuilder(4);

    private boolean mInitialProgressEnabled = false;

    private OnProgressChangeListener mOnProgressChangeListener;

    public boolean mBRoundPaintsFill = DEFAULT_FILL_MODE;            // 是否填充以填充模式绘制圆形
    public int mSidePaintInterval = DEFAULT_INSIDE_VALUE;			// 圆形向里缩进的距离
    public int mPaintWidth = DEFAULT_PAINT_WIDTH;					// 圆形画笔宽度（填充模式下无视）
    public int mDrawPos = DEFAULT_DRAW_DEGREE;						// 绘制圆形的起点（默认为-180度即9点钟方向）
    public Style mProgressPaintStyle = Style.STROKE;			// 进度条画笔样式
    public Paint mProgressPaints;									// 增量进度条画笔
    public Paint mInitialProgressPaint;    							// 本包进度条画笔
    public int mMaxSmoothTime = MAX_SMOOTH_ANIM_DURATION;			// 进度条转完的最大时间

    private int mProgressColor = DEFAULT_PROGRESS_PAINT_COLOR;						//增量进度条颜色资源id
    private int mInitialProgressColor = DEFAULT_INITIALPROGRESS_PAINT_COLOR;		//本包进度条颜色资源id

    private int mProgressDrbMinWidth = 0;

    private int style = STYLE_HORIZONTAL;
    // ==========================================================================
    // Constructors
    // ==========================================================================
    public MyProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public MyProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MyProgress(Context context) {
        super(context);
        init();
    }
    // ==========================================================================
    // Getters
    // ==========================================================================
    public synchronized float getProgress() {
        return mProgress;
    }
    public float getMax() {
        return mMaxProgress;
    }
    // ==========================================================================
    // Setters
    // ==========================================================================
    //复原进度条
    public void recover(){
        mMaxProgress = DEFAULT_MAX_VALUE;
        mProgress = 0;
        mInitialProgress = 0;
        mSmoothProgress = 0;
        mStartProgress = 0;
        invalidateSafe();
    }
    //设置背景资源
    public void setProgressBackgroundResource(int resId) {
        if (mResBackground == resId) {
            return;
        }
        mResBackground = resId;
        try {
            mDrbBackground = getResources().getDrawable(resId);
            if (null != mDrbBackground) {
                mDrbBackground.setBounds(0, 0, getWidth(), getHeight());
            }
        } catch (Exception e) {
            mDrbBackground = null;
            mResBackground = -1;
        }
        invalidateSafe();
    }
    //设置前景资源
    public void setProgressForegroundResource(int resId) {
        if (mResForeground == resId) {
            return;
        }
        mResForeground = resId;
        try {
            mDrbForeground = getResources().getDrawable(resId);
        } catch (Exception e) {
            mDrbForeground = null;
            mResForeground = -1;
        }
        invalidateSafe();
    }
    //设置增量进度条颜色
    public void setProgressColor(int color){
        if(color == mProgressColor){
            return;
        }
        mProgressColor = color;
        mProgressPaints.setColor(mProgressColor);
        invalidateSafe();

    }
    //设置本包更新进度条颜色
    public void setInitialProgressColor(int color){
        if(color == mInitialProgressColor){
            return;
        }
        mInitialProgressColor = color;
        mInitialProgressPaint.setColor(mInitialProgressColor);
        invalidateSafe();
    }
    //设置进度条画笔宽度
    public void setProgressPaintWidth(int width){
        if(width == mPaintWidth){
            return;
        }
        mPaintWidth = width;
        mInitialProgressPaint.setStrokeWidth(mPaintWidth);
        mProgressPaints.setStrokeWidth(mPaintWidth);
        mSidePaintInterval = mPaintWidth;
        invalidateSafe();
    }
    //设置进度条画笔风格
    //样式有Paint.Style.STROKE(空心)  Paint.Style.FILL(实心) Paint.Style.FILL_AND_STROKE(实心和空心)
    public void setProgressPaintStyle(Style style){
        if(style == mProgressPaintStyle){
            return;
        }
        mProgressPaintStyle = style;
        mInitialProgressPaint.setStyle(style);
        mProgressPaints.setStyle(style);
        invalidateSafe();
    }
    //是否填充圆
    public void setPaintFill(boolean fill){
        if(fill == mBRoundPaintsFill){
            return;
        }
        mBRoundPaintsFill = fill;
        invalidateSafe();
    }

    //设置是否有本包
    public void setInitialProgressEnabled(boolean enabled) {
        mInitialProgressEnabled = enabled;
        invalidateSafe();
    }
    // 设置最小进度条，可以用于两个进度条颜色交汇时重叠的区域
    public void setMinProgressWidth(int minWidth) {
        mProgressDrbMinWidth = minWidth;
        invalidateSafe();
    }
    // 设置最大进度条
    public void setMax(int max) {
        if (max > 0) {
            mMaxProgress = max;
        }
    }
    // 设置进度条平滑转完一周的时间
    public void setMaxSmoothTime(int time){
        if(time > 0){
            mMaxSmoothTime = time;
        }
    }
    // 设置当前进度值，如果是int型，需要除以最大值
    public void setProgress(int progress) {
        setProgress(progress, false);
    }
    // 浮点型为已经除以最大值的结果
    public void setProgress(float progress) {
        setProgress(progress, false);
    }

    public void setProgress(int progress, boolean smooth) {
        setProgress(progress / (float) mMaxProgress, smooth);
    }
    //设置本包大小
    public void setInitialProgress(int progress) {
        setInitialProgress(progress / (float) mMaxProgress);
    }
    //设置本包所占比例
    public void setInitialProgress(float progress) {
        if (progress >= 0) {
            mInitialProgress = progress;
            if (mSmoothProgress < mInitialProgress) {
                setProgress(progress, false);
            }
            invalidateSafe();
        }
    }
    //设置当前进度条
    public synchronized void setProgress(float progress, boolean smooth) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 1) {
            progress = 1;
        }
        mProgress = progress;
        mText = null;
        mProgressSetTime = System.currentTimeMillis();
        if (smooth) {
            mSmoothAnimDuration = (int) (mMaxSmoothTime * (1 - mProgress));
        } else {
            mSmoothAnimDuration = 0;
            mSmoothProgress = mProgress;
        }
        mStartProgress = mSmoothProgress;
        invalidateSafe();
    }
    //设置文本大小
    public void setProgressTextSize(int px) {
        mTextSize = px;
    }

    //设置文本颜色选择器
    public void setProgressTextColor(ColorStateList color) {
        mTextColorStateList = color;
        mTextColor = mTextColorStateList.getColorForState(getDrawableState(), mTextColor);
    }
    //设置文本颜色
    public void setProgressTextColor(int color) {
        mTextColorStateList = null;
        mTextColor = color;
    }
    //设置文本字体
    public void setProgressTextFont(Typeface typeFace) {
        mTypeface = typeFace;
    }
    //设置文本是否可见
    public void setProgressTextVisible(boolean visible) {
        mTextVisible = visible;
    }
    //设置文本
    public void setCenterText(String text) {
        mText = text;
    }
    //设置进度条改变的监听者
    public void setOnProgressChangeListener(OnProgressChangeListener l) {
        mOnProgressChangeListener = l;
    }
    //设置进度条的样式
    public void setStyle(int style) {
        if(style == STYLE_CIRCLE){
            this.style = STYLE_CIRCLE;
        }else if(style == STYLE_HORIZONTAL){
            this.style = STYLE_HORIZONTAL;
        }
    }
    // ==========================================================================
    // Methods
    // ==========================================================================
    //设置默认参数
    private void init()
    {
        mProgressPaints = new Paint();
        mProgressPaints.setAntiAlias(true);
        mProgressPaints.setStyle(Style.FILL);
//        mProgressPaints.setStyle(Style.STROKE);
        mProgressPaints.setStrokeWidth(mPaintWidth);
        mProgressPaints.setColor(mProgressColor);

        mInitialProgressPaint = new Paint();
        mInitialProgressPaint.setAntiAlias(true);
        mInitialProgressPaint.setStyle(Style.FILL);
//        mInitialProgressPaint.setStyle(Style.STROKE);
        mInitialProgressPaint.setStrokeWidth(mPaintWidth);
        mInitialProgressPaint.setColor(mInitialProgressColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTypeface(mTypeface);
        mTextPaint.setTextAlign(Align.CENTER);

        this.setFocusable(false);
        this.setClickable(false);
        this.setFocusableInTouchMode(false);
        mThreadId = Process.myTid();
    }

    private void invalidateSafe() {
        if (mThreadId == Process.myTid()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
    //通知进度条监听者
    private void notifyProgressChange(float smoothProgress, float targetProgress) {
        if (null != mOnProgressChangeListener) {
            mOnProgressChangeListener.onProgressChange(smoothProgress, targetProgress);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = mDrbBackground == null ? 0 : mDrbBackground.getIntrinsicWidth();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mDrbBackground == null ? 0 : mDrbBackground.getIntrinsicHeight();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        if (null != mDrbBackground) {
            mDrbBackground.setBounds(0, 0, width, height);
        }
        if(style == STYLE_CIRCLE){
            mRawProgressBounds.set(getPaddingLeft()+mSidePaintInterval, getPaddingTop()+mSidePaintInterval, width - getPaddingRight()-mSidePaintInterval,
                    height - getPaddingBottom()-mSidePaintInterval);
        }else{
            mRawProgressBounds.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(),
                    height - getPaddingBottom());
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float factor;
        if (mProgress == 0 || mProgress == 1) {
            factor = 1;
        } else {
            long elapsed = System.currentTimeMillis() - mProgressSetTime;
            if (elapsed < 0) {
                factor = 0;
            } else if (elapsed > mSmoothAnimDuration) {
                factor = 1;
            } else {
                factor = elapsed / (float) mSmoothAnimDuration;
            }
        }
        mSmoothProgress = mStartProgress + factor * (mProgress - mStartProgress);
        // 画背景
        if (null != mDrbBackground) {
            mDrbBackground.draw(canvas);
        }

        // 画进度条
        if(mInitialProgressEnabled){
            if (mInitialProgress > 0) {
                if(style == STYLE_CIRCLE){
                    canvas.drawArc(mRawProgressBounds, mDrawPos, 360*mInitialProgress , mBRoundPaintsFill, mInitialProgressPaint);
                }else if(style == STYLE_HORIZONTAL){
                    canvas.drawRect(new RectF(mRawProgressBounds.left, mRawProgressBounds.top, mRawProgressBounds.right *  mInitialProgress, mRawProgressBounds.bottom),mInitialProgressPaint);
                }
            }
            if (mSmoothProgress != 0){
                if(style == STYLE_CIRCLE){
                    canvas.drawArc(mRawProgressBounds, mDrawPos + 360*mInitialProgress, 360*(mSmoothProgress-mInitialProgress), mBRoundPaintsFill, mProgressPaints);
                }else if(style == STYLE_HORIZONTAL){
                    canvas.drawRect(new RectF(mRawProgressBounds.right * mInitialProgress, mRawProgressBounds.top, mRawProgressBounds.right *  (mSmoothProgress-mInitialProgress), mRawProgressBounds.bottom),mProgressPaints);
                }
            }
        }else {
            if(style == STYLE_CIRCLE){
                canvas.drawArc(mRawProgressBounds, mDrawPos, 360*mSmoothProgress , mBRoundPaintsFill, mProgressPaints);
            }else if(style == STYLE_HORIZONTAL){
                canvas.drawRect(new RectF(mRawProgressBounds.left, mRawProgressBounds.top, mRawProgressBounds.right *  mSmoothProgress, mRawProgressBounds.bottom),mProgressPaints);
            }
        }
        // 画前景
        if (null != mDrbForeground && style == STYLE_CIRCLE) {//横向进度条无法画前景
            mDrbForeground.draw(canvas);
        }


        if (mTextVisible && style == STYLE_CIRCLE) {//横向进度条无法写进度
            mSb.delete(0, mSb.length());
            if (mText == null) {
                mSb.append((int) (mSmoothProgress * 100));
                mSb.append('%');
            } else {
                mSb.append(mText);
            }
            String text = mSb.toString();

            FontMetrics fm = mTextPaint.getFontMetrics();
            int fontH = (int) (Math.abs(fm.ascent));
            canvas.drawText(text, (getWidth() - getPaddingLeft() - getPaddingRight()) >> 1,
                    (getHeight() - getPaddingTop() - getPaddingBottom() >> 1) + (fontH >> 1) , mTextPaint);

        }

        if (factor != 1) {
            invalidateSafe();
        }
        notifyProgressChange(mSmoothProgress, mProgress);
    }

    @Override
    protected void drawableStateChanged() {
        int[] drawableState = getDrawableState();
        if (mDrbBackground != null && mDrbBackground.isStateful()) {
            mDrbBackground.setState(drawableState);
        }
        if (mDrbForeground != null && mDrbForeground.isStateful()) {
            mDrbForeground.setState(drawableState);
        }
        if (mTextColorStateList != null) {
            mTextColor = mTextColorStateList.getColorForState(drawableState, mTextColor);
        }
    }
    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================
    public interface OnProgressChangeListener {
        void onProgressChange(float smoothProgress, float targetProgress);
    }
}
