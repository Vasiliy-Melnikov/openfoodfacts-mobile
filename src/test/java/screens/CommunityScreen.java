package screens;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import screens.onboarding.OnboardingFlow;

import java.time.Duration;

public class CommunityScreen extends BaseScreen {

    private final By loggedOut = a11y("Logged out");
    private final By createAccount = a11y("Create account");

    public CommunityScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return exists(createAccount, Duration.ofSeconds(2))
                || exists(loggedOut, Duration.ofSeconds(2));
    }

    public CommunityScreen waitShown(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < end) {
            new OnboardingFlow(driver).passIfPresent();
            if (isShown()) return this;
            sleep(300);
        }
        throw new AssertionError("Community screen not shown within " + timeout.toSeconds() + "s");
    }

    @Step("Проверить, что открыт экран Community")
    public CommunityScreen assertShown() {
        return waitShown(Duration.ofSeconds(10));
    }

    @Step("Открыть форму создания аккаунта")
    public void openCreateAccount() {
        if (!tapIfExists(createAccount, Duration.ofSeconds(8))) {
            throw new AssertionError("Create account button not found on Community");
        }
    }

    @Step("Перейти на экран Create Account")
    public CreateAccountScreen openCreateAccountScreen() {
        waitShown(Duration.ofSeconds(15));
        openCreateAccount();
        return new CreateAccountScreen(driver).assertShown();
    }
}

