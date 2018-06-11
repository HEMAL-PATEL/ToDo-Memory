package com.aar.app.todomemory.todolist;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public abstract class ItemSwipeCallback extends ItemTouchHelper.SimpleCallback {

    public ItemSwipeCallback() {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(((ToDoListAdapter.ViewHolder) viewHolder).layoutItem);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            getDefaultUIUtil().onSelected(((ToDoListAdapter.ViewHolder) viewHolder).layoutItem);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDraw(c, recyclerView, ((ToDoListAdapter.ViewHolder) viewHolder).layoutItem, dX/2, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDrawOver(c, recyclerView, ((ToDoListAdapter.ViewHolder) viewHolder).layoutItem, dX/2, dY, actionState, isCurrentlyActive);
    }

}
