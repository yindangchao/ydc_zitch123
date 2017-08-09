package com.vanpro.zitech125.manage;

import com.vanpro.zitech125.event.LocationChangeEvent;
import com.vanpro.zitech125.event.SensorRorationChangeEvent;
import com.vanpro.zitech125.event.StatusChangeEvent;
import com.vanpro.zitech125.location.ZLocation;
import com.vanpro.zitech125.present.LocationMgr;
import com.vanpro.zitech125.util.AppStateSaveUtil;
import com.vanpro.zitech125.util.LogUtil;

import de.greenrobot.event.EventBus;

/**
 * 所有的数据／状态管理器
 * <p>
 * Created by Jinsen on 16/11/25.
 */
public class StatusManage {

    static StatusManage _instance;

    int mCurStatus = Status.CONTECTING;

    /**
     * 当前的指向偏向角度
     */
    float mRotation = 0;

    /**
     * 当前手机指向的角度
     */
    float mPhoneDigress = 0;

    float mDistance = -1;

    ZLocation mCarLocation, mCurLocation;

    float mToCarBearing = 0;

    float mLastDegrees = -1;

    /**
     * 蓝牙是否已打开
     */
    boolean isOpenBle = false;

    private StatusManage() {
    }

    public static void createInstance() {
        if (_instance == null)
            _instance = new StatusManage();
    }

    public static StatusManage getInstance() {
        if (_instance == null) {
            _instance = new StatusManage();
        }

        return _instance;
    }

    public void onDestory() {
    }

    public void setCarLocation(ZLocation location) {
        this.mCarLocation = location;
        ccdb();
    }

    public ZLocation getCarLocation() {
        return mCarLocation;
    }

    public void setCurLocation(ZLocation location) {
        this.mCurLocation = location;
        if(this.mCurLocation != null) {
            ccdb();
            EventBus.getDefault().post(new LocationChangeEvent());
        }
    }

    // car 2 cur distance bearing
    private void ccdb() {
        if (mCarLocation != null && mCurLocation != null) {
            this.mDistance = mCarLocation.distanceTo(mCurLocation);
            this.mToCarBearing = (mCurLocation.bearingTo(mCarLocation)+360)%360;

            rotationChange();
        }
    }

    public ZLocation getCurLocation() {
        if(mCurLocation == null)
            setCurLocation(LocationMgr.getInstance().getLocation());
        return mCurLocation;
    }

    public void setStatusConnecting(){
        mCarLocation = null;
        setStatus(Status.CONTECTING);
    }

    public void setStatusConected() {
        mCarLocation = null;
        setStatus(Status.CONTECTED);
    }

    public void setStatusLoctionFail() {
        setStatus(Status.LOCATION_FAIL);
    }

    public void setStatusLoctionSucc() {
        mCarLocation = AppStateSaveUtil.getLastCarLocation();
        mCurLocation = LocationMgr.getInstance().getLocation();

        if(mCarLocation != null) {
            if (mCurLocation != null) {
                this.mDistance = mCarLocation.distanceTo(mCurLocation);
                this.mToCarBearing = (mCurLocation.bearingTo(mCarLocation)+360)%360;
            }
            setStatus(Status.LOCATION_SUCC);
        }else{
            setStatusLoctionFail();
        }
    }

    public void setStatus(int status) {
        this.mCurStatus = status;
        EventBus.getDefault().post(new StatusChangeEvent());
    }

    public int getStatus() {
        return mCurStatus;
    }

    public synchronized void setRotation(float rotation) {
        this.mPhoneDigress = rotation;
        rotationChange();
    }

    private void rotationChange() {
//        if(mCurStatus != Status.CONTECTED && mCurStatus != Status.LOCATION_SUCC)
//            return;

        if(mCurStatus == Status.CONTECTED)
            return;

        float oldRotation = mRotation;
        if (mToCarBearing > 0) {
            mRotation = mToCarBearing - mPhoneDigress;
//            mRotation = ((mRotation) + 360) % 360;
        }else
            mRotation = mPhoneDigress;

        if (Math.abs(mLastDegrees - mRotation) >= 2 /*&& (((int) mRotation) % 18 == 0)*/) {
            mLastDegrees = mRotation;

            LogUtil.e("xxxxx", "old bearing = " + mToCarBearing + " mRotation = " + mRotation + " old ss =" + oldRotation);

            EventBus.getDefault().post(new SensorRorationChangeEvent());
        }
    }

    /**
     * 获取当前偏向角度
     *
     * @return
     */
    public float getRotation() {
//        return mToCarBearing;
//        return mPhoneDigress;
        return mRotation;
    }

    public float getBearing() {
        return mToCarBearing;
    }

    public float getPhoneDigree(){
        return mPhoneDigress;
    }

    /**
     * 获取距离车的距离
     *
     * @return
     */
    public float getDistance() {
        return this.mDistance;
    }

    public boolean isOpenBle() {
        return isOpenBle;
    }

    public void setOpenBle(boolean openBle) {
        isOpenBle = openBle;
    }

    public class Status {

        /**
         * 正在进行连接
         */
        public final static int CONTECTING = 0x4;

        /**
         * 连接中（已连接上）
         */
        public final static int CONTECTED = 0x1;

        /**
         * 断开连接 定位失败
         */
        public final static int LOCATION_FAIL = 0x2;

        /**
         * 断开连接 定位成功
         */
        public final static int LOCATION_SUCC = 0x3;
    }

}
