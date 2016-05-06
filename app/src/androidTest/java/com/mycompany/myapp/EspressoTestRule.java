package com.mycompany.myapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;
import android.view.WindowManager;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Make sure this gets added to the manifest of the application under test (typically a manifest in the debug variant).
 *
 <code>
 <uses-permission android:name="android.permission.SET_ANIMATION_SCALE" />
 <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
 <uses-permission android:name="android.permission.WAKE_LOCK"/>
 </code>
 *
 * @param <T>
 */
public class EspressoTestRule<T extends Activity> extends ActivityTestRule<T> {
    private static final String TAG = EspressoTestRule.class.getSimpleName();

    private static final String ANIMATION_PERMISSION = "android.permission.SET_ANIMATION_SCALE";
    private static final float ANIMATION_DISABLED = 0.0f;
    private static final float ANIMATION_DEFAULT = 1.0f;

    // Giving the device a full second to turn the screen on.  This should be a one time hit.
    private static final long SCREEN_ON_ATTEMPT_DELAY = 1000;
    private static final int MAX_SCREEN_ON_ATTEMPTS = 20;

    public interface UIRunner {
        void run();
    }

    public EspressoTestRule(Class<T> activityClass) {
        super(activityClass);
    }

    public EspressoTestRule(Class<T> activityClass, boolean initialTouchMode) {
        super(activityClass, initialTouchMode);
    }

    public EspressoTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity) {
        super(activityClass, initialTouchMode, launchActivity);
    }

    @Override
    public Statement apply(Statement base, Description description) {
        try {
            disableAllAnimations();
            return super.apply(base, description);
        } finally {
            enableAllAnimations();
            closeAllActivities();
        }
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();
        final Activity activity = getActivity();
        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);

        int count = 0;
        while (!powerManager.isScreenOn() && count < MAX_SCREEN_ON_ATTEMPTS) {
            ui(new UIRunner() {
                @Override
                public void run() {
                    riseAndShine(activity);
                }
            });

            try {
                Thread.sleep(SCREEN_ON_ATTEMPT_DELAY);
            } catch (InterruptedException ex) {
                Log.e(TAG, "Couldn't sleep the test thread after trying to turn the screen on", ex);
            }
            count++;
        }

        if (count >= MAX_SCREEN_ON_ATTEMPTS) {
            Log.e(TAG, "Giving up trying to turn the screen on");
        }
    }

    public void ui(final UIRunner runner) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    runner.run();
                }
            });
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    // Inspired from https://gist.github.com/JakeWharton/f50f3b4d87e57d8e96e9
    @SuppressWarnings("deprecation")
    @SuppressLint("MissingPermission")
    private void riseAndShine(Activity activity) {
        KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(activity.getLocalClassName());
        keyguardLock.disableKeyguard();

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        WakeLock lock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "wakeup!");

        lock.acquire();
        lock.release();
    }

    private void disableAllAnimations() {
        if (getAnimationPermissionStatus() == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(ANIMATION_DISABLED);
        } else {
            Log.e(TAG, "Not granted permission to change animation scale.");
        }
    }

    private void enableAllAnimations() {
        if (getAnimationPermissionStatus() == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(ANIMATION_DEFAULT);
        } else {
            Log.e(TAG, "Not granted permission to change animation scale.");
        }
    }

    private int getAnimationPermissionStatus() {
        Context context = InstrumentationRegistry.getTargetContext();
        return context.checkCallingOrSelfPermission(ANIMATION_PERMISSION);
    }

    // https://code.google.com/p/android-test-kit/wiki/DisablingAnimations
    private void setSystemAnimationsScale(float animationScale) {
        try {
            Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
            Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
            Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
            Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales", float[].class);
            Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

            IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
            Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
            float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
            for (int i = 0; i < currentScales.length; i++) {
                currentScales[i] = animationScale;
            }
            setAnimationScales.invoke(windowManagerObj, new Object[]{currentScales});
        } catch (Exception ex) {
            Log.e(TAG, "Could not use reflection to change animation scale to: " + animationScale, ex);
        }
    }

    // See https://code.google.com/p/android-test-kit/issues/detail?id=66
    private void closeAllActivities() {
        try {
            Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
            closeAllActivities(instrumentation);
        } catch (Exception ex) {
            Log.e(TAG, "Could not use close all activities", ex);
        }
    }

    private void closeAllActivities(Instrumentation instrumentation) throws Exception {
        final int NUMBER_OF_RETRIES = 100;
        int i = 0;
        while (closeActivity(instrumentation)) {
            if (i++ > NUMBER_OF_RETRIES) {
                throw new AssertionError("Limit of retries excesses");
            }
            Thread.sleep(200);
        }
    }

    private boolean closeActivity(Instrumentation instrumentation) throws Exception {
        final Boolean activityClosed = callOnMainSync(instrumentation, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final Set<Activity> activities = getActivitiesInStages(Stage.RESUMED,
                        Stage.STARTED, Stage.PAUSED, Stage.STOPPED, Stage.CREATED);
                activities.removeAll(getActivitiesInStages(Stage.DESTROYED));
                if (activities.size() > 0) {
                    final Activity activity = activities.iterator().next();
                    activity.finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
        if (activityClosed) {
            instrumentation.waitForIdleSync();
        }
        return activityClosed;
    }

    private <X> X callOnMainSync(Instrumentation instrumentation, final Callable<X> callable) throws Exception {
        final AtomicReference<X> retAtomic = new AtomicReference<>();
        final AtomicReference<Throwable> exceptionAtomic = new AtomicReference<>();
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                try {
                    retAtomic.set(callable.call());
                } catch (Throwable e) {
                    exceptionAtomic.set(e);
                }
            }
        });
        final Throwable exception = exceptionAtomic.get();
        if (exception != null) {
            Throwables.propagateIfInstanceOf(exception, Exception.class);
            Throwables.propagate(exception);
        }
        return retAtomic.get();
    }

    public static Set<Activity> getActivitiesInStages(Stage... stages) {
        final Set<Activity> activities = Sets.newHashSet();
        final ActivityLifecycleMonitor instance = ActivityLifecycleMonitorRegistry.getInstance();
        for (Stage stage : stages) {
            final Collection<Activity> activitiesInStage = instance.getActivitiesInStage(stage);
            if (activitiesInStage != null) {
                activities.addAll(activitiesInStage);
            }
        }
        return activities;
    }
}

