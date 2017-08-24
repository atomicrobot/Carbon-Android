package com.mycompany.myapp.util.databinding;

import android.databinding.BindingAdapter;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;

public class ViewAdapters {
    @BindingAdapter("visible")
    public static void setVisible(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("showing_progress")
    public static void setVisible(ContentLoadingProgressBar view, boolean visible) {
        if (visible) {
            view.show();
        } else {
            view.hide();
        }
    }
}
