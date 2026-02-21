package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.time.Duration;

public class ProductDetailsScreen extends BaseScreen {

    private final By backBtn = AppiumBy.accessibilityId("Back");

    public ProductDetailsScreen(AppiumDriver driver) {
        super(driver);
    }

    @Step("Проверить, что открыта карточка товара и заголовок содержит: {expected}")
    public ProductDetailsScreen assertOpenedWithTitleContains(String expected) {
        By title = a11yContains(expected);
        if (!exists(title, Duration.ofSeconds(10))) {
            throw new AssertionError("Product title not found in content-desc contains: " + expected);
        }
        return this;
    }

    @Step("Вернуться назад из карточки товара")
    public void back() {
        if (tapIfExists(backBtn, Duration.ofSeconds(3))) return;
        driver.navigate().back();
    }
}




