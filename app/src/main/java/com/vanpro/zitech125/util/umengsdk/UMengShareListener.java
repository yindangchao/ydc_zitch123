package com.vanpro.zitech125.util.umengsdk;

/**
 * Created by Neil.Yang on 2017/4/7.
 */

public interface UMengShareListener {

    void onStart(UMengUtils.SHARE_PLATFORM platform);


    void onResult(UMengUtils.SHARE_PLATFORM platform);


    void onError(UMengUtils.SHARE_PLATFORM platform, Throwable throwable);


    void onCancel(UMengUtils.SHARE_PLATFORM platform);
}
