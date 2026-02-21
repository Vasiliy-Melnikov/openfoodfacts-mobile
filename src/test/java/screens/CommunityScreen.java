package screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

import java.time.Duration;

public class CommunityScreen extends BaseScreen {

    private final By loggedOut = a11y("Logged out");
    private final By createAccount = a11y("Create account");

    public CommunityScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return exists(createAccount, Duration.ofSeconds(3)) || exists(loggedOut, Duration.ofSeconds(3));
    }

    public CommunityScreen assertShown() {
        if (!isShown()) throw new AssertionError("Community screen not shown");
        return this;
    }

    public void openCreateAccount() {
        if (!tapIfExists(createAccount, Duration.ofSeconds(5))) {
            throw new AssertionError("Create account button not found on Community");
        }
    }

    public CreateAccountScreen openCreateAccountScreen() {
        assertShown();
        openCreateAccount();
        return new CreateAccountScreen(driver).assertShown();
    }
}

