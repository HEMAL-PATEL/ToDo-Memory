package com.aar.app.todomemory.settings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

public class SettingsViewModel extends AndroidViewModel {

    private SettingsProvider mSettingsProvider;

    private MutableLiveData<Boolean> mRemoveWhenDone = new MutableLiveData<>();
    private MutableLiveData<Boolean> mHistoryWhenDone = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRunWhenTurnOn = new MutableLiveData<>();
    private MutableLiveData<Integer> mTextAlignment = new MutableLiveData<>();
    private MutableLiveData<Integer> mTodoOrder = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        mSettingsProvider = SettingsProvider.getInstance(application);

        mRemoveWhenDone.setValue(mSettingsProvider.isRemoveWhenDone());
        mHistoryWhenDone.setValue(mSettingsProvider.historyWhenDone());
        mRunWhenTurnOn.setValue(mSettingsProvider.runWhenTurnOn());
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

    public void setRunWhenTurnOn(boolean enable) {
        mRunWhenTurnOn.setValue(enable);
        mSettingsProvider.setRunWhenTurnOn(enable);
    }

    public void setTextAlignment(int alignment) {
        mTextAlignment.setValue(alignment);
        mSettingsProvider.setTextAlignment(alignment);
    }

    public void setTodoOrder(int order) {
        mTodoOrder.setValue(order);
        mSettingsProvider.setTodoOrder(order);
    }

    public MutableLiveData<Boolean> getRemoveWhenDone() {
        return mRemoveWhenDone;
    }

    public MutableLiveData<Boolean> getHistoryWhenDone() {
        return mHistoryWhenDone;
    }

    public MutableLiveData<Boolean> getRunWhenTurnOn() {
        return mRunWhenTurnOn;
    }

    public MutableLiveData<Integer> getTextAlignment() {
        return mTextAlignment;
    }

    public MutableLiveData<Integer> getTodoOrder() {
        return mTodoOrder;
    }
}
