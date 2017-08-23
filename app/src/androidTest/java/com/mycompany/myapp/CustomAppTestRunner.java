package com.mycompany.myapp;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnitRunner;

import com.linkedin.android.testbutler.TestButler;

public class CustomAppTestRunner extends AndroidJUnitRunner {
    @Override
    public void onStart() {
        runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Context context = InstrumentationRegistry.getTargetContext();
                TestButler.setup(context);
                unlockScreen(context, CustomAppTestRunner.class.getName());
                keepScreenAwake(context, CustomAppTestRunner.class.getName());
            }
        });

        super.onStart();
    }


    @Override
    public void finish(int resultCode, Bundle results) {
        TestButler.teardown(InstrumentationRegistry.getTargetContext());
        super.finish(resultCode, results);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, TestMainApplication.class.getName(), context);
    }

    private void keepScreenAwake(Context app, String name) {
        PowerManager power = (PowerManager) app.getSystemService(Context.POWER_SERVICE);
        power.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, name)
                .acquire(10*60*1000L /*10 minutes*/);
    }

    private void unlockScreen(Context app, String name) {
        KeyguardManager keyguard = (KeyguardManager) app.getSystemService(Context.KEYGUARD_SERVICE);
        keyguard.newKeyguardLock(name).disableKeyguard();
    }
}
