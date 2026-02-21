package screens.onboarding;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import screens.BaseScreen;

import java.time.Duration;

public class AllowCameraUseScreen extends BaseScreen {

    private final By allowBtn = AppiumBy.accessibilityId("Allow");
    private final By title    = AppiumBy.accessibilityId("Allow camera use to scan barcodes");

    public AllowCameraUseScreen(AppiumDriver driver) { super(driver); }

    public boolean isShown() {
        return exists(allowBtn, Duration.ofMillis(300)) || exists(title, Duration.ofMillis(300));
    }

    public void allow() { tapIfExists(allowBtn, Duration.ofSeconds(1)); }
}




