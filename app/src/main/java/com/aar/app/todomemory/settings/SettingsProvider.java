package com.aar.app.todomemory.settings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.aar.app.todomemory.R;

public class SettingsProvider {

    private static SettingsProvider INSTANCE = null;

    public static SettingsProvider getInstance(@NonNull Application application) {
        if (INSTANCE == null) {
            INSTANCE = new SettingsProvider(application);
        }

        return INSTANCE;
    }

    private static final String KEY_REMOVE_WHEN_DONE = "remove_when_done";
    private static final String KEY_HISTORY_WHEN_DONE = "history_when_done";
    private static final String KEY_RUN_WHEN_TURN_ON = "run_when_turn_on";
    private static final String KEY_RUN_ONLY_TODO_EXIST = "run_only_todo_exist";
    private static final String KEY_TEXT_ALIGN = "text_align";
    private static final String KEY_ORDER = "order";
    private static final String KEY_THEME = "theme";

    private SharedPreferences mPref;

    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;

    public static final int ORDER_ASC = 0;
    public static final int ORDER_DESC = 1;

    public static final int THEME_DARK = R.style.AppTheme;
    public static final int THEME_LIGHT = R.style.AppTheme_Light;

    private SettingsProvider(@NonNull Application application) {
        mPref = application.getSharedPreferences("todo_memory", Context.MODE_PRIVATE);
    }

    public boolean isRemoveWhenDone() {
        return mPref.getBoolean(KEY_REMOVE_WHEN_DONE, false);
    }

    public void setRemoveWhenDone(boolean enable) {
        mPref.edit()
                .putBoolean(KEY_REMOVE_WHEN_DONE, enable)
                .apply();
    }

    public boolean historyWhenDone() {
        return mPref.getBoolean(KEY_HISTORY_WHEN_DONE, true);
    }

    public void setHistoryWhenDone(boolean enable) {
        mPref.edit()
                .putBoolean(KEY_HISTORY_WHEN_DONE, enable)
                .apply();
    }

    public boolean runWhenTurnOn() {
        return mPref.getBoolean(KEY_RUN_WHEN_TURN_ON, true);
    }

    public void setRunWhenTurnOn(boolean enable) {
        mPref.edit()
                .putBoolean(KEY_RUN_WHEN_TURN_ON, enable)
                .apply();
    }

    public boolean runOnlyWhenToDoExist() {
        return mPref.getBoolean(KEY_RUN_ONLY_TODO_EXIST, true);
    }

    public void setRunOnlyWhenToDoExist(boolean enable) {
        mPref.edit()
                .putBoolean(KEY_RUN_ONLY_TODO_EXIST, enable)
                .apply();
    }

    public int textAlignment() {
        return mPref.getInt(KEY_TEXT_ALIGN, ALIGN_CENTER);
    }

    public void setTextAlignment(int alignment) {
        mPref.edit()
                .putInt(KEY_TEXT_ALIGN, alignment)
                .apply();
    }

    public int todoOrder() {
        return mPref.getInt(KEY_ORDER, ORDER_ASC);
    }

    public void setTodoOrder(int order) {
        mPref.edit()
                .putInt(KEY_ORDER, order)
                .apply();
    }

    public int theme() {
        return mPref.getInt(KEY_THEME, THEME_DARK);
    }

    public void setTheme(int theme) {
        mPref.edit()
                .putInt(KEY_THEME, theme)
                .apply();
    }
}
