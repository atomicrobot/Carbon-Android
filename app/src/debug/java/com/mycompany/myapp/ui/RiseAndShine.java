package com.mycompany.myapp.ui;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.WindowManager;

/**
 * Inspired from https://gist.github.com/JakeWharton/f50f3b4d87e57d8e96e9
 */
@SuppressWarnings("deprecation")
public class RiseAndShine {
    private static final int LOCK_FLAGS = PowerManager.FULL_WAKE_LOCK
            | PowerManager.ACQUIRE_CAUSES_WAKEUP
            | PowerManager.ON_AFTER_RELEASE;

    public static void riseAndShine(Activity activity) {
        KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock(activity.getLocalClassName());
        keyguardLock.disableKeyguard();

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        WakeLock lock = powerManager.newWakeLock(LOCK_FLAGS, "wakeup!");

        lock.acquire();
        lock.release();
    }
}
