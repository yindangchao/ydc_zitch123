package com.vanpro.zitech125;

import android.app.Application;
import android.content.Intent;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.vanpro.zitech125.bluetooth.BluetoothLeService;
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
    }

    /**
     * 应用退出，由AppManager回调
     */
    public void onExit() {
        stopService(new Intent(this, BluetoothLeService.class));
    }


}
