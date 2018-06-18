package com.aar.app.todomemory.settings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.aar.app.todomemory.SingleLiveEvent;

public class SettingsViewModel extends AndroidViewModel {

    private SettingsProvider mSettingsProvider;

    private MutableLiveData<Boolean> mRemoveWhenDone = new MutableLiveData<>();
    private MutableLiveData<Boolean> mHistoryWhenDone = new MutableLiveData<>();
    private MutableLiveData<Integer> mTextAlignment = new MutableLiveData<>();
    private MutableLiveData<Integer> mTodoOrder = new MutableLiveData<>();
    private SingleLiveEvent<Integer> mOnThemeChanged = new SingleLiveEvent<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        mSettingsProvider = SettingsProvider.getInstance(application);

        mRemoveWhenDone.setValue(mSettingsProvider.removeWhenDone());
        mHistoryWhenDone.setValue(mSettingsProvider.historyWhenDone());
        mTextAlignment.setValue(mSettingsProvider.textAlignment());
        mTodoOrder.setValue(mSettingsProvider.todoOrder());
    }

    public void setRemoveWhenDone(boolean enable) {
        mRemoveWhenDone.setValue(enable);
        mSettingsProvider.setRemoveWhenDone(enable);
    }

    public void setHistoryWhenDone(boolean enable) {
        mHistoryWhenDone.setValue(enable);
        mSettingsProvider.setHistoryWhenDone(enable);
    }

    public void setTextAlignment(int alignment) {
        mTextAlignment.setValue(alignment);
        mSettingsProvider.setTextAlignment(alignment);
    }

    public void setTodoOrder(int order) {
        mTodoOrder.setValue(order);
        mSettingsProvider.setTodoOrder(order);
    }

    public void setTheme(int theme) {
        if (mSettingsProvider.theme() != theme) {
            mSettingsProvider.setTheme(theme);
            mOnThemeChanged.setValue(theme);
        }
    }

    public MutableLiveData<Boolean> getRemoveWhenDone() {
        return mRemoveWhenDone;
    }

    public MutableLiveData<Boolean> getHistoryWhenDone() {
        return mHistoryWhenDone;
    }

    public MutableLiveData<Integer> getTextAlignment() {
        return mTextAlignment;
    }

    public MutableLiveData<Integer> getTodoOrder() {
        return mTodoOrder;
    }

    public MutableLiveData<Integer> getOnThemeChanged() {
        return mOnThemeChanged;
    }
}
