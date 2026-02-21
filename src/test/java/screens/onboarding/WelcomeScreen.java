package screens.onboarding;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import screens.BaseScreen;

import java.time.Duration;

public class WelcomeScreen extends BaseScreen {
    private final By continueBtn = AppiumBy.accessibilityId("Continue");

    public WelcomeScreen(AppiumDriver driver) { super(driver); }

    public boolean isShown() { return exists(continueBtn, Duration.ofMillis(300)); }
    public void next() { tapIfExists(continueBtn, Duration.ofSeconds(1)); }
}




