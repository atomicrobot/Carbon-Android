package com.mycompany.myapp.ui.devsettings;

import android.test.suitebuilder.annotation.MediumTest;

import com.mycompany.myapp.EspressoTestRule;
import com.mycompany.myapp.MainApplicationDaggerMockRule;
import com.mycompany.myapp.R;
import com.squareup.spoon.Spoon;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import rx.Observable;

@MediumTest
public class DevSettingsActivityEspressoTests {

    @Rule public MainApplicationDaggerMockRule mockitoRule = new MainApplicationDaggerMockRule();

    @Rule public EspressoTestRule<DevSettingsActivity> activityRule = new EspressoTestRule<>(DevSettingsActivity.class, false, false);

    @Test
    public void testLaunchActivity() {
        DevSettingsActivity activity = activityRule.launchActivity(null);
        Spoon.screenshot(activity, "launch_activity");
    }
}
