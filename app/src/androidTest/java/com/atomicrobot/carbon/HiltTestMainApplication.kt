package com.atomicrobot.carbon

import com.atomicrobot.carbon.app.CoreApplication
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(CoreApplication::class)
interface HiltTestMainApplication