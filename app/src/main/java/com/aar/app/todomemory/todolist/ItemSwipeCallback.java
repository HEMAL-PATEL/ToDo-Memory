package com.aar.app.todomemory.todolist;

import android.arch.persistence.room.ColumnInfo;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.aar.app.todomemory.R;
import com.aar.app.todomemory.model.ToDo;

public abstract class ItemSwipeCallback extends ItemTouchHelper.SimpleCallback {

    private Drawable mDeleteBackground;
    private Drawable mDeleteIcon;
    private Drawable mDoneBackground;
    private Drawable mDoneIcon;
    private int mIconHorizPadding;

    public ItemSwipeCallback(Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mDeleteBackground = new ColorDrawable(context.getResources().getColor(R.color.delete));
        mDoneBackground = new ColorDrawable(context.getResources().getColor(R.color.done));
        mDoneIcon = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_done));
        mDeleteIcon = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_delete));
        mIconHorizPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, context.getResources().getDisplayMetrics());
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // swipe to left
        if (dX <= 0) {
            mDeleteBackground.setBounds(
                    (int) (viewHolder.itemView.getRight() + dX),
                    viewHolder.itemView.getTop(),
                    viewHolder.itemView.getRight(),
                    viewHolder.itemView.getBottom()
                    );
            mDeleteBackground.draw(c);

            int vertCenter = mDeleteBackground.getBounds().top + (mDeleteBackground.getBounds().height() / 2);
            int bmpHalfHeight = mDeleteIcon.getIntrinsicHeight() / 2;
            int right = viewHolder.itemView.getRight() - mIconHorizPadding;
            mDeleteIcon.setBounds(
                    right - mDeleteIcon.getIntrinsicWidth(),
                    vertCenter - bmpHalfHeight,
                    right,
                    vertCenter + bmpHalfHeight
            );
            mDeleteIcon.draw(c);

            float offset = Math.max(0f, Math.min(1f, Math.abs(dX) / viewHolder.itemView.getWidth()));
            viewHolder.itemView.setAlpha(1f - offset);
        } else {
            mDoneBackground.setBounds(
                    0,
                    viewHolder.itemView.getTop(),
                    (int) dX,
                    viewHolder.itemView.getBottom()
            );
            mDoneBackground.draw(c);

            int vertCenter = mDoneBackground.getBounds().top + (mDoneBackground.getBounds().height() / 2);
            int bmpHalfHeight = mDoneIcon.getIntrinsicHeight() / 2;
            mDoneIcon.setBounds(
                    mIconHorizPadding,
                    vertCenter - bmpHalfHeight,
                    mIconHorizPadding + mDoneIcon.getIntrinsicWidth(),
                    vertCenter + bmpHalfHeight
            );
            mDoneIcon.draw(c);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
