package com.mycompany.myapp.util.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.mycompany.myapp.util.recyclerview.ArrayAdapter;

import java.util.List;

public class RecyclerViewAdapters {
    /**
     * This adapter will fail if the RecyclerView isn't using our ArrayAdapter adapter or if
     * the type of the items passed in doesn't match what the adapter expects. But it does make
     * wiring up a RecyclerView with Data Binding very easy!
     */
    @BindingAdapter("items")
    public static void setItems(RecyclerView view, List<?> items) {
        ArrayAdapter arrayAdapter = (ArrayAdapter) view.getAdapter();
        //noinspection unchecked
        arrayAdapter.setItems(items);
    }
}
