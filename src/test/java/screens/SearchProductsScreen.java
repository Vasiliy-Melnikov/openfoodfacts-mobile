package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import screens.onboarding.OnboardingFlow;

import java.time.Duration;
import java.util.List;

public class SearchProductsScreen extends BaseScreen {
    private static final Duration SCREEN_MARKER_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration SCREEN_OPEN_TIMEOUT = Duration.ofSeconds(25);
    private static final Duration RESULTS_WAIT_TIMEOUT = Duration.ofMinutes(2);

    private Duration resultsLoadTimeout() {
        return isBrowserStack() ? Duration.ofMinutes(2) : Duration.ofSeconds(60);
    }
    private final By markerSearchAProduct = a11yContains("Search a product");
    private final By markerSearch = a11yContains("Search");

    private final By searchInput = AppiumBy.xpath(
            "//*[@class='android.widget.EditText' and contains(@content-desc,'Search a product')]"
    );
    private final By anyEditText = AppiumBy.className("android.widget.EditText");

    private final By anyClickableResultCard = AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"%\").clickable(true)"
    );

    public SearchProductsScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return exists(markerSearchAProduct, SCREEN_MARKER_TIMEOUT)
                || exists(markerSearch, SCREEN_MARKER_TIMEOUT)
                || exists(searchInput, SCREEN_MARKER_TIMEOUT)
                || exists(anyEditText, SCREEN_MARKER_TIMEOUT);
    }

    public SearchProductsScreen waitShown(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < end) {
            new OnboardingFlow(driver).passIfPresent();
            if (isShown()) return this;
            sleep(300);
        }
        throw new AssertionError("Search products screen not shown within " + timeout.toSeconds() + "s");
    }

    @Step("Проверить, что открыт экран поиска товаров")
    public SearchProductsScreen assertShown() {
        return waitShown(SCREEN_OPEN_TIMEOUT);
    }

    @Step("Выполнить поиск: {query}")
    public void search(String query) {
        waitShown(SCREEN_OPEN_TIMEOUT);
        tapIfExists(markerSearchAProduct, Duration.ofSeconds(3));
        tapIfExists(markerSearch, Duration.ofSeconds(2));
        if (exists(searchInput, Duration.ofSeconds(15))) {
            typeSlow(searchInput, query);
        } else if (exists(anyEditText, Duration.ofSeconds(15))) {
            typeSlow(anyEditText, query);
        } else {
            throw new AssertionError("Search input not found on SearchProducts screen");
        }

        pressEnter();

        waitResultsLoaded(resultsLoadTimeout());
    }
    public void waitResultsLoaded(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();

        while (System.currentTimeMillis() < end) {
            new OnboardingFlow(driver).passIfPresent();
            if (!driver.findElements(anyClickableResultCard).isEmpty()) return;

            sleep(400);
        }

        throw new AssertionError("Search results not loaded within " + timeout.toSeconds() + "s");
    }


    @Step("Открыть первый результат (ожидаем часть названия: {namePart})")
    public void openFirstResultContaining(String namePart) {
        By byContaining = AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"" + namePart.replace("\"", "\\\"") + "\").clickable(true)"
        );

        long end = System.currentTimeMillis() + RESULTS_WAIT_TIMEOUT.toMillis();
        while (System.currentTimeMillis() < end) {
            new OnboardingFlow(driver).passIfPresent();

            List<WebElement> matched = driver.findElements(byContaining);
            if (!matched.isEmpty()) {
                matched.get(0).click();
                return;
            }

            List<WebElement> any = driver.findElements(anyClickableResultCard);
            if (!any.isEmpty()) {
                any.get(0).click();
                return;
            }

            waitShort();
        }

        throw new AssertionError("No clickable results found within " + RESULTS_WAIT_TIMEOUT.getSeconds()
                + "s (expected part: " + namePart + ")");
    }

    @Step("Найти товар '{query}' и открыть первый результат")
    public ProductDetailsScreen searchAndOpenFirst(String query, String expectedNamePart) {
        assertShown();
        search(query);
        openFirstResultContaining(expectedNamePart);
        return new ProductDetailsScreen(driver);
    }
}




