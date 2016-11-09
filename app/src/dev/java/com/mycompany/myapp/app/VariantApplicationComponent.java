package com.mycompany.myapp.app;


import com.mycompany.myapp.ui.devsettings.DevSettingsComponent;
import com.mycompany.myapp.ui.devsettings.DevSettingsComponent.DevSettingsModule;

public interface VariantApplicationComponent {
    DevSettingsComponent devSettingsComponent(DevSettingsModule module);
}
