package com.mycompany.myapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycompany.myapp.BuildConfig;
import com.mycompany.myapp.R;
import com.mycompany.myapp.ui.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;

public class MainFragment extends BaseFragment<MainComponent> {

    @InjectView(R.id.version)
    TextView versionView;

    @InjectView(R.id.fingerprint)
    TextView fingerprintView;

    @Override
    protected void inject(MainComponent component) {
        component.inject(this);
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
        versionView.setText(String.format("Version: %s", BuildConfig.VERSION_NAME));
        fingerprintView.setText(String.format("Fingerprint: %s", BuildConfig.VERSION_FINGERPRINT));

        crashReporter.logMessage("woohoo from the fragment!");
    }
}
