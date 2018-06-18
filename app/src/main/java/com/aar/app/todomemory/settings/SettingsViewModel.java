package com.aar.app.todomemory.settings;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.aar.app.todomemory.AutoLaunchServiceManager;
import com.aar.app.todomemory.SingleLiveEvent;
import com.aar.app.todomemory.data.ToDoDao;
import com.aar.app.todomemory.data.ToDoDatabase;

public class SettingsViewModel extends AndroidViewModel {

    private SettingsProvider mSettingsProvider;
    private ToDoDao mToDoDao;
    private AutoLaunchServiceManager mAutoLaunchServiceManager;

    private MutableLiveData<Boolean> mRemoveWhenDone = new MutableLiveData<>();
    private MutableLiveData<Boolean> mHistoryWhenDone = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRunWhenTurnOn = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRunOnlyWhenToDoExist = new MutableLiveData<>();
    private MutableLiveData<Integer> mTextAlignment = new MutableLiveData<>();
    private MutableLiveData<Integer> mTodoOrder = new MutableLiveData<>();
    private SingleLiveEvent<Integer> mOnThemeChanged = new SingleLiveEvent<>();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        mSettingsProvider = SettingsProvider.getInstance(application);

        mRemoveWhenDone.setValue(mSettingsProvider.isRemoveWhenDone());
        mHistoryWhenDone.setValue(mSettingsProvider.historyWhenDone());
        mRunWhenTurnOn.setValue(mSettingsProvider.runWhenTurnOn());
        mRunOnlyWhenToDoExist.setValue(mSettingsProvider.runOnlyWhenToDoExist());
        mTextAlignment.setValue(mSettingsProvider.textAlignment());
        mTodoOrder.setValue(mSettingsProvider.todoOrder());

        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
        mAutoLaunchServiceManager = new AutoLaunchServiceManager(application);
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
        if (!enable) {
            mSettingsProvider.setRunOnlyWhenToDoExist(false);
            mRunOnlyWhenToDoExist.setValue(false);

            mAutoLaunchServiceManager.stop();
        } else if (mSettingsProvider.runOnlyWhenToDoExist()) {
            if (mToDoDao.getCount() > 0)
                mAutoLaunchServiceManager.start();
            else
                mAutoLaunchServiceManager.stop();
        } else {
            mAutoLaunchServiceManager.start();
        }
        mRunWhenTurnOn.setValue(enable);
        mSettingsProvider.setRunWhenTurnOn(enable);
    }

    public void setRunOnlyWhenToDoExist(boolean enable) {
        if (mSettingsProvider.runWhenTurnOn()) {
            mRunOnlyWhenToDoExist.setValue(enable);
            mSettingsProvider.setRunOnlyWhenToDoExist(enable);

            if (enable) {
                if (mToDoDao.getCount() > 0)
                    mAutoLaunchServiceManager.start();
                else
                    mAutoLaunchServiceManager.stop();
            } else {
                mAutoLaunchServiceManager.start();
            }
        }
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

    public MutableLiveData<Boolean> getRunWhenTurnOn() {
        return mRunWhenTurnOn;
    }

    public MutableLiveData<Boolean> getRunOnlyWhenToDoExist() {
        return mRunOnlyWhenToDoExist;
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
