package com.vanpro.zitech125.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.ui.activity.MainActivity;
import com.vanpro.zitech125.ui.extend.BaseFragment;
import com.vanpro.zitech125.ui.widget.StartRadarView;
import com.vanpro.zitech125.util.AppDataManager;

/**
 * Created by HANER on 15/04
 */
public class StartFragment extends BaseFragment {

    ImageView mStartImg1;
    StartRadarView mStartImg2;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_start;
    }

    protected void initView() {
        mStartImg1 = (ImageView) findViewById(R.id.start_image_1);
        mStartImg1.setVisibility(View.VISIBLE);
        mStartImg2 = (StartRadarView) findViewById(R.id.start_image_2);
        mStartImg2.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {

    }

    protected void initData() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mStartImg1.setVisibility(View.GONE);
//                mStartImg2.setVisibility(View.VISIBLE);
//                mStartImg2.start();
//            }
//        }, 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(AppDataManager.getInstance().getBoolean(AppDataManager.KEY.HAS_SHOW_GUIDE)){
                    toMain();
                }else{
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.content_view, new GuideFragment());
                    ft.commitAllowingStateLoss();

                    AppDataManager.getInstance().setData(AppDataManager.KEY.HAS_SHOW_GUIDE,true);
                }

            }
        }, 1000);//2500有旋转动画时
    }

    private void toMain(){
        if(getActivity() == null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getActivity(), MainActivity.class));
                onTransitionAnim();
                getActivity().finish();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
