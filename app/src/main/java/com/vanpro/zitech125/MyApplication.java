package com.vanpro.zitech125;

import android.app.Application;
import android.content.Intent;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.vanpro.zitech125.bluetooth.BluetoothLeService;
import com.vanpro.zitech125.constants.Constants;
import com.vanpro.zitech125.controller.BaseController;
import com.vanpro.zitech125.util.Config;

/**
 * Created by Jinsen on 16/1/21.
 */
public class MyApplication extends Application {
    private static MyApplication me = null;

    public MyApplication() {
        me = this;
    }

    public static MyApplication getInstance() {
        return me;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;

//        SDKInitializer.initialize(getApplicationContext());

        BaseController.init(this);
        Config.register(this);

        //我们现在需要服务运行, 将标志位重置为 false
//        WorkService.sShouldStopService = false;
//        startService(new Intent(this, WorkService.class));
        UMShareAPI.get(this);
//        initUmeng();
    }

    private void initUmeng() {
        //关闭默认的activity统计，这里需要统计fragment 和 activity
        MobclickAgent.openActivityDurationTrack(false);

        //微信 appid appsecret
        PlatformConfig.setWeixin(Constants.WECHAT_APP_ID, Constants.WECHAT_APP_KEY);
        // QQ和Qzone appid appkey
//        PlatformConfig.setQQZone(Constant.QQ_LOGIN_ID, Constant.QQ_LOGIN_KEY);
//        // SINA appid appkey
//        //Config.REDIRECT_URL = "http://www.heyhou.com/weibo_callback";
//        PlatformConfig.setSinaWeibo(Constant.SINA_LOGIN_KEY, Constant.SINA_LOGIN_ID, "http://www.heyhou.com/weibo_callback");
    }

    /**
     * 应用退出，由AppManager回调
     */
    public void onExit() {
        stopService(new Intent(this, BluetoothLeService.class));
    }


}
