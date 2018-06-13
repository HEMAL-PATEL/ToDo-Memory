package com.aar.app.todomemory.todolist;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.aar.app.todomemory.model.ToDo;

public abstract class ItemSwipeCallback extends ItemTouchHelper.SimpleCallback {

    public ItemSwipeCallback() {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        ToDoListAdapter adapter = (ToDoListAdapter) recyclerView.getAdapter();
        ToDo todo = adapter.at(viewHolder.getAdapterPosition());
        if (todo != null && todo.getState() == ToDo.STATE_DONE) {
            return makeMovementFlags(0, ItemTouchHelper.LEFT);
        }
        return super.getMovementFlags(recyclerView, viewHolder);
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
        if (Math.abs(dX) < viewHolder.itemView.getWidth()) {
            getDefaultUIUtil().onDraw(c, recyclerView, ((ToDoListAdapter.ViewHolder) viewHolder).layoutItem, dX/2, 0, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (Math.abs(dX) < viewHolder.itemView.getWidth()) {
            getDefaultUIUtil().onDrawOver(c, recyclerView, ((ToDoListAdapter.ViewHolder) viewHolder).layoutItem, dX/2, 0, actionState, isCurrentlyActive);
        }
    }

}
