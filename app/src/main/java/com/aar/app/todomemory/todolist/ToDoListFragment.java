package com.aar.app.todomemory.todolist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.model.ToDo;

public class ToDoListFragment extends Fragment {

    private ToDoListAdapter mToDoListAdapter = new ToDoListAdapter();
    private ToDoListViewModel mViewModel;

    public ToDoListFragment() {
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

        RecyclerView recyclerViewToDos = view.findViewById(R.id.recyclerViewToDo);
        recyclerViewToDos.setAdapter(mToDoListAdapter);
        recyclerViewToDos.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemSwipeCallback() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ToDo todo = mToDoListAdapter.at(viewHolder.getAdapterPosition());
                if (direction == ItemTouchHelper.LEFT) {
                    mViewModel.delete(todo);
                    mToDoListAdapter.removeAt(viewHolder.getAdapterPosition());
                } else if (direction == ItemTouchHelper.RIGHT) {
                    mViewModel.done(todo);
                    mToDoListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewToDos);

        TextView textEmpty = view.findViewById(R.id.textEmpty);

        mViewModel = ViewModelProviders.of(this).get(ToDoListViewModel.class);
        mViewModel.getToDosLiveData().observe(this, todos -> {
            if (todos != null && !todos.isEmpty()) {
                textEmpty.setVisibility(View.GONE);
                recyclerViewToDos.setVisibility(View.VISIBLE);

                mToDoListAdapter.replaceData(todos);
            } else {
                textEmpty.setVisibility(View.VISIBLE);
                recyclerViewToDos.setVisibility(View.GONE);
            }
        });

    }

    public void setOnToDoClickListener(ToDoListAdapter.OnToDoClickListener listener) {
        mToDoListAdapter.setOnToDoClickListener(listener);
    }
}
