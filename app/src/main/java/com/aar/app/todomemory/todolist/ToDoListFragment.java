package com.aar.app.todomemory.todolist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.model.ToDo;
import com.aar.app.todomemory.settings.SettingsProvider;

import java.util.ArrayList;

public class ToDoListFragment extends Fragment {

    private ToDoListAdapter mToDoListAdapter = new ToDoListAdapter();
    private ToDoListViewModel mViewModel;

    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView recyclerViewToDos;
    private TextView textEmpty;
    private View buttonNotifyMe;

    public ToDoListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ToDoListViewModel.class);

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

        buttonNotifyMe = view.findViewById(R.id.buttonNotifyMe);
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
                buttonNotifyMe.setVisibility(View.VISIBLE);
                textEmpty.setVisibility(View.GONE);
                recyclerViewToDos.setVisibility(View.VISIBLE);

                setTextAlignmentOnAdapter();
                mToDoListAdapter.replaceData(todos);
            } else {
                buttonNotifyMe.setVisibility(View.GONE);
                textEmpty.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.getNotificationEnableState().observe(this, this::onNotifEnableState);
        mViewModel.getOnToDoDeleted().observe(this, this::onDeleted);
        mViewModel.getOnToDoDone().observe(this, mToDoListAdapter::notifyItemChanged);
        mViewModel.getOnUndo().observe(this, this::onUndo);

        textEmpty = view.findViewById(R.id.textEmpty);

        buttonNotifyMe.setOnClickListener(v -> {
            mViewModel.switchEnableNotification();
        });
    }

    public void setOnToDoClickListener(ToDoListAdapter.OnToDoClickListener<ToDo> listener) {
        mToDoListAdapter.setOnToDoClickListener(listener);
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
        textEmpty.setVisibility(View.GONE);
        mToDoListAdapter.insertAt(undoObj.first, undoObj.second);
    }

    private void onNotifEnableState(Boolean enabled) {
        TextView textView = buttonNotifyMe.findViewById(R.id.textViewNotifyMe);
        ImageView imageView = buttonNotifyMe.findViewById(R.id.imageViewNotifyMe);
        String text = "Notify me when screen turn on";
        if (enabled) {
            imageView.setImageResource(R.drawable.ic_notifications);
            textView.setText(text + " (enabled)");
        } else {
            imageView.setImageResource(R.drawable.ic_notifications_none);
            textView.setText(text + " (disabled)");
        }
    }
}
