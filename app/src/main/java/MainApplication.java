package PACKAGE_NAME;

import android.app.Application;
import com.crashlytics.android.Crashlytics;

public class MainApplication extends Application {
    
    @Override
    public void onCreate() {
    	super.onCreate();

        setupCrashReporter();
    }

    private void setupCrashReporter() {
        //noinspection ConstantConditions,PointlessBooleanExpression
        if (!BuildConfig.DEBUG) {
            Crashlytics.start(this);
        }
    }
}