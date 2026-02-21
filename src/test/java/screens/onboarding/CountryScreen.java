package screens.onboarding;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import screens.BaseScreen;

import java.time.Duration;

public class CountryScreen extends BaseScreen {
    public CountryScreen(AppiumDriver driver) { super(driver); }

    private final By nextBtn = a11y("Next");
    private final By title = AppiumBy.androidUIAutomator("new UiSelector().textContains(\"country\")");

    public boolean isShown() {
        return exists(nextBtn, Duration.ofMillis(300)) || exists(title, Duration.ofMillis(300));
    }

    public void next() { tapIfExists(nextBtn, Duration.ofSeconds(1)); }

}




