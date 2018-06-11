package com.aar.app.todomemory.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.model.ToDo;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    private List<ToDo> mData = new ArrayList<>();
    private OnToDoClickListener mListener;

    public void replaceData(List<ToDo> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public ToDo at(int position) {
        return mData.get(position);
    }

    public void setOnToDoClickListener(OnToDoClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnToDoClickListener {
        void onToDoClick(ToDo todo);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View layoutDelete;
        View layoutDone;
        View layoutItem;

        private TextView textContent;
        private ImageView imageDone;
        private View viewImportant;

        ViewHolder(View itemView) {
            super(itemView);

            layoutDelete = itemView.findViewById(R.id.layoutDelete);
            layoutDone = itemView.findViewById(R.id.layoutDone);
            layoutItem = itemView.findViewById(R.id.layoutItem);

            textContent = itemView.findViewById(R.id.textContent);
            imageDone = itemView.findViewById(R.id.imageDone);
            viewImportant = itemView.findViewById(R.id.viewImportantIndicator);
        }

        void bind(ToDo todo) {
            textContent.setText(todo.getContent());
            if (todo.isImportant()) {
                viewImportant.setVisibility(View.VISIBLE);
            } else {
                viewImportant.setVisibility(View.GONE);
            }

            if (todo.getState() == ToDo.STATE_DONE) {
                imageDone.setVisibility(View.VISIBLE);
            } else {
                imageDone.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (mListener != null) mListener.onToDoClick(todo);
            });
        }
    }

}
