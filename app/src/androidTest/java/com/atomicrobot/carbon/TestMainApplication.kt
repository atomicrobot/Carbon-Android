package com.atomicrobot.carbon

import com.atomicrobot.carbon.app.MainApplication

class TestMainApplication : MainApplication() {

    override fun initializeApplication() {
        // Don't initialize the application
    }

    override fun isTesting() = true
}
