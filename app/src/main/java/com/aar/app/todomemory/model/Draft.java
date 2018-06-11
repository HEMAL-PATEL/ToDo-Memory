package com.aar.app.todomemory.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "draft")
public class Draft {

    @PrimaryKey
    private long mId = 1;

    @ColumnInfo(name = "content")
    private String mContent;
    @ColumnInfo(name = "important")
    private boolean mImportant;

    public Draft() {
        this("", false);
    }

    public Draft(String content, boolean important) {
        mContent = content;
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

    public boolean isImportant() {
        return mImportant;
    }

    public void setImportant(boolean important) {
        mImportant = important;
    }
}
