package com.mycompany.myapp

import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnitRunner

import com.linkedin.android.testbutler.TestButler

class CustomAppTestRunner : AndroidJUnitRunner() {
    override fun onStart() {
        runOnMainSync {
            val context = InstrumentationRegistry.getTargetContext()
            TestButler.setup(context)
            unlockScreen(context, CustomAppTestRunner::class.java.name)
            keepScreenAwake(context, CustomAppTestRunner::class.java.name)
        }

        super.onStart()
    }

    override fun finish(resultCode: Int, results: Bundle) {
        TestButler.teardown(InstrumentationRegistry.getTargetContext())
        super.finish(resultCode, results)
    }

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestMainApplication::class.java.name, context)
    }

    @Suppress("DEPRECATION")
    private fun keepScreenAwake(app: Context, name: String) {
        val power = app.getSystemService(Context.POWER_SERVICE) as PowerManager
        power.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE, name)
                .acquire(10 * 60 * 1000L /*10 minutes*/)
    }

    @Suppress("DEPRECATION")
    private fun unlockScreen(app: Context, name: String) {
        val keyguard = app.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguard.newKeyguardLock(name).disableKeyguard()
    }
}
