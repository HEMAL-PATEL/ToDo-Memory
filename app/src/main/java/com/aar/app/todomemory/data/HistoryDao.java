package com.aar.app.todomemory.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.aar.app.todomemory.model.History;

import java.util.List;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM histories")
    List<History> getAll();

    @Insert
    long insert(History todo);

    @Delete
    void delete(History todo);

    @Query("DELETE FROM histories")
    void clear();
}
