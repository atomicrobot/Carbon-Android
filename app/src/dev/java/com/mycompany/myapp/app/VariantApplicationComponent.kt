package com.mycompany.myapp.app


import com.mycompany.myapp.ui.devsettings.DevSettingsActivity

interface VariantApplicationComponent {
    fun inject(activity: DevSettingsActivity)
}
