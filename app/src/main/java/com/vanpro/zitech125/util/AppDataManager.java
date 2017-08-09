package com.vanpro.zitech125.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.vanpro.zitech125.MyApplication;

/**
 * Created by Jinsen on 16/4/13.
 */
public class AppDataManager {

    private static AppDataManager _instance = null;

    private Context mContext;

    private static SharedPreferences mSpf = null;

    private AppDataManager(Context context) {
        this.mContext = context;
        mSpf = mContext.getSharedPreferences(KEY.FILE_NAME, Context.MODE_APPEND);
    }

    public static AppDataManager getInstance() {
        if (_instance == null) {
            _instance = new AppDataManager(MyApplication.getInstance());
        }

        return _instance;
    }

    public void setData(String key, Object object) {
        if (object == null)
            return;

        SharedPreferences.Editor editor = mSpf.edit();
        try {
            if (object instanceof String) {
                editor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, ((Integer) object).intValue());
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, ((Boolean) object).booleanValue());
            } else if (object instanceof Long) {
                editor.putLong(key, ((Long) object).longValue());
            } else if (object instanceof Float) {
                editor.putFloat(key, ((Float) object).floatValue());
            }
        } finally {
            editor.commit();
        }
    }


    public void remove(String key) {
        mSpf.edit().remove(key).commit();
    }

    public String getString(String key) {
        return mSpf.getString(key, null);
    }

    public int getInt(String key) {
        return mSpf.getInt(key, 0);
    }

    public int getSpecialInt(String key, int special) {
        return mSpf.getInt(key, special);
    }

    public long getLong(String key) {
        return mSpf.getLong(key, 0);
    }

    public Float getFloat(String key) {
        return mSpf.getFloat(key, 0);
    }

    public boolean getBoolean(String key) {
        return mSpf.getBoolean(key, false);
    }

    public boolean getBoolean(String key,boolean defaultV) {
        return mSpf.getBoolean(key, defaultV);
    }

    public void cleanUserPreference() {
        mSpf.edit().clear().commit();
    }

    public void close() {
        mSpf = null;
        mContext = null;
    }


    public static class KEY {
        public static final String FILE_NAME = "zitech_data_file";

        public static final String APP_RUNING = "app_runing";

        //蓝牙是否连接 中
        public static final String BLUETOOTH_IS_CONNECTED = "bluetooth_is_connected";

        //上次连接成功的蓝牙设备address
        public static final String LAST_CONNECTED_DEVICES_ADDRESSS = "last_connected_devices_address";


        //定位车的地址
        public static final String LOCATION_KEY = "location";

        //上次停车时间
        public static final String LAST_PARK_TIME_KEY = "last_park_time";


        public static final String LOCATION_STATE = "location_state";

        //上次保存定位地址的类型， 是中文还是英文
        public static final String LOCATON_LAN_TYPE = "location_lan_type";

        //是否是导航
        public static final String IS_NAVIGATION = "is_navigation";

        public static final String PHOTO_PTAH = "photo_path";
        public static final String PHOTO_TIPS_PTAH = "photo_tips";

        //main activity 是否是在运行中
        public static final String MAIN_IS_RUNING = "main_is_runing";

        public static final String HAS_SHOW_GUIDE = "has_show_guide";

        //上次设置停车时间时间戳
        public static final String LAST_SET_ALERT_TIME = "last_set_alert_time";


        public static final String IS_NOTIFY_SWITCH = "notify_is_open";

        public static final String IS_SOUND_NOTIFY = "notify_sound_switch";

        public static final String UNIT_TYPE_KEY = "unit_type_name";

        //地图选择的类型 1 为百度地图， 2 为Google地图 ， 0 为根据系统语言默认选择
        public static final String MAP_SELECTED_ID = "map_selected_id";

        public static final String IGNORE_UPDATE_INFO_KEY = "ignore_update_info";
    }


}
