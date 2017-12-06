package com.mycompany.myapp

import com.mycompany.myapp.app.MainApplication

class TestMainApplication : MainApplication() {

    override fun initializeApplication() {
        // Don't initialize the application
    }

    override fun isTesting() = true
}
