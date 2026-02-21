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
    private static final Duration SCREEN_OPEN_TIMEOUT = Duration.ofSeconds(20);
    private static final Duration RESULTS_WAIT_TIMEOUT = Duration.ofMinutes(2);

    private final By markerSearchAProduct = a11yContains("Search a product");
    private final By markerSearch = a11yContains("Search");
    private final By editText = AppiumBy.className("android.widget.EditText");

    private final By anyClickableResultCard = AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"%\").clickable(true)"
    );

    public SearchProductsScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return exists(markerSearchAProduct, SCREEN_MARKER_TIMEOUT)
                || exists(markerSearch, SCREEN_MARKER_TIMEOUT)
                || exists(editText, SCREEN_MARKER_TIMEOUT);
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
        try {
            WebElement active = driver.switchTo().activeElement();
            active.click();
            active.clear();
            active.sendKeys(query);
        } catch (Exception e) {
            tap(editText);
            typeSlow(editText, query);
        }

        pressEnter();
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




