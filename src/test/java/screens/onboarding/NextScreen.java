package screens.onboarding;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import screens.BaseScreen;

import java.time.Duration;

public class NextScreen extends BaseScreen {
    private final By nextBtn = AppiumBy.accessibilityId("Next");

    public NextScreen(AppiumDriver driver) { super(driver); }

    public boolean isShown() { return exists(nextBtn, Duration.ofMillis(300)); }
    public void tapNext() { tapIfExists(nextBtn, Duration.ofSeconds(1)); }

}
