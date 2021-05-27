package com.atomicrobot.carbon.app


import com.atomicrobot.carbon.ui.devsettings.DevSettingsActivity
interface VariantApplicationComponent {
    fun inject(activity: DevSettingsActivity)
}
