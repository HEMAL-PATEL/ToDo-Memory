package com.aar.app.todomemory.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.aar.app.todomemory.model.ToDo;

import java.util.List;

@Dao
public interface ToDoDao {

    @Query("SELECT * FROM todos")
    LiveData<List<ToDo>> getAll();

    @Query("SELECT * FROM todos WHERE id=:todoId")
    ToDo getBy(long todoId);

    @Insert
    long insert(ToDo todo);

    @Update
    void update(ToDo todo);

    @Delete
    void delete(ToDo todo);
}
