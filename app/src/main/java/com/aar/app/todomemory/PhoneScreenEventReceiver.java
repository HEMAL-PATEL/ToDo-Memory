package com.aar.app.todomemory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneScreenEventReceiver extends BroadcastReceiver {

    public static final String EXTRA_SCREEN_STATE = "screen_state";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Intent i = new Intent(context, PhoneScreenService.class);
            i.putExtra(EXTRA_SCREEN_STATE, false);
            context.startService(i);
        }
    }
}
