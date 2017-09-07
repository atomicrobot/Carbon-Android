package com.mycompany.myapp.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), LifecycleRegistryOwner {

    // We do this lazily to ensure any child classes have a chance to finish initializing
    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry
}
