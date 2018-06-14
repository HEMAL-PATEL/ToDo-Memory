package com.aar.app.todomemory;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.List;

public class Utils {

    public static void rateAppInGooglePlay(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intentMarket = new Intent(Intent.ACTION_VIEW, uri);

        intentMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(intentMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void startPhoneScreenEventService(Context context) {
        Intent i = new Intent(context, PhoneScreenService.class);
        context.startService(i);
    }

    public static void stopPhoneScreenEventService(Context context) {
        Intent i = new Intent(context, PhoneScreenService.class);
        context.stopService(i);
    }
}
