package com.aar.app.todomemory;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.aar.app.todomemory.model.ToDo;
import com.aar.app.todomemory.data.ToDoDao;
import com.aar.app.todomemory.data.ToDoDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ToDoDatabaseTest {

    private ToDoDao mToDoDao;
    private ToDoDatabase mToDoDatabase;
    private Observer<List<ToDo>> observer;

    @Before
    public void setup() {
        Context ctx = InstrumentationRegistry.getTargetContext();
        mToDoDatabase = Room.inMemoryDatabaseBuilder(ctx, ToDoDatabase.class).build();
        mToDoDao = mToDoDatabase.getToDoDao();
        observer = toDos -> { };
    }

    @After
    public void shutdown() {
        mToDoDatabase.close();
    }

    @Test
    public void testInsertAndReadList() throws InterruptedException {
        ToDo todo = getToDoSample();
        mToDoDao.insert(todo);

        assertEquals(1, getValue(mToDoDao.getAll()).size());
        assertEquals("content", getValue(mToDoDao.getAll()).get(0).getContent());
    }

    @Test
    public void testUpdate() throws InterruptedException {
        ToDo todo = getToDoSample();
        long id = mToDoDao.insert(todo);
        todo.setId(id);

        todo.setContent("changed");
        mToDoDao.update(todo);

        assertEquals("changed", getValue(mToDoDao.getAll()).get(0).getContent());
    }

    @Test
    public void testDelete() throws InterruptedException {
        ToDo todo = getToDoSample();
        long id = mToDoDao.insert(todo);
        todo.setId(id);

        mToDoDao.delete(todo);

        assertEquals(0, getValue(mToDoDao.getAll()).size());
    }

    private ToDo getToDoSample() {
        return new ToDo(0, "content", 0, false);
    }

    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }
}
