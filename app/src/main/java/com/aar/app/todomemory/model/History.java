package com.aar.app.todomemory.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "histories")
public class History {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;
    @ColumnInfo(name = "content")
    private String mContent;
    @ColumnInfo(name = "important")
    private boolean mImportant;
    @ColumnInfo(name = "done_at")
    private Date mDoneAt;

    public History() {
        this(0, "", false, Calendar.getInstance().getTime());
    }

    @Ignore
    public History(long id, String content, boolean important, Date doneAt) {
        mId = id;
        mContent = content;
        mImportant = important;
        mDoneAt = doneAt;
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

    public Date getDoneAt() {
        return mDoneAt;
    }

    public void setDoneAt(Date doneAt) {
        mDoneAt = doneAt;
    }
}
