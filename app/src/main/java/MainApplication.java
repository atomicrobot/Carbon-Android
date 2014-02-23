package PACKAGE_NAME;

import android.app.Application;
import com.crashlytics.android.Crashlytics;

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        //noinspection ConstantConditions,PointlessBooleanExpression
        if (BuildConfig.DEBUG) {
            setupDebugConfiguration();
        } else {
            setupReleaseConfiguration();
        }
    }

    private void setupDebugConfiguration() {
        android.util.Log.i(TAG, "Starting with the debug configuration.");
    }

    private void setupReleaseConfiguration() {
        Crashlytics.start(this);
    }
}