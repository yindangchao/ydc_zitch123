package com.vanpro.zitech125.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vanpro.zitech125.event.VerifyStoragePermissionEvent;

import java.io.File;

import de.greenrobot.event.EventBus;


/**
 * 程序配置文件（读取和写入）
 * <p/>
 * Created by Jinsen
 */
public class Config {

    private static final String TAG = Config.class.getName();

    /**
     * 数据目录
     */
    public final static String APP_DATA_PATH = "zitech117";

    /**
     * 图片目录
     */
    public final static String APP_IMAGES_PATH = "photos";

    /**
     * 临时目录名称
     */
    public final static String APP_TEMP_PATH = "temp";

    /**
     * 下载
     */
    public final static String APP_DOWNLOAD_PATH = "download";


    private static Context context = null;

    public static void register(Context context) {
        Config.context = context;
    }

    /**
     * 是否存在SD卡
     *
     * @return
     */
    public static boolean isExsitSDCard() {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 取得空闲SD卡空间大小
     *
     * @return MB
     */
    public static long getAvailaleSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        /* 获取block的SIZE */
        long blockSize = stat.getBlockSize();
        /* 空闲的Block的数量 */
        long availableBlocks = stat.getAvailableBlocks();
        /* 返回bit大小值 */
        return availableBlocks * blockSize / 1024 / 1024;
    }

    /**
     * 获取app数据目录
     *
     * @return
     */
    public static String getAppDataPath() {
        // 判断是否挂载了SD卡
        String dataPath = null;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            dataPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + File.separator
                    + APP_DATA_PATH
                    + File.separator;
        } else {
            File basePath = context.getFilesDir();
            if (basePath == null) {
                basePath = context.getCacheDir();
            }
            dataPath = basePath.getAbsolutePath()
                    + File.separator
                    + APP_DATA_PATH
                    + File.separator;
        }
        File file = new File(dataPath);
        if (!file.exists()) {
            boolean b = file.mkdirs();
            if(!b){
                EventBus.getDefault().post(new VerifyStoragePermissionEvent());
            }
        }
        return dataPath;
    }

    /**
     * 下载文件存放路径
     *
     * @return
     */
    public static String getDownloadPath() {
        String tempPath = getAppDataPath() + APP_DOWNLOAD_PATH + File.separator;
        File file = new File(tempPath);
        if (!file.exists()) {
            boolean s = file.mkdirs();
            LogUtil.e(TAG, "file " + file.getPath() + "   " + s);
        }
        return tempPath;
    }

    /**
     * /**
     * 获取程序图片目录
     *
     * @return
     */
    public static String getAppImagesPath() {
        String imgPath = getAppDataPath() + APP_IMAGES_PATH + File.separator;
        File file = new File(imgPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return imgPath;
    }

    /**
     * 获取程序临时目录
     *
     * @return
     */
    public static String getAppTempPath() {
        return getAppDataPath() + APP_TEMP_PATH + File.separator;
    }

    /**
     * 清空所有app数据
     */
    public static void clearAppData() {
        Log.d(TAG, "delete " + getAppDataPath());

        File cacheFile = context.getCacheDir();
        if (cacheFile != null) {
            FileUtils.deleteAllFile(cacheFile.getAbsolutePath());
        }
        FileUtils.deleteAllFile(getAppDataPath());
    }

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences() {
        if (Config.context == null) {
            Log.e(TAG, "配置类没有注册上AppContext(下文环境)");
        }
        return PreferenceManager.getDefaultSharedPreferences(Config.context);
    }

    /**
     * 写入配置信息，需要最后面进行 commit()
     *
     * @param key
     * @param value
     * @return
     */
    public static void putString(String key, String value) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 写入配置信息，需要最后面进行 commit()
     *
     * @param key
     * @param value
     * @return
     */
    public static void putInt(String key, int value) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 写入配置信息，需要最后面进行 commit()
     *
     * @param key
     * @param value
     * @return
     */
    public static void putLong(String key, long value) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 读取配置信息
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    /**
     * 读取配置信息
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    /**
     * 读取配置信息
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return getSharedPreferences().getLong(key, 0L);
    }

    /**
     * 删除配置信息，可以同时删除多个
     *
     * @param keys
     */
    public static void remove(String... keys) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }

}