package com.aar.app.todomemory.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "todos")
public class ToDo {

    public static final int STATE_NONE = 0;
    public static final int STATE_DONE = 1;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;
    @ColumnInfo(name = "content")
    private String mContent;
    @ColumnInfo(name = "state")
    private int mState;
    @ColumnInfo(name = "important")
    private boolean mImportant;

    public ToDo() {
        this(0, "", STATE_NONE, false);
    }

    @Ignore
    public ToDo(long id, String content, int state, boolean important) {
        mId = id;
        mContent = content;
        mState = state;
        mImportant = important;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public boolean isImportant() {
        return mImportant;
    }

    public void setImportant(boolean important) {
        mImportant = important;
    }
}
