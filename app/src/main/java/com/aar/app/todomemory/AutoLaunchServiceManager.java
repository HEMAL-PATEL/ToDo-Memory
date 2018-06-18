package com.aar.app.todomemory;

import android.content.Context;

public class AutoLaunchServiceManager {

    private Context mContext;

    public AutoLaunchServiceManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public void start() {
        Utils.startPhoneScreenEventService(mContext);
    }

    public void stop() {
        Utils.stopPhoneScreenEventService(mContext);
    }
}
