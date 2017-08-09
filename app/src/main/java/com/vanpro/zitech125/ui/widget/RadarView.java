package com.vanpro.zitech125.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * 雷达搜索
 *
 * Created by Jinsen on 16/pic_guide_2/8.
 */
public class RadarView extends View{

    private Paint mPaintLine, mPaintCircle, mPaintBoldLine;
    private int w, h;
    // 动画
    private Matrix matrix;
    // 旋转角度
    private int start;

    RectF mRectF ;

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
            RadarView.this.invalidate();
            // 继续循环
            handler.postDelayed(run, 10);
        }
    };

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        // 获取高宽
        w = attrs.getAttributeIntValue(android.R.attr.layout_width,0);
        h = attrs.getAttributeIntValue(android.R.attr.layout_height,0);
    }

    private void initView() {
        mPaintLine = new Paint();
        mPaintLine.setColor(Color.parseColor("#808080"));
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);

        mPaintBoldLine = new Paint();
        mPaintBoldLine.setColor(Color.parseColor("#808080"));
        mPaintBoldLine.setAntiAlias(true);
        mPaintBoldLine.setStyle(Paint.Style.STROKE);
        mPaintBoldLine.setStrokeWidth(10);

        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.parseColor("#60D86B1C"));
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.FILL);



        matrix = new Matrix();
    }


    private void reset(){
        start = 0;
        matrix = new Matrix();
        // 参数：旋转角度，围绕点坐标的x,y坐标点
        matrix.postRotate(start, w / 2, h / 2);
        // 刷新重绘
        RadarView.this.invalidate();
    }

    public void start(){
        reset();
        handler.post(run);
    }

    public void stop(){
        handler.removeCallbacks(run);
        reset();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        w = View.MeasureSpec.getSize(widthMeasureSpec);
        h = View.MeasureSpec.getSize(widthMeasureSpec);

        mRectF = new RectF();
        mRectF.set(0, 0, w, h);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        PathEffect effects = new DashPathEffect(new float[]{60,8,60,8},1);
//        mPaintBoldLine.setPathEffect(effects);

        // 画四个圆形
        canvas.drawCircle(w / 2, h / 2, (w - 20)/ 2 , mPaintBoldLine);
        canvas.drawCircle(w / 2, h / 2, w *3 / 8, mPaintLine);
        canvas.drawCircle(w / 2, h / 2, w / 4, mPaintLine);
        canvas.drawCircle(w / 2, h / 2, w / 8, mPaintBoldLine);




        // 增加旋转动画，使用矩阵实现
        canvas.concat(matrix); // 前置动画

        canvas.drawArc(mRectF,start-30,60,true,mPaintCircle);

    }


}
