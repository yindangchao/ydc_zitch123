package com.vanpro.zitech125.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.vanpro.zitech125.event.TabChangeEvent;
import com.vanpro.zitech125.ui.activity.StartActivity;
import com.vanpro.zitech125.util.AndroidUtils;

import de.greenrobot.event.EventBus;


/**
 * Created by Jinsen on 16/10/13.
 */

public class DisconnectedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isBtn = intent.getBooleanExtra("isBtn",false);
        if (AndroidUtils.applicationHasRunning(context)) {
            EventBus.getDefault().post(new TabChangeEvent(0));
        } else {
            Intent intent1 = new Intent(context, StartActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }

}
