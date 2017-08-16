package com.vanpro.zitech125.util.umengsdk;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.vanpro.zitech125.R;


/**
 * Created by Neil.Yang on 2017/3/20.
 */

public final class UMengUtils {

    private static final String TAG = "UMengUtils";

//    8.  EVENT_ID_SELECT_CATEGORY	        精选页面->分类
//    9.  EVENT_ID_SELECT_BANNER	        精选页面->广告banner
//    10. EVENT_ID_SELECT_SEARCH	        精选页面->搜索
//    11. EVENT_ID_SELECT_UPLAOD	        精选页面->上传
//    12. EVENT_ID_TOPIC_BANNER	        专题页面->广告banner
//    13. EVENT_ID_TOPIC_LIST	        	专题页面->专题列表
//    14. EVENT_ID_NEWS_BANNER	        咨询页面->广告banner
//    15. EVENT_ID_NEWS_LIST	 	        咨询页面->咨询列表
//    16. EVENT_ID_FOUND_LIST                 发现页面->好友列表
//    17. EVENT_ID_MUSIC_ARTIST_LIST          音乐界面->艺人歌单
//    18. EVENT_ID_MUSIC_LIST                 音乐界面->普通用户上传歌单
//    19. EVENT_ID_ARTIST_BANNER              艺人->banner
//    20. EVENT_ID_ARTIST_LIST                艺人->艺人列表
//    21. EVENT_ID_SELECT_VIDEO	        精选页面->视频列表
//    22. EVENT_ID_SELECT_COMPREHENSIVE	精选页面->综合排行
//    22. EVENT_ID_SELECT_JUMP	        精选页面->跳转


    //精选页面->分类
    public static final String EVENT_ID_SELECT_CATEGORY = "EVENT_ID_SELECT_CATEGORY";
    //精选页面->广告banner
    public static final String EVENT_ID_SELECT_BANNER = "EVENT_ID_SELECT_BANNER";
    //精选页面->搜索
    public static final String EVENT_ID_SELECT_SEARCH = "EVENT_ID_SELECT_SEARCH";
    //精选页面->上传
    public static final String EVENT_ID_SELECT_UPLAOD = "EVENT_ID_SELECT_UPLAOD";
    //专题页面->广告banner
    public static final String EVENT_ID_TOPIC_BANNER = "EVENT_ID_TOPIC_BANNER";
    //专题页面->专题列表
    public static final String EVENT_ID_TOPIC_LIST = "EVENT_ID_TOPIC_LIST";
    //咨询页面->广告banner
    public static final String EVENT_ID_NEWS_BANNER = "EVENT_ID_NEWS_BANNER";
    //咨询页面->咨询列表
    public static final String EVENT_ID_NEWS_LIST = "EVENT_ID_NEWS_LIST";
    //发现页面->好友列表
    public static final String EVENT_ID_FOUND_LIST = "EVENT_ID_FOUND_LIST";
    //音乐界面->艺人歌单
    public static final String EVENT_ID_MUSIC_ARTIST_LIST = "EVENT_ID_MUSIC_ARTIST_LIST";
    //音乐界面->普通用户上传歌单
    public static final String EVENT_ID_MUSIC_LIST = "EVENT_ID_MUSIC_LIST";
    //艺人->banner
    public static final String EVENT_ID_ARTIST_BANNER = "EVENT_ID_ARTIST_BANNER";
    //艺人->艺人列表
    public static final String EVENT_ID_ARTIST_LIST = "EVENT_ID_ARTIST_LIST";
    //精选页面->视频列表
    public static final String EVENT_ID_SELECT_VIDEO = "EVENT_ID_SELECT_VIDEO";
    //精选页面->综合排行
    public static final String EVENT_ID_SELECT_COMPREHENSIVE = "EVENT_ID_SELECT_COMPREHENSIVE";
    //精选页面->跳转
    public static final String EVENT_ID_SELECT_JUMP = "EVENT_ID_SELECT_JUMP";

    public static final SHARE_MEDIA[] alldisplaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.EMAIL, SHARE_MEDIA.SMS,
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.FACEBOOK_MESSAGER,
                    SHARE_MEDIA.WHATSAPP
            };

    public static final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.EMAIL, SHARE_MEDIA.SMS,
                    SHARE_MEDIA.WHATSAPP
            };

    public static void onEvent(Context context, String eventID) {
        MobclickAgent.onEvent(context, eventID);
    }

    public enum SHARE_PLATFORM {
        SHARE_PLATFORM_QQ,
        SHARE_PLATFORM_QZONE,
        SHARE_PLATFORM_WECHAT,
        SHARE_PLATFORM_WECHAT_CIRCLE,
        SHARE_PLATFORM_SINA,
        FACEBOOK,
        FACEBOOK_MESSAGER,
        WHATSAPP,
        EMAIL,
        SMS
    }

    private static SHARE_PLATFORM convertPlatform(SHARE_MEDIA share_media) {
        if (share_media == SHARE_MEDIA.QQ) {
            return SHARE_PLATFORM.SHARE_PLATFORM_QQ;
        } else if (share_media == SHARE_MEDIA.WEIXIN) {
            return SHARE_PLATFORM.SHARE_PLATFORM_WECHAT;
        } else if (share_media == SHARE_MEDIA.SINA) {
            return SHARE_PLATFORM.SHARE_PLATFORM_SINA;
        } else if (share_media == SHARE_MEDIA.QZONE) {
            return SHARE_PLATFORM.SHARE_PLATFORM_QZONE;
        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            return SHARE_PLATFORM.SHARE_PLATFORM_WECHAT_CIRCLE;
        } else if (share_media == SHARE_MEDIA.FACEBOOK) {
            return SHARE_PLATFORM.FACEBOOK;
        } else if (share_media == SHARE_MEDIA.FACEBOOK_MESSAGER) {
            return SHARE_PLATFORM.FACEBOOK_MESSAGER;
        } else if (share_media == SHARE_MEDIA.WHATSAPP) {
            return SHARE_PLATFORM.WHATSAPP;
        } else if (share_media == SHARE_MEDIA.EMAIL) {
            return SHARE_PLATFORM.EMAIL;
        } else if (share_media == SHARE_MEDIA.SMS) {
            return SHARE_PLATFORM.SMS;
        }
        return null;
    }

    private static SHARE_MEDIA convertPlatform(SHARE_PLATFORM share_media) {
        if (share_media == SHARE_PLATFORM.SHARE_PLATFORM_QQ) {
            return SHARE_MEDIA.QQ;
        } else if (share_media == SHARE_PLATFORM.SHARE_PLATFORM_WECHAT) {
            return SHARE_MEDIA.WEIXIN;
        } else if (share_media == SHARE_PLATFORM.SHARE_PLATFORM_SINA) {
            return SHARE_MEDIA.SINA;
        } else if (share_media == SHARE_PLATFORM.SHARE_PLATFORM_QZONE) {
            return SHARE_MEDIA.QZONE;
        } else if (share_media == SHARE_PLATFORM.SHARE_PLATFORM_WECHAT_CIRCLE) {
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        } else if (share_media == SHARE_PLATFORM.FACEBOOK) {
            return SHARE_MEDIA.FACEBOOK;
        } else if (share_media == SHARE_PLATFORM.FACEBOOK_MESSAGER) {
            return SHARE_MEDIA.FACEBOOK_MESSAGER;
        } else if (share_media == SHARE_PLATFORM.WHATSAPP) {
            return SHARE_MEDIA.WHATSAPP;
        } else if (share_media == SHARE_PLATFORM.SMS) {
            return SHARE_MEDIA.SMS;
        } else if (share_media == SHARE_PLATFORM.EMAIL) {
            return SHARE_MEDIA.EMAIL;
        }
        return null;
    }

    /**
     * 分享本地图片的函数
     * 参数
     *
     * @Param context ： activity
     * @Param title ： 分享标题
     * @Param targetUrl ： 分享链接的地址
     * @Param imgUrl ： 本地的图片资源
     * @Param listener ： 分享回调
     */
    public static void share(Activity context, String title, String content, String targetUrl, @DrawableRes int imgUrl, final UMengShareListener listener) {
        share(context, title, content, targetUrl, new UMImage(context, imgUrl), listener);
    }

    /**
     * 分享网络图片的函数
     * 参数
     *
     * @Param context ： activity
     * @Param title ： 分享标题
     * @Param targetUrl ： 分享链接的地址
     * @Param imgUrl ： 网络的图片资源
     * @Param listener ： 分享回调
     */
    public static void share(Activity context, String title, String content, String targetUrl, String imgUrl, final UMengShareListener listener) {
        share(context, title, content, targetUrl, new UMImage(context, imgUrl), listener);
    }

    /**
     * 分享网络图片使用七牛缩略图的函数
     * 参数
     *
     * @Param context ： activity
     * @Param title ： 分享标题
     * @Param targetUrl ： 分享链接的地址
     * @Param imgUrl ： 本地的图片资源
     * @Param appendThummnail： 是否对图片使用七牛的缩略图
     * @Param listener ： 分享回调
     */
    public static void share(Activity context, String title, String content, String targetUrl, String imgUrl, boolean appendThummnail, final UMengShareListener listener) {
        if (appendThummnail) {
            imgUrl = imgUrl;
        }
        share(context, title, content, targetUrl, new UMImage(context, imgUrl), listener);
    }


    /**
     * 分享网络图片使用七牛缩略图的函数
     * 参数
     *
     * @Param context ： activity
     * @Param platform: 分享的平台
     * @Param title ： 分享标题
     * @Param targetUrl ： 分享链接的地址
     * @Param imgUrl ： 本地的图片资源
     * @Param appendThummnail： 是否对图片使用七牛的缩略图
     * @Param listener ： 分享回调
     */
    public static void share(Activity context, SHARE_PLATFORM platform, String title, String content, String targetUrl, int imgUrl, final UMengShareListener listener) {

        UMImage umImage = new UMImage(context, imgUrl);
        UMWeb umWeb = new UMWeb(targetUrl);
        umWeb.setTitle(title);
        umWeb.setDescription(content);
        umWeb.setThumb(umImage);

        new ShareAction(context)
                .setPlatform(convertPlatform(platform))
                .withMedia(umWeb).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onStart(platform);
                }
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onResult(platform);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onError(platform, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onCancel(platform);
                }
            }
        }).share();
    }


    /**
     * 分享网络图片使用七牛缩略图的函数
     * 参数
     *
     * @Param context ： activity
     * @Param platform: 分享的平台
     * @Param title ： 分享标题
     * @Param targetUrl ： 分享链接的地址
     * @Param imgUrl ： 本地的图片资源
     * @Param appendThummnail： 是否对图片使用七牛的缩略图
     * @Param listener ： 分享回调
     */
    public static void share(Activity context, SHARE_PLATFORM platform, String title, String content, String targetUrl, String imgUrl, boolean appendThummnail, final UMengShareListener listener) {
        if (appendThummnail && !TextUtils.isEmpty(imgUrl)) {
            imgUrl = imgUrl;
        }


        UMImage umImage = TextUtils.isEmpty(imgUrl) ? new UMImage(context, R.drawable.logo)
                : new UMImage(context, imgUrl);
        UMWeb umWeb = new UMWeb(targetUrl);
        umWeb.setTitle(title);
        umWeb.setDescription(content);
        umWeb.setThumb(umImage);

        new ShareAction(context)
                .setPlatform(convertPlatform(platform))
                .withMedia(umWeb).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onStart(platform);
                }
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onResult(platform);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onError(platform, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onCancel(platform);
                }
            }
        }).share();
    }

    private static void share(Activity context, String title, String content, String targetUrl, UMImage umImage, final UMengShareListener listener) {
        UMWeb umWeb = new UMWeb(targetUrl);
        umWeb.setTitle(title);
        umWeb.setDescription(content);
        umWeb.setThumb(umImage);
        new ShareAction(context)
                .setDisplayList(displaylist)
                .withMedia(umWeb).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onStart(platform);
                }
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onResult(platform);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onError(platform, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (listener != null) {
                    SHARE_PLATFORM platform = convertPlatform(share_media);
                    listener.onCancel(platform);
                }
            }
        }).open();

    }
}
