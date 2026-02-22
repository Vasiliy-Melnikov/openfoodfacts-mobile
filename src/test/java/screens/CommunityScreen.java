package screens;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import screens.onboarding.OnboardingFlow;

import java.time.Duration;

public class CommunityScreen extends BaseScreen {

    private final By loggedOut = a11y("Logged out");
    private final By createAccount = a11y("Create account");

    private final By latestNews = a11yContains("Latest news");
    private final By helpUsInform = a11yContains("Help us inform");
    private final By contribute = a11yContains("Contribute");

    public CommunityScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return existsAny(Duration.ofSeconds(2),
                latestNews, helpUsInform, contribute,
                createAccount, loggedOut
        );
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
        return waitShown(isBrowserStack() ? Duration.ofSeconds(30) : Duration.ofSeconds(15));
    }

    @Step("Открыть форму создания аккаунта")
    public void openCreateAccount() {
        if (!tapIfExists(createAccount, Duration.ofSeconds(8))) {
            throw new AssertionError("Create account button not found on Community");
        }
    }

    @Step("Перейти на экран Create Account")
    public CreateAccountScreen openCreateAccountScreen() {
        waitShown(isBrowserStack() ? Duration.ofSeconds(40) : Duration.ofSeconds(20));
        openCreateAccount();
        return new CreateAccountScreen(driver).assertShown();
    }
}

