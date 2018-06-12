package com.aar.app.todomemory.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.model.History;
import com.aar.app.todomemory.todolist.ArrayListAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class HistoryListAdapter extends ArrayListAdapter<History, HistoryListAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(at(position));
        super.onBindViewHolder(holder, position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm - dd/MMM/yyyy", Locale.US);

        View layoutDelete;
        View layoutDone;
        View layoutItem;

        private TextView textContent;
        private TextView textDate;
        private View viewImportant;

        public ViewHolder(View itemView) {
            super(itemView);

            layoutDelete = itemView.findViewById(R.id.layoutDelete);
            layoutDone = itemView.findViewById(R.id.layoutDone);
            layoutItem = itemView.findViewById(R.id.layoutItem);

            textContent = itemView.findViewById(R.id.textContent);
            textDate = itemView.findViewById(R.id.textDate);
            viewImportant = itemView.findViewById(R.id.viewImportantIndicator);
        }

        void bind(History todo) {
            textContent.setText(todo.getContent());
            if (todo.isImportant()) {
                viewImportant.setVisibility(View.VISIBLE);
            } else {
                viewImportant.setVisibility(View.GONE);
            }

            textDate.setText("Done at " + DATE_FORMAT.format(todo.getDoneAt()));
        }
    }
}
