package com.aar.app.todomemory.history;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aar.app.todomemory.data.HistoryDao;
import com.aar.app.todomemory.data.ToDoDao;
import com.aar.app.todomemory.data.ToDoDatabase;
import com.aar.app.todomemory.model.History;
import com.aar.app.todomemory.model.ToDo;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private HistoryDao mHistoryDao;
    private ToDoDao mToDoDao;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        mHistoryDao = ToDoDatabase.getInstance(application).getHistoryDao();
        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
    }

    public void delete(History todo) {
        mHistoryDao.delete(todo);
    }

    public void reuse(History todo) {
        mToDoDao.insert(new ToDo(0, todo.getContent(), ToDo.STATE_NONE, todo.isImportant()));
    }

    public LiveData<List<History>> getToDoHistoryLiveData() {
        return mHistoryDao.getAll();
    }
}
