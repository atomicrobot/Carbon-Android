package com.mycompany.myapp.app


import com.mycompany.myapp.ui.devsettings.DevSettingsComponent
import com.mycompany.myapp.ui.devsettings.DevSettingsComponent.DevSettingsModule

interface VariantApplicationComponent {
    fun devSettingsComponent(module: DevSettingsModule): DevSettingsComponent
}
