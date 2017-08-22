package com.mycompany.myapp.ui.devsettings;

import android.support.test.rule.ActivityTestRule;
import com.mycompany.myapp.MainApplicationDaggerMockRule;
import org.junit.Rule;
import org.junit.Test;

public class DevSettingsActivityEspressoTests {

    @Rule public MainApplicationDaggerMockRule mockitoRule = new MainApplicationDaggerMockRule();

    @Rule public ActivityTestRule<DevSettingsActivity> activityRule = new ActivityTestRule<>(DevSettingsActivity.class, false, false);

    @Test
    public void testLaunchActivity() {
        DevSettingsActivity activity = activityRule.launchActivity(null);
        //Spoon.screenshot(activity, "launch_activity");
    }
}
