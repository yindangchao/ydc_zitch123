package com.vanpro.zitech125.ui.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.activity.MainActivity;
import com.vanpro.zitech125.ui.adapter.CommonPageAdapter;
import com.vanpro.zitech125.ui.extend.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jinsen on 16/04/8
 */
public class GuideFragment extends BaseFragment implements View.OnClickListener{

    private ViewPager viewPager;
    private List<View> imgsList;
    private LinearLayout viewgroup;
    private ImageView[] points;
    private View start;

    /* 只需在此处定义图片数量即可完成更新启动图 */
    private int[] imgRes = {
            R.drawable.pic_guide_1,
            R.drawable.pic_guide_2,
            R.drawable.pic_guide_3,
            R.drawable.pic_guide_4
    };

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.vp_images);
        viewgroup = (LinearLayout) findViewById(R.id.viewGroup);
        start = findViewById(R.id.guide_open_btn);
        start.setVisibility(View.GONE);
        //创建导航图标
        points = new ImageView[imgRes.length];
        imgsList = new ArrayList<View>();
        //创建图片组
        for (int i = 0; i < imgRes.length; i++) {
            ImageView img = new ImageView(getActivity());
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageResource(imgRes[i]);
            imgsList.add(img);

            points[i] = new ImageView(getActivity());
            points[i].setBackgroundResource(R.drawable.point_on);
            if (i != 0) {
                points[i].setBackgroundResource(R.drawable.point_normal);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_10), 0, 0, 0);
                points[i].setLayoutParams(params);
            }
            viewgroup.addView(points[i]);
        }
        //给最后一张图片添加点击事件
        //点击该图片后跳转到下一个界面
        imgsList.get(imgsList.size() - 1).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        viewPager.setAdapter(new CommonPageAdapter(imgsList));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_guide_layout;
    }

    protected void initListener() {
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

                if(isLastPager)
                    start.setVisibility(View.VISIBLE);
                else
                    start.setVisibility(View.GONE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (isLastPager && state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                    //LogUtil.d(TAG, "the last item");
//                    if (trys > 0)
//                        //LogUtil.d(TAG, "Go to next Activity");
//                        direct();
//                    else
//                        trys++;
//                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direct();
            }
        });
    }

    @Override
    public void onClick(View v) {
        direct();
    }

    /**
     * 显示App介绍页面之后跳转到不同的Activity
     */
    private void direct() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.removeAllViews();
        viewPager.setAdapter(null);
        viewPager = null;
        System.gc();
    }
}
