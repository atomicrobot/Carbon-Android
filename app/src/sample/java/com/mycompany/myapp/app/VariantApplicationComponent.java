package com.mycompany.myapp.app;


import com.mycompany.myapp.ui.main.SampleComponent;
import com.mycompany.myapp.ui.main.SampleComponent.SampleModule;

public interface VariantApplicationComponent {
    SampleComponent sampleComponent(SampleModule module);
}
