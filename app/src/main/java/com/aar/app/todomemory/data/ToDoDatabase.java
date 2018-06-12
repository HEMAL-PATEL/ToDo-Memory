package com.aar.app.todomemory.data;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.aar.app.todomemory.model.Draft;
import com.aar.app.todomemory.model.History;
import com.aar.app.todomemory.model.ToDo;

@Database(entities = {ToDo.class, Draft.class, History.class}, version = 1)
@TypeConverters({RoomDateConverter.class})
public abstract class ToDoDatabase extends RoomDatabase {

    private static ToDoDatabase INSTANCE = null;

    public static ToDoDatabase getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(application, ToDoDatabase.class, "todos.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }

    public abstract ToDoDao getToDoDao();
    public abstract DraftDao getDraftDao();
    public abstract HistoryDao getHistoryDao();
}
