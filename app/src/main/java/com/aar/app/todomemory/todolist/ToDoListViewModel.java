package com.aar.app.todomemory.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.aar.app.todomemory.SingleLiveEvent;
import com.aar.app.todomemory.data.HistoryDao;
import com.aar.app.todomemory.data.ToDoDao;
import com.aar.app.todomemory.data.ToDoDatabase;
import com.aar.app.todomemory.model.History;
import com.aar.app.todomemory.model.ToDo;
import com.aar.app.todomemory.settings.SettingsProvider;

import java.util.Calendar;
import java.util.List;

public class ToDoListViewModel extends AndroidViewModel {

    private ToDoDao mToDoDao;
    private HistoryDao mHistoryDao;
    private SettingsProvider mSettingsProvider;

    private MutableLiveData<List<ToDo>> mToDosLiveData = new MutableLiveData<>();
    private SingleLiveEvent<Integer> mOnToDoDeleted = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> mOnToDoDone = new SingleLiveEvent<>();

    public ToDoListViewModel(@NonNull Application application) {
        super(application);
        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
        mHistoryDao = ToDoDatabase.getInstance(application).getHistoryDao();
        mSettingsProvider = SettingsProvider.getInstance(application);
    }

    public void delete(int index, ToDo todo) {
        mToDoDao.delete(todo);
        mOnToDoDeleted.setValue(index);
    }

    public void done(int index, ToDo todo) {
        todo.setState(ToDo.STATE_DONE);

        if (mSettingsProvider.historyWhenDone()) {
            mHistoryDao.insert(getHistory(todo));
        }

        if (mSettingsProvider.isRemoveWhenDone()) {
            mToDoDao.delete(todo);
            mOnToDoDeleted.setValue(index);
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

    public SingleLiveEvent<Integer> getOnToDoDeleted() {
        return mOnToDoDeleted;
    }

    public SingleLiveEvent<Integer> getOnToDoDone() {
        return mOnToDoDone;
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
            mToDosLiveData.setValue(mToDoDao.getAllAsc());
        else
            mToDosLiveData.setValue(mToDoDao.getAllDesc());
    }
}
