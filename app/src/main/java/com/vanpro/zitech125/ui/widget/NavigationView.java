package com.vanpro.zitech125.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * 导航
 * <p>
 * Created by Jinsen on 16/4/8.
 */
public class NavigationView extends View {

    private Paint mPaintLine, mPaintCircle, mPaintBoldLine;
    private int w, h;
    // 动画
    private Matrix matrix;
    // 旋转角度
    private int start = -90;

    RectF mRectF;

    boolean isNavigation = false;

    int SHAPE_WIDTH = 40;
    int LINE_WIDTH = 10;

    // Handler定时动画
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {

        @Override
        public void run() {

            start = start + 1;
            matrix = new Matrix();
            // 参数：旋转角度，围绕点坐标的x,y坐标点
            matrix.postRotate(start, w / 2, h / 2);
            // 刷新重绘
            NavigationView.this.invalidate();
            // 继续循环
            handler.postDelayed(run, 10);
        }
    };

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        // 获取高宽
        w = attrs.getAttributeIntValue(android.R.attr.layout_width, 0);
        h = attrs.getAttributeIntValue(android.R.attr.layout_height, 0);
    }

    private void initView() {
        mPaintLine = new Paint();
        mPaintLine.setColor(Color.parseColor("#88808080"));
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(SHAPE_WIDTH);

        mPaintBoldLine = new Paint();
        mPaintBoldLine.setColor(Color.parseColor("#808080"));
        mPaintBoldLine.setAntiAlias(true);
        mPaintBoldLine.setStyle(Paint.Style.STROKE);
        mPaintBoldLine.setStrokeWidth(LINE_WIDTH);

        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.parseColor("#60D86B1C"));
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.FILL);


        matrix = new Matrix();
    }

    public void updateDirection(float roate){
        // 参数：旋转角度，围绕点坐标的x,y坐标点
        start = (int) roate;
//        matrix.postRotate(start, w / 2, h / 2);
        invalidate();
    }


    private void reset() {
        start = 0;
        matrix = new Matrix();
        // 参数：旋转角度，围绕点坐标的x,y坐标点
        matrix.postRotate(start, w / 2, h / 2);
        // 刷新重绘
        NavigationView.this.invalidate();
    }

    public void start() {
        reset();
        handler.post(run);
    }

    public void stop() {
        handler.removeCallbacks(run);
        reset();
    }

    public void setNavigation(boolean  isNavigation) {
        this.isNavigation = isNavigation;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(widthMeasureSpec);

        mRectF = new RectF();
        mRectF.set(0+SHAPE_WIDTH, 0+SHAPE_WIDTH, w - SHAPE_WIDTH, h - SHAPE_WIDTH);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        PathEffect effects = new DashPathEffect(new float[]{60, 8, 60, 8}, 1);
        mPaintBoldLine.setPathEffect(effects);

        RadialGradient lg=new RadialGradient(w / 2, h / 2,(w - SHAPE_WIDTH + 4) / 2,Color.TRANSPARENT,Color.parseColor("#D86B1C"), Shader.TileMode.MIRROR);
        mPaintCircle.setShader(lg);

//        if (!isNavigation) {
//            canvas.drawCircle(w / 2, h / 2, w / 8, mPaintLine);
//            mPaintCircle.setStyle(Paint.Style.FILL);
//            mPaintCircle.setStrokeWidth(LINE_WIDTH);
//        }else{
//            mPaintCircle.setStyle(Paint.Style.FILL);
//        }

        // 画四个圆形
        canvas.drawCircle(w / 2, h / 2, (w - SHAPE_WIDTH - SHAPE_WIDTH - LINE_WIDTH - LINE_WIDTH - 2) / 2, mPaintBoldLine);

        canvas.drawCircle(w / 2, h / 2, (w - SHAPE_WIDTH - SHAPE_WIDTH) / 2, mPaintLine);

        // 增加旋转动画，使用矩阵实现
        canvas.concat(matrix); // 前置动画

        if(isNavigation)
            canvas.drawArc(mRectF, start - 45, 90, true, mPaintCircle);

    }

}
