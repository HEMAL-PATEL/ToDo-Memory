package com.aar.app.todomemory.todolist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.Utils;
import com.aar.app.todomemory.model.ToDo;
import com.aar.app.todomemory.settings.SettingsProvider;


public class ToDoListFragment extends Fragment {

    private ToDoListAdapter mToDoListAdapter = new ToDoListAdapter();
    private ToDoListViewModel mViewModel;

    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView recyclerViewToDos;
    private TextView textEmpty;
    private ImageButton notifyMeButton;

    public ToDoListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ToDoListViewModel.class);
        mToDoListAdapter.setOnToDoClickListener(todo -> {
            if (isActivityNavListener()) {
                getNavigationClickListener().onGoToEdit(todo.getId());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBottomButtons(view);
        mCoordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        recyclerViewToDos = view.findViewById(R.id.recyclerViewToDo);
        recyclerViewToDos.setAdapter(mToDoListAdapter);
        recyclerViewToDos.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemSwipeCallback() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    mViewModel.delete(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    mViewModel.done(position);
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewToDos);

        mViewModel.getToDosLiveData().observe(this, todos -> {
            if (todos != null && !todos.isEmpty()) {
                unDimNotifyMeButton();
                textEmpty.setVisibility(View.GONE);
                recyclerViewToDos.setVisibility(View.VISIBLE);

                setTextAlignmentOnAdapter();
                mToDoListAdapter.replaceData(todos);
            } else {
                dimNotifyMeButton();
                textEmpty.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.getNotificationEnableState().observe(this, this::onNotifyMeEnableState);
        mViewModel.getOnToDoDeleted().observe(this, this::onDeleted);
        mViewModel.getOnToDoDone().observe(this, mToDoListAdapter::notifyItemChanged);
        mViewModel.getOnUndo().observe(this, this::onUndo);

        textEmpty = view.findViewById(R.id.textEmpty);
    }

    private void setTextAlignmentOnAdapter() {
        if (mViewModel.getTodoTextAlignment() == SettingsProvider.ALIGN_LEFT) {
            mToDoListAdapter.setItemTextAligment(Gravity.START);
        } else if (mViewModel.getTodoTextAlignment() == SettingsProvider.ALIGN_CENTER) {
            mToDoListAdapter.setItemTextAligment(Gravity.CENTER);
        } else if (mViewModel.getTodoTextAlignment() == SettingsProvider.ALIGN_RIGHT) {
            mToDoListAdapter.setItemTextAligment(Gravity.END);
        }
    }

    private void onDeleted(int index) {
        mToDoListAdapter.removeAt(index);
        Snackbar.make(mCoordinatorLayout, "To-do successfully deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> mViewModel.undo())
                .show();
    }

    private void onUndo(Pair<Integer, ToDo> undoObj) {
        unDimNotifyMeButton();
        textEmpty.setVisibility(View.GONE);
        mToDoListAdapter.insertAt(undoObj.first, undoObj.second);
    }

    private void onNotifyMeEnableState(Boolean enabled) {
        if (enabled) {
            int color = Utils.getColorThemeAttribute(getContext(), R.attr.colorAccent);
            notifyMeButton.setImageResource(R.drawable.ic_notifications);
            notifyMeButton.getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        } else {
            notifyMeButton.setImageResource(R.drawable.ic_notifications_none);
        }
    }

    private void initBottomButtons(View view) {
        notifyMeButton = view.findViewById(R.id.buttonNotify);
        View addBtn = view.findViewById(R.id.buttonAdd);
        View historyBtn = view.findViewById(R.id.buttonHistory);
        View settingsBtn = view.findViewById(R.id.buttonSettings);

        notifyMeButton.setOnClickListener(this::onNotifyMeButtonClick);
        addBtn.setOnClickListener(this::onAddButtonClick);
        historyBtn.setOnClickListener(this::onHistoryButtonClick);
        settingsBtn.setOnClickListener(this::onSettingsButtonClick);
    }

    private void dimNotifyMeButton() {
        notifyMeButton.setAlpha(0.5f);
        notifyMeButton.setEnabled(false);
    }

    private void unDimNotifyMeButton() {
        notifyMeButton.setAlpha(1f);
        notifyMeButton.setEnabled(true);
    }

    private void onAddButtonClick(View v) {
        if (isActivityNavListener()) {
            getNavigationClickListener().onGoToAdd();
        }
    }

    private void onNotifyMeButtonClick(View v) {
        mViewModel.switchEnableNotification();
    }

    private void onHistoryButtonClick(View v) {
        if (isActivityNavListener()) {
            getNavigationClickListener().onGoToHistory();
        }
    }

    private void onSettingsButtonClick(View v) {
        if (isActivityNavListener()) {
            getNavigationClickListener().onGoToSettings();
        }
    }

    private OnNavigationClickListener getNavigationClickListener() {
        return (OnNavigationClickListener) getActivity();
    }

    private boolean isActivityNavListener() {
        return getActivity() instanceof OnNavigationClickListener;
    }

    public interface OnNavigationClickListener {
        void onGoToAdd();
        void onGoToEdit(long todoId);
        void onGoToHistory();
        void onGoToSettings();
    }
}
