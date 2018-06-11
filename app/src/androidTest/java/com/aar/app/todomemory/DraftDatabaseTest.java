package com.aar.app.todomemory;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aar.app.todomemory.data.DraftDao;
import com.aar.app.todomemory.data.ToDoDatabase;
import com.aar.app.todomemory.model.Draft;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DraftDatabaseTest {

    private DraftDao mDraftDao;
    private ToDoDatabase mToDoDatabase;

    @Before
    public void setup() {
        Context ctx = InstrumentationRegistry.getTargetContext();
        mToDoDatabase = Room.inMemoryDatabaseBuilder(ctx, ToDoDatabase.class).build();
        mDraftDao = mToDoDatabase.getDraftDao();
    }

    @After
    public void shutdown() {
        mToDoDatabase.close();
    }

    @Test
    public void testUpdateAndReadDraft() {
        Draft d = new Draft("content", true);
        mDraftDao.update(d);
        d = new Draft("content2", false);
        mDraftDao.update(d);

        Draft d2 = mDraftDao.getDraft();

        assertEquals(d.getContent(), d2.getContent());
        assertEquals(d.isImportant(), d2.isImportant());
    }

    @Test
    public void testClearDraft() {
        Draft d = new Draft("content", true);
        mDraftDao.update(d);

        mDraftDao.clear();

        Draft d2 = mDraftDao.getDraft();

        assertNull(d2);
    }
}
