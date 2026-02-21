package screens.onboarding;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import screens.BaseScreen;

import java.time.Duration;

public class CameraAccessScreen extends BaseScreen {

    private final By laterBtn = AppiumBy.accessibilityId("Later");
    private final By authorizeBtn = AppiumBy.accessibilityId("Authorize");
    private final By title = AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Camera access\")");

    public CameraAccessScreen(AppiumDriver driver) { super(driver); }

    public boolean isShown() {
        return exists(laterBtn, Duration.ofMillis(300))
                || exists(authorizeBtn, Duration.ofMillis(300))
                || exists(title, Duration.ofMillis(300));
    }

    public void handle() {
        if (tapIfExists(laterBtn, Duration.ofSeconds(1))) return;
        if (tapIfExists(authorizeBtn, Duration.ofSeconds(1))) return;
        tapAndroidPermissionAllowIfPresent();
    }
}












