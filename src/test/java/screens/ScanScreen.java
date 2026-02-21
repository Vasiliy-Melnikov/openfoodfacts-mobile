package screens;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import screens.onboarding.OnboardingFlow;

import java.time.Duration;

public class ScanScreen extends BaseScreen {

    private final By welcome = a11yContains("Welcome to Open");
    private final By scanOrSearch = a11y("Scan a barcode or search for a product");
    private final By searchField = a11y("Search for a product");

    public ScanScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return existsAny(Duration.ofSeconds(2), searchField, scanOrSearch, welcome);
    }

    public ScanScreen waitShown(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < end) {
            new OnboardingFlow(driver).passIfPresent();
            if (isShown()) return this;
            sleep(300);
        }
        throw new AssertionError("Scan screen not shown within " + timeout.toSeconds() + "s");
    }

    @Step("Проверить, что открыт экран Scan")
    public ScanScreen assertShown() {
        return waitShown(Duration.ofSeconds(15));
    }

    @Step("Открыть поиск на экране Scan")
    public void openSearch() {
        long end = System.currentTimeMillis() + Duration.ofSeconds(15).toMillis();

        while (System.currentTimeMillis() < end) {
            new OnboardingFlow(driver).passIfPresent();

            if (tapIfExists(searchField, Duration.ofSeconds(2))) return;
            if (tapIfExists(scanOrSearch, Duration.ofSeconds(2))) return;

            sleep(250);
        }

        throw new AssertionError("Cannot open Search from Scan screen (content-desc not found)");
    }

    @Step("Открыть экран поиска товаров")
    public SearchProductsScreen openSearchScreen() {
        waitShown(Duration.ofSeconds(20));
        openSearch();
        return new SearchProductsScreen(driver).waitShown(Duration.ofSeconds(25));
    }
}











