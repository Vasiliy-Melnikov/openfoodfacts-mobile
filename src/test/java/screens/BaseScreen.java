package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;

public abstract class BaseScreen {

    protected final AppiumDriver driver;

    protected BaseScreen(AppiumDriver driver) {
        this.driver = driver;
    }

    protected By a11y(String id) {
        return AppiumBy.accessibilityId(id);
    }
    protected By a11yContains(String part) {
        return AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"" + part.replace("\"", "\\\"") + "\")"
        );
    }

    protected By byText(String text) {
        return AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + text.replace("\"", "\\\"") + "\")"
        );
    }

    protected By byTextContains(String text) {
        return AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"" + text.replace("\"", "\\\"") + "\")"
        );
    }
    protected By byHint(String hint) {
        return AppiumBy.xpath("//*[@class='android.widget.EditText' and @hint='" + hint + "']");
    }

    protected boolean scrollToTextContains(String text) {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().textContains(\"" + text.replace("\"", "\\\"") + "\"))"
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected FluentWait<AppiumDriver> waitOf(Duration timeout) {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    protected boolean exists(By locator, Duration timeout) {
        try {
            waitOf(timeout).until(d -> !d.findElements(locator).isEmpty());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean exists(By locator) {
        return exists(locator, Duration.ofSeconds(2));
    }

    @SafeVarargs
    protected final boolean existsAny(Duration timeout, By... locators) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < end) {
            for (By l : locators) {
                if (!driver.findElements(l).isEmpty()) return true;
            }
            sleep(200);
        }
        return false;
    }

    protected WebElement waitVisible(By locator, Duration timeout) {
        return waitOf(timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        waitVisible(locator, Duration.ofSeconds(10)).click();
    }

    protected boolean clickIfExists(By locator) {
        try {
            List<WebElement> els = driver.findElements(locator);
            if (els.isEmpty()) return false;
            els.get(0).click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean clickIfExists(By locator, Duration timeout) {
        if (!exists(locator, timeout)) return false;
        return clickIfExists(locator);
    }

    protected void tap(By locator) { click(locator); }
    protected boolean tapIfExists(By locator) { return clickIfExists(locator); }
    protected boolean tapIfExists(By locator, Duration timeout) { return clickIfExists(locator, timeout); }

    protected void typeSlow(By locator, String text) {
        WebElement el = waitVisible(locator, Duration.ofSeconds(10));
        el.click();
        el.clear();
        el.sendKeys(text);
    }

    protected void pressEnter() {
        try {
            if (driver instanceof AndroidDriver ad) {
                ad.pressKey(new KeyEvent(AndroidKey.ENTER));
                return;
            }
        } catch (Exception ignored) {}

        try {
            driver.switchTo().activeElement().sendKeys(Keys.ENTER);
        } catch (Exception ignored) {}
    }

    protected void waitShort() {
        sleep(250);
    }

    protected void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    protected boolean tapAndroidPermissionAllowIfPresent() {
        By allow1 = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button");
        By allow2 = AppiumBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
        By allow3 = AppiumBy.id("com.android.packageinstaller:id/permission_allow_button");

        return tapIfExists(allow2)
                || tapIfExists(allow1)
                || tapIfExists(allow3)
                || tapIfExists(byTextContains("Allow"))
                || tapIfExists(byTextContains("While using"));
    }
    protected List<WebElement> waitAllPresent(By locator, Duration timeout) {
        return waitOf(timeout).until(d -> {
            List<WebElement> els = d.findElements(locator);
            return els.isEmpty() ? null : els;
        });
    }

    protected void typeSlow(WebElement el, String text) {
        el.click();
        el.clear();
        el.sendKeys(text);
    }
}










