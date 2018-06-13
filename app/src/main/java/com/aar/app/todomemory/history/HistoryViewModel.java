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

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private HistoryDao mHistoryDao;
    private ToDoDao mToDoDao;
    private List<History> mHistoryList = new ArrayList<>();

    private MutableLiveData<List<History>> mHistoryLiveData = new MutableLiveData<>();
    private SingleLiveEvent<Integer> mHistoryDeleted = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> mOnHistoryReused = new SingleLiveEvent<>();

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        mHistoryDao = ToDoDatabase.getInstance(application).getHistoryDao();
        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
    }

    public void delete(int index) {
        if (!isIndexValid(index)) return;
        History history = mHistoryList.remove(index);
        mHistoryDao.delete(history);
        mHistoryDeleted.setValue(index);

        if (mHistoryList.isEmpty()) {
            mHistoryLiveData.setValue(mHistoryList);
        }
    }

    public void reuse(int index) {
        if (!isIndexValid(index)) return;
        History history = mHistoryList.get(index);
        mToDoDao.insert(new ToDo(0, history.getContent(), ToDo.STATE_NONE, history.isImportant()));
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
        mHistoryList = mHistoryDao.getAll();
        mHistoryLiveData.setValue(mHistoryList);
    }

    private boolean isIndexValid(int index) {
        return index >= 0 && index < mHistoryList.size();
    }
}
