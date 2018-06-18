package com.aar.app.todomemory.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.aar.app.todomemory.AutoLaunchServiceManager;
import com.aar.app.todomemory.SingleLiveEvent;
import com.aar.app.todomemory.data.HistoryDao;
import com.aar.app.todomemory.data.ToDoDao;
import com.aar.app.todomemory.data.ToDoDatabase;
import com.aar.app.todomemory.model.History;
import com.aar.app.todomemory.model.ToDo;
import com.aar.app.todomemory.settings.SettingsProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ToDoListViewModel extends AndroidViewModel {

    private ToDoDao mToDoDao;
    private HistoryDao mHistoryDao;
    private SettingsProvider mSettingsProvider;
    private AutoLaunchServiceManager mAutoLaunchServiceManager;

    private Pair<Integer, ToDo> mLastDeleted;
    private List<ToDo> mTodoList = new ArrayList<>();

    private MutableLiveData<List<ToDo>> mToDosLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNotificationEnableState = new MediatorLiveData<>();
    private SingleLiveEvent<Integer> mOnToDoDeleted = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> mOnToDoDone = new SingleLiveEvent<>();
    private SingleLiveEvent<Pair<Integer, ToDo>> mOnUndo = new SingleLiveEvent<>();

    public ToDoListViewModel(@NonNull Application application) {
        super(application);
        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
        mHistoryDao = ToDoDatabase.getInstance(application).getHistoryDao();
        mSettingsProvider = SettingsProvider.getInstance(application);
        mAutoLaunchServiceManager = new AutoLaunchServiceManager(application);
        mNotificationEnableState.setValue(mAutoLaunchServiceManager.isEnabled());
    }

    public void switchEnableNotification() {
        if (mAutoLaunchServiceManager.isEnabled()) {
            disableNotification();
        } else {
            enableNotification();
        }
    }

    private void enableNotification() {
        if (mToDoDao.getCount() > 0) {
            mAutoLaunchServiceManager.start();
            mNotificationEnableState.setValue(mAutoLaunchServiceManager.isEnabled());
        }
    }

    private void disableNotification() {
        mAutoLaunchServiceManager.stop();
        mNotificationEnableState.setValue(mAutoLaunchServiceManager.isEnabled());
    }

    public void delete(int index) {
        if (!isIndexValid(index)) return;
        ToDo todo = mTodoList.remove(index);
        mLastDeleted = new Pair<>(index, todo);
        mToDoDao.delete(todo);
        mOnToDoDeleted.setValue(index);

        if (mTodoList.isEmpty()) {
            mToDosLiveData.setValue(mTodoList);

            disableNotification();
        }
    }

    public void undo() {
        if (mLastDeleted != null) {
            int index = mLastDeleted.first;
            ToDo todo = mLastDeleted.second;

            mToDoDao.insert(todo);
            mTodoList.add(index, todo);
            mOnUndo.setValue(mLastDeleted);
            mLastDeleted = null;
        }
    }

    public void done(int index) {
        if (!isIndexValid(index)) return;
        ToDo todo = mTodoList.get(index);
        todo.setState(ToDo.STATE_DONE);

        if (mSettingsProvider.historyWhenDone()) {
            mHistoryDao.insert(getHistory(todo));
        }

        if (mSettingsProvider.removeWhenDone()) {
            delete(index);
        } else {
            mToDoDao.update(todo);
            mOnToDoDone.setValue(index);
        }
    }

    public int getTodoTextAlignment() {
        return mSettingsProvider.textAlignment();
    }

    public LiveData<List<ToDo>> getToDosLiveData() {
        updateToDoList();
        return mToDosLiveData;
    }

    public LiveData<Boolean> getNotificationEnableState() {
        return mNotificationEnableState;
    }

    public LiveData<Integer> getOnToDoDeleted() {
        return mOnToDoDeleted;
    }

    public LiveData<Integer> getOnToDoDone() {
        return mOnToDoDone;
    }

    public LiveData<Pair<Integer, ToDo>> getOnUndo() {
        return mOnUndo;
    }

    private History getHistory(ToDo todo) {
        return new History(
                0,
                todo.getContent(),
                todo.isImportant(),
                Calendar.getInstance().getTime()
        );
    }

    private void updateToDoList() {
        if (mSettingsProvider.todoOrder() == SettingsProvider.ORDER_ASC)
            mTodoList = mToDoDao.getAllAsc();
        else
            mTodoList = mToDoDao.getAllDesc();
        mToDosLiveData.setValue(mTodoList);
    }

    private boolean isIndexValid(int index) {
        return index >= 0 && index < mTodoList.size();
    }
}
