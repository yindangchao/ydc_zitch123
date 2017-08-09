package com.vanpro.zitech125.daemon.service;

import android.annotation.*;
import android.app.job.*;
import android.content.*;
import android.os.*;

import com.vanpro.zitech125.MyApplication;
import com.vanpro.zitech125.bluetooth.BluetoothLeService;

/**
 * Android 5.0+ 使用的 JobScheduler.
 * 运行在 :watch 子进程中.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        startService(new Intent(MyApplication.getInstance(), WorkService.class));
        startService(new Intent(MyApplication.getInstance(), BluetoothLeService.class));
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
