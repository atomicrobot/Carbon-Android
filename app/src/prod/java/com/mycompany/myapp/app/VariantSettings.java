package com.mycompany.myapp.app;

import android.content.Context;

import com.mycompany.myapp.R;

public class VariantSettings {
    private final Context context;

    public VariantSettings(Context context) {
        this.context = context;
    }

    public String getBaseUrl() {
        return context.getString(R.string.default_base_url);
    }
}
