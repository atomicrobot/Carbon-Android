package com.atomicrobot.carbon.ui

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.PowerManager
import android.view.WindowManager

/**
 * Inspired from https://gist.github.com/JakeWharton/f50f3b4d87e57d8e96e9
 */
@Suppress("DEPRECATION")
object RiseAndShine {
    private val LOCK_FLAGS = PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE

    fun riseAndShine(activity: Activity) {
        val keyguardManager = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val keyguardLock = keyguardManager.newKeyguardLock(activity.localClassName)
        keyguardLock.disableKeyguard()

        activity.window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)

        val powerManager = activity.getSystemService(Context.POWER_SERVICE) as PowerManager
        val lock = powerManager.newWakeLock(LOCK_FLAGS, "carbon:riseandshine")

        lock.acquire(1000)
        lock.release()
    }
}
