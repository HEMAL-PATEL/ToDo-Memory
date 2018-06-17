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

    enum ViewState {
        NEW_EMPTY_TODO,
        EDIT_DRAFT,
        EDIT_TODO,
        EDIT_DONE
    }

    private ToDoDao mToDoDao;
    private DraftDao mDraftDao;

    private long mToDoId;
    private ViewState mViewState;

    private MutableLiveData<ToDo> mToDoLiveData = new MutableLiveData<>();
    private MutableLiveData<ViewState> mStateLiveData = new MutableLiveData<>();

    public ToDoEditorViewModel(@NonNull Application application) {
        super(application);
        mToDoDao = ToDoDatabase.getInstance(application).getToDoDao();
        mDraftDao = ToDoDatabase.getInstance(application).getDraftDao();
    }

    public void initToDo(long todoId) {
        if (todoId <= 0) {
            newToDo();
        } else {
            editToDo(todoId);
        }
    }

    public void clear() {
        if (mViewState == ViewState.EDIT_DRAFT) {
            mDraftDao.clear();
            mViewState = ViewState.NEW_EMPTY_TODO;
            mStateLiveData.setValue(mViewState);
        }
    }

    public void saveDraft(String content, boolean important) {
        content = content.trim();
        if (content.isEmpty()) {
            return;
        }

        if (mViewState != ViewState.EDIT_TODO && mViewState != ViewState.EDIT_DONE) {
            mDraftDao.update(new Draft(content, important));
        }
    }

    public void saveToDo(String content, boolean important, boolean isDone) {
        content = content.trim();
        if (content.isEmpty()) {
            return;
        }

        int todoState = isDone ? ToDo.STATE_DONE : ToDo.STATE_NONE;
        if (mViewState == ViewState.EDIT_TODO) {
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

        mViewState = ViewState.EDIT_DONE;
        mStateLiveData.setValue(mViewState);
    }

    public LiveData<ToDo> getToDoLiveData() {
        return mToDoLiveData;
    }

    public LiveData<ViewState> getViewStateLiveData() {
        return mStateLiveData;
    }

    private void newToDo() {
        Draft draft = mDraftDao.getDraft();
        if (draft == null) {
            mViewState = ViewState.NEW_EMPTY_TODO;
        } else {
            mViewState = ViewState.EDIT_DRAFT;
            mToDoLiveData.setValue(toDoFromDraft(draft));
        }

        mStateLiveData.setValue(mViewState);
    }

    private void editToDo(long todoId) {
        mToDoId = todoId;
        mViewState = ViewState.EDIT_TODO;

        mToDoLiveData.setValue(mToDoDao.getBy(mToDoId));
        mStateLiveData.setValue(mViewState);
    }

    private ToDo toDoFromDraft(Draft draft) {
        return new ToDo(0, draft.getContent(), ToDo.STATE_NONE, draft.isImportant());
    }
}
