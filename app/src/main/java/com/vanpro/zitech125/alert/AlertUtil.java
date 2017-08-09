package com.vanpro.zitech125.alert;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.vanpro.zitech125.manage.NotificationMgr;
import com.vanpro.zitech125.ui.activity.TimeAlertActivity;


/**
 * Created by Jinsen on 16/5/25.
 */
public class AlertUtil {
    
    public static final int FIRST_ALERT_CODE = 1022;
    public static final int END_ALERT_CODE = 1130;

    public static void set(Context context, int time){
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent i2=new Intent(context, TimeAlertActivity.class);
        PendingIntent pi2=PendingIntent.getActivity(context, 0, i2, 0);

        Intent intent2 = new Intent(context, AlarmReceiver.class);//创建Intent对象
        intent2.putExtra("code",END_ALERT_CODE);
        PendingIntent endPi = PendingIntent.getBroadcast(context, END_ALERT_CODE, intent2, 0);//创建PendingIntent
        alarmManager.cancel(endPi);
        long endTime = System.currentTimeMillis() + time * 60 * 1000;//
        if(Build.VERSION.SDK_INT >= 21){
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(endTime,pi2),endPi);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, endTime, endPi);//设置闹钟，当前时间就唤醒
        }

        Intent intent = new Intent(context, AlarmReceiver.class);//创建Intent对象
        PendingIntent firstPi = PendingIntent.getBroadcast(context, FIRST_ALERT_CODE, intent, 0);//创建PendingIntent
        alarmManager.cancel(firstPi);

        if(time > 10){
            intent.putExtra("code",FIRST_ALERT_CODE);
            long firstTime = System.currentTimeMillis() + ( time -10 ) * 60 * 1000;
            if(Build.VERSION.SDK_INT >= 21){
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(firstTime,pi2),firstPi);
            }else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, firstTime, firstPi);//设置闹钟，当前时间就唤醒
            }
        }
    }

    public static void clearAlertNofity(Context context){
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(context, AlarmReceiver.class);//创建Intent对象
        intent2.putExtra("code",END_ALERT_CODE);
        PendingIntent endPi = PendingIntent.getBroadcast(context, END_ALERT_CODE, intent2, 0);//创建PendingIntent
        alarmManager.cancel(endPi);
        Intent intent = new Intent(context, AlarmReceiver.class);//创建Intent对象
        PendingIntent firstPi = PendingIntent.getBroadcast(context, FIRST_ALERT_CODE, intent, 0);//创建PendingIntent
        alarmManager.cancel(firstPi);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NotificationMgr.NID_PARk_TIME_ALERT);
    }


}
