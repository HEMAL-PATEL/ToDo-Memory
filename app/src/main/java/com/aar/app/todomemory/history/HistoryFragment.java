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

import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    private HistoryListAdapter mHistoryListAdapter = new HistoryListAdapter();

    private RecyclerView mRecyclerViewHistory;
    private TextView mEmptyText;

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

        View textClear = view.findViewById(R.id.textClear);
        mEmptyText = view.findViewById(R.id.textEmpty);
        mRecyclerViewHistory = view.findViewById(R.id.recyclerViewToDo);
        mRecyclerViewHistory.setAdapter(mHistoryListAdapter);
        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
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
        itemTouchHelper.attachToRecyclerView(mRecyclerViewHistory);

        mViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        mViewModel.getToDoHistoryLiveData().observe(this, this::historyListChanged);
        mViewModel.getOnHistoryReused().observe(this, this::onHistoryReused);
        mViewModel.getHistoryDeleted().observe(this, mHistoryListAdapter::removeAt);

        textClear.setOnClickListener(v -> mViewModel.clear());
    }

    private void historyListChanged(List<History> histories) {
        if (histories != null && !histories.isEmpty()) {
            mEmptyText.setVisibility(View.GONE);
            mRecyclerViewHistory.setVisibility(View.VISIBLE);
            mHistoryListAdapter.replaceData(histories);
        } else {
            mEmptyText.setVisibility(View.VISIBLE);
            mRecyclerViewHistory.setVisibility(View.GONE);
        }
    }

    private void onHistoryReused(int index) {
        mHistoryListAdapter.notifyItemChanged(index);
        Toast.makeText(getContext(), R.string.msg_todo_added, Toast.LENGTH_SHORT).show();
    }
}
