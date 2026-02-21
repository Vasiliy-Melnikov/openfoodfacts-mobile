package screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

import java.time.Duration;

public class ScanScreen extends BaseScreen {

    private final By welcome = a11yContains("Welcome to Open");
    private final By scanOrSearch = a11y("Scan a barcode or search for a product");
    private final By searchField = a11y("Search for a product");

    public ScanScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return existsAny(Duration.ofSeconds(4), searchField, scanOrSearch, welcome);
    }

    public ScanScreen assertShown() {
        if (!isShown()) throw new AssertionError("Scan screen not shown");
        return this;
    }

    public void openSearch() {
        if (tapIfExists(searchField, Duration.ofSeconds(5))) return;
        if (tapIfExists(scanOrSearch, Duration.ofSeconds(3))) return;

        throw new AssertionError("Cannot open Search from Scan screen (content-desc not found)");
    }

    public SearchProductsScreen openSearchScreen() {
        assertShown();
        openSearch();
        return new SearchProductsScreen(driver).assertShown();
    }
}











