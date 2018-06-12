package com.aar.app.todomemory.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aar.app.todomemory.data.HistoryDao;
import com.aar.app.todomemory.data.ToDoDao;
import com.aar.app.todomemory.data.ToDoDatabase;
import com.aar.app.todomemory.model.History;
import com.aar.app.todomemory.model.ToDo;

import java.util.Calendar;
import java.util.List;

public class ToDoListViewModel extends AndroidViewModel {

    private ToDoDao mToDoDao;
    private HistoryDao mHistoryDao;

    public ToDoListViewModel(@NonNull Application application) {
        super(application);
        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
        mHistoryDao = ToDoDatabase.getInstance(application).getHistoryDao();
    }

    public void delete(ToDo todo) {
        mToDoDao.delete(todo);
    }

    public void done(ToDo todo) {
        todo.setState(ToDo.STATE_DONE);
        mToDoDao.update(todo);
        mHistoryDao.insert(getHistory(todo));
    }

    public LiveData<List<ToDo>> getToDosLiveData() {
        return mToDoDao.getAll();
    }

    private History getHistory(ToDo todo) {
        return new History(
                0,
                todo.getContent(),
                todo.isImportant(),
                Calendar.getInstance().getTime()
        );
    }
}
