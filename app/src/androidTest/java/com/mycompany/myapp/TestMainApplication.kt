package com.mycompany.myapp

import com.mycompany.myapp.app.MainApplication

class TestMainApplication : MainApplication() {

    override fun initializeApplication() {
        // Don't setupViewModel the application
    }
}
