package com.aar.app.todomemory.history;

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

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private HistoryDao mHistoryDao;
    private ToDoDao mToDoDao;

    private MutableLiveData<List<History>> mHistoryLiveData = new MutableLiveData<>();
    private SingleLiveEvent<Integer> mHistoryDeleted = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> mOnHistoryReused = new SingleLiveEvent<>();

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        mHistoryDao = ToDoDatabase.getInstance(application).getHistoryDao();
        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
    }

    public void delete(int index, History todo) {
        mHistoryDao.delete(todo);
        mHistoryDeleted.setValue(index);
    }

    public void reuse(int index, History todo) {
        mToDoDao.insert(new ToDo(0, todo.getContent(), ToDo.STATE_NONE, todo.isImportant()));
        mOnHistoryReused.setValue(index);
    }

    public LiveData<List<History>> getToDoHistoryLiveData() {
        return mHistoryLiveData;
    }

    public SingleLiveEvent<Integer> getHistoryDeleted() {
        return mHistoryDeleted;
    }

    public SingleLiveEvent<Integer> getOnHistoryReused() {
        updateHistoryList();
        return mOnHistoryReused;
    }

    private void updateHistoryList() {
        mHistoryLiveData.setValue(mHistoryDao.getAll());
    }
}
