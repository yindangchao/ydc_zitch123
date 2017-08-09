package com.vanpro.zitech125.util;

import android.location.Location;

import com.baidu.location.BDLocation;
import com.vanpro.zitech125.MyApplication;
import com.vanpro.zitech125.location.ZLocation;

/**
 *
 * app的状态保存
 * Created by Jinsen on 16/4/20.
 */
public class AppStateSaveUtil {

    public static void reset(){
        AppDataManager.getInstance().setData(AppDataManager.KEY.BLUETOOTH_IS_CONNECTED,true);
        AppDataManager.getInstance().remove(AppDataManager.KEY.LOCATION_KEY);
        AppDataManager.getInstance().remove(AppDataManager.KEY.LOCATION_STATE);
        AppDataManager.getInstance().remove(AppDataManager.KEY.IS_NAVIGATION);
        AppDataManager.getInstance().remove(AppDataManager.KEY.PHOTO_PTAH);
        AppDataManager.getInstance().remove(AppDataManager.KEY.PHOTO_TIPS_PTAH);
        AppDataManager.getInstance().remove(AppDataManager.KEY.LAST_PARK_TIME_KEY);
        AppDataManager.getInstance().remove(AppDataManager.KEY.LAST_SET_ALERT_TIME);
        AppDataManager.getInstance().remove(AppDataManager.KEY.LOCATON_LAN_TYPE);
    }

    public static void navigationState(){
        AppDataManager.getInstance().setData(AppDataManager.KEY.BLUETOOTH_IS_CONNECTED,false);
        AppDataManager.getInstance().setData(AppDataManager.KEY.IS_NAVIGATION,true);
    }


    public static ZLocation getLastCarLocation(){
        String locStr = AppDataManager.getInstance().getString(AppDataManager.KEY.LOCATION_KEY);
        if(locStr != null){
            String [] loc = locStr.split("//");
            if(loc != null && loc.length == 3){
                if("ZH".equals(AppDataManager.getInstance().getString(AppDataManager.KEY.LOCATON_LAN_TYPE))){
                    BDLocation bdLocation = new BDLocation();
                    bdLocation.setLatitude(Double.valueOf(loc[1]));
                    bdLocation.setLongitude(Double.valueOf(loc[0]));
                    bdLocation.setDirection(Float.valueOf(loc[2]));
                    return new ZLocation(bdLocation);
                }else{
                    Location carLocation = new Location(android.location.LocationManager.GPS_PROVIDER);
                    carLocation.setLongitude(Double.valueOf(loc[0]));
                    carLocation.setLatitude(Double.valueOf(loc[1]));
                    carLocation.setBearing(Float.valueOf(loc[2]));
                    return new ZLocation(carLocation);
                }

            }
        }

        return null;
    }

    /**
     * 上次保存定位地址时跟当前的语言是否相同
     * @return
     */
    public static boolean isSameLan(){
        String curLan = AndroidUtils.getLanguage(MyApplication.getInstance());
        String last = AppDataManager.getInstance().getString(AppDataManager.KEY.LOCATON_LAN_TYPE);
        return curLan.equals(last);
    }

    public static void removeDevices(){
        reset();
        AppDataManager.getInstance().remove(AppDataManager.KEY.BLUETOOTH_IS_CONNECTED);
        AppDataManager.getInstance().remove(AppDataManager.KEY.LAST_CONNECTED_DEVICES_ADDRESSS);
    }

}
