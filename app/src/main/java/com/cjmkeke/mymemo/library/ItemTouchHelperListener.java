package com.cjmkeke.mymemo.library;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemSwipe(int position);
    void onItemMoveFinished(RecyclerView recyclerView, int fromPosition, int toPosition);
}
