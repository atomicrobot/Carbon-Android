package com.atomicrobot.carbon

import com.atomicrobot.carbon.app.CoreApplication
import com.atomicrobot.carbon.app.MainApplication
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(CoreApplication::class)
interface HiltTestMainApplication