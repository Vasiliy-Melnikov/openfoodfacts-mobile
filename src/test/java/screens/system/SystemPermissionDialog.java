package screens.system;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import screens.BaseScreen;

import java.time.Duration;

public class SystemPermissionDialog extends BaseScreen {

    private static final By ALLOW_1 = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button");
    private static final By ALLOW_2 = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
    private static final By ALLOW_3 = AppiumBy.id("com.android.packageinstaller:id/permission_allow_button");

    private static final By ALLOW_TXT = AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Allow\")");
    private static final By WHILE_USING = AppiumBy.androidUIAutomator("new UiSelector().textContains(\"While using\")");

    public SystemPermissionDialog(AppiumDriver driver) {
        super(driver);
    }

    public void acceptIfPresent(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();

        while (System.currentTimeMillis() < end) {
            boolean clicked =
                    tapIfExists(ALLOW_2, Duration.ofMillis(300)) ||
                            tapIfExists(ALLOW_1, Duration.ofMillis(300)) ||
                            tapIfExists(ALLOW_3, Duration.ofMillis(300)) ||
                            tapIfExists(WHILE_USING, Duration.ofMillis(300)) ||
                            tapIfExists(ALLOW_TXT, Duration.ofMillis(300));

            if (!clicked) return;

            sleep(150);
        }
    }
}

