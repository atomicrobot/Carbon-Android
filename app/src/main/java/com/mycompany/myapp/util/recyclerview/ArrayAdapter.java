package com.mycompany.myapp.util.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class ArrayAdapter<Item, ItemViewHolder extends ViewHolder>
        extends RecyclerView.Adapter<ItemViewHolder> {

    private List<Item> items = new ArrayList<>();

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected Item getItemAtPosition(int position) {
        return items.get(position);
    }
}
