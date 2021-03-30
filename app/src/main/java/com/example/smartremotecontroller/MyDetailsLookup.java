package com.example.smartremotecontroller;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class MyDetailsLookup extends ItemDetailsLookup {

    private RecyclerView r;

    public MyDetailsLookup(RecyclerView recyclerView) {
        this.r = recyclerView;
    }

    @Nullable
    @Override



    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = r.findChildViewUnder(e.getX(), e.getY());
        if(view != null) {
            return ((CustomAdapter.ViewHolder)r.getChildViewHolder(view)).getItemDetails();
        }
        return null;
    }
}
