package com.mycompany.myapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mycompany.myapp.BuildConfig;
import com.mycompany.myapp.R;
import com.mycompany.myapp.ui.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTextChanged;
import hugo.weaving.DebugLog;
import icepick.Icepick;
import icepick.Icicle;

public class MainFragment extends BaseFragment<MainComponent> {

    @InjectView(R.id.editText)
    EditText editText;

    @InjectView(R.id.version)
    TextView versionView;

    @InjectView(R.id.fingerprint)
    TextView fingerprintView;

    @Icicle
    String enteredText;

    @Override
    protected void inject(MainComponent component) {
        component.inject(this);
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        Icepick.restoreInstanceState(this, savedInstanceState);
        return view;
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();

        editText.setText(enteredText);
        versionView.setText(String.format("Version: %s", BuildConfig.VERSION_NAME));
        fingerprintView.setText(String.format("Fingerprint: %s", BuildConfig.VERSION_FINGERPRINT));

        crashReporter.logMessage("woohoo from the fragment!");

        logger.i("Fragment is ready to go!");
    }

    @OnTextChanged(R.id.editText)
    public void onEditTextChanged(CharSequence text) {
        this.enteredText = text.toString();
    }
}
