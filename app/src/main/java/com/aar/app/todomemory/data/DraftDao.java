package com.aar.app.todomemory.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.aar.app.todomemory.model.Draft;

@Dao
public interface DraftDao {

    @Query("SELECT * FROM draft")
    Draft getDraft();

    @Query("DELETE FROM draft")
    void clear();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(Draft draft);

}
