package com.aar.app.todomemory.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.aar.app.todomemory.model.ToDo;

import java.util.List;

@Dao
public interface ToDoDao {

    @Query("SELECT * FROM todos ORDER BY id ASC")
    List<ToDo> getAllAsc();

    @Query("SELECT * FROM todos ORDER BY id DESC")
    List<ToDo> getAllDesc();

    @Query("SELECT * FROM todos WHERE id=:todoId")
    ToDo getBy(long todoId);

    @Query("SELECT COUNT(*) FROM todos")
    int getCount();

    @Insert
    long insert(ToDo todo);

    @Update
    void update(ToDo todo);

    @Delete
    void delete(ToDo todo);
}
