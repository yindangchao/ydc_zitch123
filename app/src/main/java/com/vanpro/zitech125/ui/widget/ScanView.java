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
public class ScanView extends RelativeLayout {

    ImageView mOutShadeBgIv;
    ImageView mBthIconIv;

    ImageView mScanAnimIv;

    public ScanView(Context context) {
        super(context);
        initView(context);
    }

    public ScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        inflate(context, R.layout.view_scan_layout, this);
        mOutShadeBgIv = (ImageView) findViewById(R.id.view_scan_bg_line);
        mBthIconIv = (ImageView) findViewById(R.id.view_scan_bg_bt);

        mScanAnimIv = (ImageView) findViewById(R.id.view_scan_anim);
    }



    // 旋转角度
    private int start = 0;

    // Handler定时动画
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {

        @Override
        public void run() {

            start = start + 2;
            // 参数：旋转角度，围绕点坐标的x,y坐标点
            mScanAnimIv.setRotation(start);
            // 继续循环
            handler.postDelayed(run, 10);
        }
    };


    private void reset() {
        start = 0;
        // 刷新重绘
        mScanAnimIv.setRotation(start);
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
