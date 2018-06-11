package com.aar.app.todomemory.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.aar.app.todomemory.data.ToDoDao;
import com.aar.app.todomemory.data.ToDoDatabase;
import com.aar.app.todomemory.model.ToDo;

import java.util.List;

public class ToDoListViewModel extends AndroidViewModel {

    private ToDoDao mToDoDao;

    public ToDoListViewModel(@NonNull Application application) {
        super(application);
        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
    }

    public void delete(ToDo todo) {
        mToDoDao.delete(todo);
    }

    public void done(ToDo todo) {
        todo.setState(ToDo.STATE_DONE);
        mToDoDao.update(todo);
    }

    public LiveData<List<ToDo>> getToDosLiveData() {
        return mToDoDao.getAll();
    }
}
