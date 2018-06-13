package com.aar.app.todomemory.todolist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.model.ToDo;
import com.aar.app.todomemory.settings.SettingsProvider;

import java.util.ArrayList;

public class ToDoListFragment extends Fragment {

    private ToDoListAdapter mToDoListAdapter = new ToDoListAdapter();
    private ToDoListViewModel mViewModel;

    private RecyclerView recyclerViewToDos;
    private TextView textEmpty;

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
                textEmpty.setVisibility(View.GONE);
                recyclerViewToDos.setVisibility(View.VISIBLE);

                setTextAlignmentOnAdapter();
                mToDoListAdapter.replaceData(todos);
            } else {
                textEmpty.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.getOnToDoDeleted().observe(this, mToDoListAdapter::removeAt);
        mViewModel.getOnToDoDone().observe(this, mToDoListAdapter::notifyItemChanged);

        textEmpty = view.findViewById(R.id.textEmpty);
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
}
