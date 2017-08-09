package com.vanpro.zitech125.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vanpro.zitech125.R;

/**
 * Created by Jinsen on 16/7/14.
 */
public class StartRadarView extends RelativeLayout {

    ImageView mLineBgIv;
    ImageView mOutShadeBgIv;
    ImageView mBthIconIv;

    ImageView mArrowLine,mArrowSector, mArrowPoint;

    public StartRadarView(Context context) {
        super(context);
        initView(context);
    }

    public StartRadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StartRadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        inflate(context, R.layout.view_start_radar_layout, this);

        mArrowLine = (ImageView) findViewById(R.id.view_start_radar_arrow_line);
//        mArrowSector = (ImageView) findViewById(R.id.view_start_radar_arrow_sector);
        mArrowPoint = (ImageView) findViewById(R.id.view_start_radar_arrow_point);
    }



    // 旋转角度
    private int start = 0;

    // Handler定时动画
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {

        @Override
        public void run() {

            start = start + 9;
            // 参数：旋转角度，围绕点坐标的x,y坐标点
            mArrowLine.setRotation(start);
//            mArrowSector.setRotation(start);
            mArrowPoint.setRotation(start);
            // 继续循环
            if(start < 360)
                handler.postDelayed(run, 12);
        }
    };


    private void reset() {
        start = 0;
        // 刷新重绘
        mArrowLine.setRotation(start);
//        mArrowSector.setRotation(start);
        mArrowPoint.setRotation(start);
    }

    public void start() {
        reset();
        handler.post(run);
    }

    public void stop() {
        handler.removeCallbacks(run);
        reset();
    }
}
