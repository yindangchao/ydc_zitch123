package com.vanpro.data.core.http.util;

import android.util.Log;

import com.vanpro.zitech125.BuildConfig;


/**
 * Created by Administrator on 2015/3/30.
 */
public class LogUtil {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void d(String tag, String info){
        if(DEBUG)
            Log.e(tag,info);
    }
}
