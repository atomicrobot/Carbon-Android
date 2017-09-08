package com.mycompany.myapp.monitoring.model

import timber.log.Timber

class NoOpTree : Timber.Tree() {
    override fun log(priority: Int, tag: String, message: String, t: Throwable) { }

    override fun isLoggable(tag: String, priority: Int) = false
}
