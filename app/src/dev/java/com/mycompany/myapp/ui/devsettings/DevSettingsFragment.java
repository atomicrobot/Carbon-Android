package com.mycompany.myapp.ui.devsettings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.mycompany.myapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DevSettingsFragment extends Fragment {
    public interface DevSettingsFragmentHost {
        void inject(DevSettingsFragment fragment);

        void setTrustAllSSL(boolean trustAllSSL);

        void saveSettingsAndRestart();
    }

    private DevSettingsFragmentHost host;
    private Unbinder unbinder;

    @BindView(R.id.trust_all_ssl) CheckBox trustAllSSLView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        host = (DevSettingsFragmentHost) context;
    }

    @Override
    public void onDetach() {
        host = null;
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        host.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dev_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    public void displayTrustAllSSL(boolean trustAllSSL) {
        trustAllSSLView.setChecked(trustAllSSL);
    }

    @OnCheckedChanged(R.id.trust_all_ssl)
    public void handleOnTrustAllSSLChanged(boolean trustAllSSL) {
        host.setTrustAllSSL(trustAllSSL);
    }

    @OnClick(R.id.save_settings_and_restart)
    public void handleSaveSettingsAndRestart() {
        host.saveSettingsAndRestart();
    }
}
