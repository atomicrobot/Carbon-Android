package com.mycompany.myapp.ui.devsettings;

import com.mycompany.myapp.EspressoTestRule;
import com.mycompany.myapp.MainApplicationDaggerMockRule;
import com.squareup.spoon.Spoon;

import org.junit.Rule;
import org.junit.Test;

public class DevSettingsActivityEspressoTests {

    @Rule public MainApplicationDaggerMockRule mockitoRule = new MainApplicationDaggerMockRule();

    @Rule public EspressoTestRule<DevSettingsActivity> activityRule = new EspressoTestRule<>(DevSettingsActivity.class, false, false);

    @Test
    public void testLaunchActivity() {
        DevSettingsActivity activity = activityRule.launchActivity(null);
        Spoon.screenshot(activity, "launch_activity");
    }
}
