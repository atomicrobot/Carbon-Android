package PACKAGE_NAME;

import junit.framework.TestCase;

public class DummyTest extends InstrumentationTestCase {

    public void testAppNme() {
        Context context = getInstrumentation().getTargetContext();
        String appName = context.getString(R.string.app_name);
        assertEquals(APP_NAME, appName);
    }
}
