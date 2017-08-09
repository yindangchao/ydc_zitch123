package com.vanpro.zitech125.ui.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.adapter.CommonPageAdapter;
import com.vanpro.zitech125.ui.extend.CustomToolbarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jinsen on 16/7/8.
 */
public class UseDescActivity extends CustomToolbarActivity{

    private ViewPager viewPager;
    private List<View> imgsList;
    private LinearLayout viewgroup;
    private ImageView[] points;

    TextView mOkBtn;

    /* 只需在此处定义图片数量即可完成更新启动图 */
    private int[] imgRes = {
            R.drawable.use_desc_1,
            R.drawable.use_desc_2,
            R.drawable.use_desc_3,
            R.drawable.use_desc_4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_desc_layout);
        setTitle(R.string.setting_use_desc);
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.vp_images);
        viewgroup = (LinearLayout) findViewById(R.id.viewGroup);

        //创建导航图标
        points = new ImageView[imgRes.length];
        imgsList = new ArrayList<View>();
        //创建图片组
        for (int i = 0; i < imgRes.length; i++) {
            ImageView img = new ImageView(this);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageResource(imgRes[i]);
            imgsList.add(img);

            points[i] = new ImageView(this);
            points[i].setBackgroundResource(R.drawable.point_on);
            if (i != 0) {
                points[i].setBackgroundResource(R.drawable.point_normal);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(getResources().getDimensionPixelOffset(R.dimen.dp_10), 0, 0, 0);
                points[i].setLayoutParams(params);
            }
            viewgroup.addView(points[i]);
        }
        //给最后一张图片添加点击事件
        //点击该图片后跳转到下一个界面
        imgsList.get(imgsList.size() - 1).setOnClickListener(this);

        mOkBtn = (TextView) findViewById(R.id.use_desc_ok_btn);
        mOkBtn.setVisibility(View.GONE);

        viewPager.setAdapter(new CommonPageAdapter(imgsList));
    }

    @Override
    public void setLisetener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private boolean isLastPager = false;
            private int trys = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //LogUtil.w(TAG, "current position = "+position);
            }

            @Override
            public void onPageSelected(int position) {
                isLastPager = viewPager.getAdapter().getCount() == position + 1;

                //更新导航点状态
                int index = position % points.length;
                points[index].setBackgroundResource(R.drawable.point_on);
                for (int i = 0; i < points.length; i++) {
                    if (index != i) {
                        points[i].setBackgroundResource(R.drawable.point_normal);
                    }
                }

                if(isLastPager) {
                    mOkBtn.setVisibility(View.VISIBLE);
                    mOkBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    mOkBtn.getPaint().setAntiAlias(true);//抗锯齿
                }else
                    mOkBtn.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (isLastPager && state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //LogUtil.d(TAG, "the last item");
                    if (trys > 0);
                        //LogUtil.d(TAG, "Go to next Activity");
                    else
                        trys++;
                }
            }
        });

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.removeAllViews();
        viewPager.setAdapter(null);
        viewPager = null;
    }
}
