package com.aar.app.todomemory.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.model.ToDo;

import java.util.ArrayList;
import java.util.List;

public abstract class ArrayListAdapter<DT, VT extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<VT> {

    private List<DT> mData = new ArrayList<>();
    private OnToDoClickListener<DT> mListener;

    public void replaceData(List<DT> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public DT at(int position) {
        if (position < 0 || position >= mData.size())
            return null;
        return mData.get(position);
    }

    public List<DT> all() {
        return mData;
    }

    public void setOnToDoClickListener(OnToDoClickListener<DT> listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull VT holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                DT data = at(position);
                if (data != null) {
                    mListener.onToDoClick(data);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnToDoClickListener<T> {
        void onToDoClick(T data);
    }

}
