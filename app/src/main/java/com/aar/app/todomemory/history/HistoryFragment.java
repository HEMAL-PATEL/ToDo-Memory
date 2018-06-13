package com.aar.app.todomemory.history;

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
import android.widget.Toast;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.model.History;

public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    private HistoryListAdapter mHistoryListAdapter = new HistoryListAdapter();

    public HistoryFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView emptyText = view.findViewById(R.id.textEmpty);
        RecyclerView recyclerViewToDos = view.findViewById(R.id.recyclerViewToDo);
        recyclerViewToDos.setAdapter(mHistoryListAdapter);
        recyclerViewToDos.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new HistorySwipeCallback() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    mViewModel.delete(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    mViewModel.reuse(position);
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewToDos);

        mViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        mViewModel.getToDoHistoryLiveData().observe(this, toDoHistories -> {
            if (toDoHistories != null && !toDoHistories.isEmpty()) {
                emptyText.setVisibility(View.GONE);
                recyclerViewToDos.setVisibility(View.VISIBLE);
                mHistoryListAdapter.replaceData(toDoHistories);
            } else {
                emptyText.setVisibility(View.VISIBLE);
                recyclerViewToDos.setVisibility(View.GONE);
            }
        });
        mViewModel.getOnHistoryReused().observe(this, index -> {
            mHistoryListAdapter.notifyItemChanged(index);
            Toast.makeText(getContext(), "Added to to-do list", Toast.LENGTH_SHORT).show();
        });
        mViewModel.getHistoryDeleted().observe(this, mHistoryListAdapter::removeAt);
    }
}
