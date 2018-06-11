package com.aar.app.todomemory.edittodo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.aar.app.todomemory.data.DraftDao;
import com.aar.app.todomemory.model.Draft;
import com.aar.app.todomemory.model.ToDo;
import com.aar.app.todomemory.data.ToDoDao;
import com.aar.app.todomemory.data.ToDoDatabase;

public class ToDoEditorViewModel extends AndroidViewModel {

    enum State {
        NEW_EMPTY_TODO,
        EDIT_DRAFT,
        EDIT_TODO,
        EDIT_DONE
    }

    private ToDoDao mToDoDao;
    private DraftDao mDraftDao;

    private long mToDoId;
    private State mState;

    private MutableLiveData<ToDo> mToDoLiveData = new MutableLiveData<>();
    private MutableLiveData<State> mStateLiveData = new MutableLiveData<>();

    public ToDoEditorViewModel(@NonNull Application application) {
        super(application);
        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
        mDraftDao = ToDoDatabase.getInstance(application).getDraftDao();
    }

    public void newToDo() {
        Draft draft = mDraftDao.getDraft();
        mState = State.EDIT_DRAFT;
        if (draft == null) {
            draft = new Draft();
            mState = State.NEW_EMPTY_TODO;
        }

        mStateLiveData.setValue(mState);
        mToDoLiveData.setValue(toDoFromDraft(draft));
    }

    public void editToDo(long todoId) {
        mToDoId = todoId;
        mState = State.EDIT_TODO;

        mToDoLiveData.setValue(mToDoDao.getBy(mToDoId));
        mStateLiveData.setValue(mState);
    }

    public void clear() {
        mToDoLiveData.setValue(new ToDo());
        if (mState == State.EDIT_DRAFT) {
            mDraftDao.clear();
            mState = State.NEW_EMPTY_TODO;
            mStateLiveData.setValue(mState);
        }
    }

    public void saveDraft(String content, boolean important) {
        content = content.trim();
        if (content.isEmpty()) {
            return;
        }

        if (mState != State.EDIT_TODO && mState != State.EDIT_DONE) {
            mDraftDao.update(new Draft(content, important));
        }
    }

    public void saveToDo(String content, boolean important, boolean isDone) {
        content = content.trim();
        if (content.isEmpty()) {
            return;
        }

        int todoState = isDone ? ToDo.STATE_DONE : ToDo.STATE_NONE;
        if (mState == State.EDIT_TODO) {
            ToDo todo = mToDoDao.getBy(mToDoId);
            todo.setContent(content);
            todo.setImportant(important);
            todo.setState(todoState);
            mToDoDao.update(todo);
        } else {
            ToDo todo = new ToDo(0, content, todoState, important);
            mToDoDao.insert(todo);
            mDraftDao.clear();
        }

        mState = State.EDIT_DONE;
        mStateLiveData.setValue(mState);
    }

    public LiveData<ToDo> getToDoLiveData() {
        return mToDoLiveData;
    }

    public LiveData<State> getStateLiveData() {
        return mStateLiveData;
    }

    private ToDo toDoFromDraft(Draft draft) {
        return new ToDo(0, draft.getContent(), ToDo.STATE_NONE, draft.isImportant());
    }
}
