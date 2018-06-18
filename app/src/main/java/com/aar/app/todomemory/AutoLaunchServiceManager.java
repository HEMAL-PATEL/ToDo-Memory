package com.aar.app.todomemory;

import android.content.Context;
import android.content.SharedPreferences;

public class AutoLaunchServiceManager {

    private static final String PREF_NAME = "auto_launch";
    private static final String KEY_ENABLED = "enabled";

    private Context mContext;
    private SharedPreferences mPref;

    public AutoLaunchServiceManager(Context context) {
        mContext = context.getApplicationContext();
        mPref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (isEnabled()) {
            if (!PhoneScreenService.isRunning())
                start();
        } else if (PhoneScreenService.isRunning()) {
                stop();
        }
    }

    public boolean isEnabled() {
        return mPref.getBoolean(KEY_ENABLED, false);
    }

    public void start() {
        setEnabled(true);
        if (!PhoneScreenService.isRunning()) {
            Utils.startPhoneScreenEventService(mContext);
        }
    }

    public void stop() {
        setEnabled(false);
        if (PhoneScreenService.isRunning()) {
            Utils.stopPhoneScreenEventService(mContext);
        }
    }

    private void setEnabled(boolean enabled) {
        mPref.edit()
                .putBoolean(KEY_ENABLED, enabled)
                .apply();
    }
}
