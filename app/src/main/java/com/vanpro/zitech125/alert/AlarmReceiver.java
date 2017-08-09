package com.vanpro.zitech125.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import com.vanpro.zitech125.R;
import com.vanpro.zitech125.manage.NotificationMgr;
import com.vanpro.zitech125.ui.activity.MainActivity;
import com.vanpro.zitech125.ui.activity.TimeAlertActivity;
import com.vanpro.zitech125.util.AndroidUtils;
import com.vanpro.zitech125.util.AppDataManager;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int code = intent.getIntExtra("code", AlertUtil.FIRST_ALERT_CODE);

        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        String str;
        if (code == AlertUtil.END_ALERT_CODE)
            str = context.getString(R.string.park_time_time_up);
        else
            str = context.getString(R.string.park_time_has_ten_mins_left);

        notify(context, str);
    }

    private void notify(Context context, String text) {
        Intent i = new Intent(context, TimeAlertActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 100, makeIntentStack(context, i),
                PendingIntent.FLAG_ONE_SHOT);

        boolean notify = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_NOTIFY_SWITCH, true);
        if (notify) {
            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(context.getText(R.string.app_name))
                    .setTicker(context.getText(R.string.app_name))
                    .setContentText(text)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent).build();

            boolean sound = AppDataManager.getInstance().getBoolean(AppDataManager.KEY.IS_SOUND_NOTIFY, true);
            if (sound) {
//                notification.defaults = Notification.DEFAULT_SOUND;
                notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NotificationMgr.NID_PARk_TIME_ALERT, notification);
        }

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    //点击通知栏消息后启动对应的界面 连续打开首页和消息页面
    public Intent[] makeIntentStack(Context context, Intent intent) {
        Intent[] intents;
        if (AndroidUtils.applicationHasRunning(context)) {
            intents = new Intent[]{intent};
        } else {
            intents = new Intent[2];
            intents[0] = Intent.makeRestartActivityTask(new ComponentName(context, MainActivity.class));
            intents[1] = intent;
        }
        return intents;
    }
}