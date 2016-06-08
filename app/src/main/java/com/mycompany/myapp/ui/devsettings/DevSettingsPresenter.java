package com.mycompany.myapp.ui.devsettings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mycompany.myapp.BuildConfig;
import com.mycompany.myapp.R;
import com.mycompany.myapp.util.RxUtils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DevSettingsPresenter {
    private static final String EXTRA_STATE = "DevSettingsPresenterState";

    public interface DevSettingsViewContract {
        
    }

    @Parcel
    public static class State {

    }

    private final Context context;

    private CompositeSubscription subscriptions;
    private DevSettingsViewContract view;
    private State state;

    public DevSettingsPresenter(Context context) {
        this.context = context;
    }

    public void setView(DevSettingsViewContract view) {
        this.view = view;
    }

    public void saveState(@NonNull  Bundle bundle) {
        bundle.putParcelable(EXTRA_STATE, Parcels.wrap(state));
    }

    public void restoreState(@Nullable Bundle bundle) {
        if (bundle != null && bundle.containsKey(EXTRA_STATE)) {
            state = Parcels.unwrap(bundle.getParcelable(EXTRA_STATE));
        }
    }

    public void onResume() {
        subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptions);
        if (state == null) {
            state = new State();
        }
    }

    public void onPause() {
        RxUtils.unsubscribeIfNotNull(subscriptions);
    }
}
