package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

public class SearchProductsScreen extends BaseScreen {
    private static final Duration SCREEN_MARKER_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration RESULTS_WAIT_TIMEOUT = Duration.ofMinutes(2);

    private final By markerSearchAProduct = a11yContains("Search a product");
    private final By markerSearch = a11yContains("Search");
    private final By anyClickableResultCard = AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"%\").clickable(true)"
    );

    private final By editText = AppiumBy.className("android.widget.EditText");

    public SearchProductsScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return exists(markerSearchAProduct, SCREEN_MARKER_TIMEOUT)
                || exists(markerSearch, SCREEN_MARKER_TIMEOUT)
                || exists(editText, SCREEN_MARKER_TIMEOUT);
    }

    public SearchProductsScreen assertShown() {
        if (!isShown()) throw new AssertionError("Search products screen not shown");
        return this;
    }

    public void search(String query) {
        try {
            WebElement active = driver.switchTo().activeElement();
            active.clear();
            active.sendKeys(query);
        } catch (Exception e) {
            if (!exists(editText, SCREEN_MARKER_TIMEOUT))
                throw new AssertionError("Search input not found (no focused element and no EditText)");
            tap(editText);
            typeSlow(editText, query);
        }
        pressEnter();
    }

    public void openFirstResultContaining(String namePart) {
        String safe = namePart.replace("\"", "\\\"");
        By byContaining = AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"" + safe + "\").clickable(true)"
        );

        long end = System.currentTimeMillis() + RESULTS_WAIT_TIMEOUT.toMillis();
        while (System.currentTimeMillis() < end) {
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

    public ProductDetailsScreen searchAndOpenFirst(String query, String expectedNamePart) {
        assertShown();
        search(query);
        openFirstResultContaining(expectedNamePart);
        return new ProductDetailsScreen(driver);
    }
}




