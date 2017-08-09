package com.vanpro.zitech125.manage;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.vanpro.zitech125.MyApplication;
import com.vanpro.zitech125.R;
import com.vanpro.zitech125.alert.DisconnectedReceiver;
import com.vanpro.zitech125.ui.activity.MainActivity;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.AppDataManager;

/**
 * Created by Jinsen on 16/9/20.
 */
public class NotificationMgr {
    public static final String TAG = NotificationMgr.class.getName();

    /**
     * 蓝牙断开连接时发出的通知ID
     */
    public static final int NID_DISCONNECT_BLE = 111201;

    /**
     * 断开蓝牙获取定位信息失败时发出的通知栏ID
     */
    public static final int NID_LOCATION_FAIL = 111202;

    /**
     * 停车提醒时间提醒通知ID
     */
    public static final int NID_PARk_TIME_ALERT = 1234512;

    /**
     * 普通通知蓝牙
     */
    public static final int NID_NORMAL_ALERT = 1234523;

    private static class SingelHelper {
        public static NotificationMgr instance = new NotificationMgr();
    }

    private NotificationMgr() {
        init();
    }

    private void init() {
        if (mNotifyMgr == null) {
            mNotifyMgr = (android.app.NotificationManager) MyApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

            mBuilder = new NotificationCompat.Builder(MyApplication.getInstance());
            mBuilder.setSmallIcon(R.drawable.logo);
            mBuilder.setContentTitle(MyApplication.getInstance().getResources().getString(R.string.app_name));
            mBuilder.setAutoCancel(true);
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        }
    }

    public static NotificationMgr getInstance() {
        return SingelHelper.instance;
    }

    private NotificationCompat.Builder mBuilder;
    private android.app.NotificationManager mNotifyMgr;

    private static int DOWNLOAD_ID = 10000;
    private static int mCommonMessageId = 0;

    private Notification mDownloadNofication;
    RemoteViews mContentView;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showDownloadProgress(int downloadSize, int count){
        if(mDownloadNofication == null) {
            mDownloadNofication = new Notification();
            mDownloadNofication.icon = R.drawable.logo;
            // 这个参数是通知提示闪出来的值.
            mDownloadNofication.tickerText = MyApplication.getInstance().getResources().getString(R.string.upgrade_notify_download_start);

            Intent updateIntent = new Intent(MyApplication.getInstance().getBaseContext(), MainActivity.class);
            updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getInstance().getBaseContext(), 0, updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            /***
             * 在这里我们用自定的view来显示Notification
             */
            mContentView = new RemoteViews(MyApplication.getInstance().getPackageName(), R.layout.notification_download_layout);
            mContentView.setTextViewText(R.id.download_state, MyApplication.getInstance().getResources().getString(R.string.upgrade_notify_download_start));
            mContentView.setTextViewText(R.id.download_progress, apkSize2M(downloadSize,count));
            mContentView.setProgressBar(R.id.progressBar, 100, 0, false);

            //给我remoteViews上的控件tv_content添加监听事件
            mContentView.setOnClickPendingIntent(R.id.download_state, pendingIntent);
            mDownloadNofication.contentView = mContentView;
            mDownloadNofication.contentIntent = pendingIntent;
            mDownloadNofication.flags = Notification.FLAG_AUTO_CANCEL;
        }

        mContentView.setTextViewText(R.id.download_state, MyApplication.getInstance().getResources().getString(R.string.upgrade_notify_download_ing));
        mContentView.setTextViewText(R.id.download_progress, apkSize2M(downloadSize,count));
        mContentView.setProgressBar(R.id.progressBar, 100, downloadSize * 100 / count, false);

        mNotifyMgr.notify(DOWNLOAD_ID,mDownloadNofication);
    }

    private String apkSize2M(int downloadSize, int count){
        return Math.round(100*downloadSize/1024/1024)/100.0 + "M/" + Math.round(100*count/1024/1024)/100.0 + "M";
    }

    public void showDownloadDone(int count){
        mContentView.setTextViewText(R.id.download_state, MyApplication.getInstance().getResources().getString(R.string.upgrade_notify_download_done));
        mContentView.setTextViewText(R.id.download_progress, apkSize2M(count,count));
        mContentView.setProgressBar(R.id.progressBar, 100, 100, false);

        mNotifyMgr.notify(DOWNLOAD_ID,mDownloadNofication);
    }


    /**
     * sendMessage
     * 将消息发送到通知栏，并相应用户点击发布
     *
     * @param resultIntent
     */
    public void sendMessage(Context context, Intent resultIntent) {
        if (mBuilder == null || mNotifyMgr == null)
            init();
        /**
         * 消息ID逐渐累加是为了让每条消息不被覆盖
         */
        mCommonMessageId++;

        /**
         * 创建任务栈，当点击返回按钮时可以退回到指定Activity.
         */


        PendingIntent pendingIntent =
                PendingIntent.getActivities(context, mCommonMessageId, makeIntentStack(context, resultIntent),
                        PendingIntent.FLAG_ONE_SHOT);

//        mBuilder.setContentTitle(message.getTitle())
//                .setContentText(message.getAlert())
//                .setTicker(message.getTitle())
//                .setContentIntent(pendingIntent);

        mNotifyMgr.notify(mCommonMessageId, mBuilder.build());
    }


    //点击通知栏消息后启动对应的界面 连续打开首页和消息页面
    public Intent[] makeIntentStack(Context context, Intent intent) {
        Intent[] intents;
//        if(AppDataManager.getInstance().getBoolean(AppDataManager.KEY.APP_HAS_RUNING)){
        if (AndroidUtils.applicationHasRunning(context)) {
            intents = new Intent[]{intent};
        } else {
            intents = new Intent[2];
            intents[0] = Intent.makeRestartActivityTask(new ComponentName(context, MainActivity.class));
            intents[1] = intent;
        }
        return intents;
    }


    public void alertDisconnectedNotify(String text) {
        Intent i = new Intent(MyApplication.getInstance().getBaseContext(), DisconnectedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getInstance().getBaseContext(), 111201, i, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentTitle(MyApplication.getInstance().getResources().getString(R.string.app_name))
                .setContentText(text)
                .setTicker(text)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);
        boolean sound = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_SOUND_NOTIFY, true);
        if (sound) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }else{
            mBuilder.setDefaults(0);
        }

        mNotifyMgr.notify(NID_DISCONNECT_BLE, mBuilder.build());
    }

    public void alertLocationFailNotify(String text) {
        Intent i = new Intent(MyApplication.getInstance().getBaseContext(), DisconnectedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getInstance().getBaseContext(), 111202, i, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentTitle(MyApplication.getInstance().getResources().getString(R.string.app_name))
                .setContentText(text)
                .setTicker(text)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        boolean sound = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_SOUND_NOTIFY, true);
        if (sound) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }else{
            mBuilder.setDefaults(0);
        }

        mNotifyMgr.notify(NID_LOCATION_FAIL, mBuilder.build());
    }

    public void alertNormalNotify(String text) {
        Intent i = new Intent(MyApplication.getInstance().getBaseContext(), DisconnectedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getInstance().getBaseContext(), 111202, i, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentTitle(MyApplication.getInstance().getResources().getString(R.string.app_name))
                .setContentText(text)
                .setTicker(text)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        boolean sound = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_SOUND_NOTIFY, true);
        if (sound) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }else{
            mBuilder.setDefaults(0);
        }

        mNotifyMgr.notify(NID_NORMAL_ALERT, mBuilder.build());
    }

    public void cancelNormalNotify(){
        if(mNotifyMgr != null)
            mNotifyMgr.cancel(NID_NORMAL_ALERT);
    }

    /**
     * 取消所有的通知
     */
    public void cancelAll(){
        if(mNotifyMgr != null)
            mNotifyMgr.cancelAll();
    }

    /**
     * 清除定位失败的通知栏
     */
    public void cancelLocationFailNotify(){
        if(mNotifyMgr != null)
            mNotifyMgr.cancel(NID_LOCATION_FAIL);
    }

    /**
     * 清除定位成功后，通知设置提醒时间的通知
     */
    public void cancelLocationSuccNotity(){
        if(mNotifyMgr != null)
            mNotifyMgr.cancel(NID_DISCONNECT_BLE);
    }


}
