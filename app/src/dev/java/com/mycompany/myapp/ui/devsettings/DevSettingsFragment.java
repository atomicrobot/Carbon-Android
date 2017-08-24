package com.mycompany.myapp.ui.devsettings;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.myapp.R;

import javax.inject.Inject;

public class DevSettingsFragment extends Fragment {
    public interface DevSettingsFragmentHost {
        void inject(DevSettingsFragment fragment);
    }

    @Inject DevSettingsPresenter presenter;
    private DevSettingsFragmentBinding binding;
    private DevSettingsFragmentHost host;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        host = (DevSettingsFragmentHost) context;
        host.inject(this);
    }

    @Override
    public void onDetach() {
        host = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dev_settings, container, false);
        binding.setPresenter(presenter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding.unbind();
        super.onDestroyView();
    }
}
