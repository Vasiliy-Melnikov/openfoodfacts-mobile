package screens;

import com.github.javafaker.Faker;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class CreateAccountScreen extends BaseScreen {

    private final By title = a11y("Create account");
    private final By signUpBtn = a11y("Sign Up");

    private final By agreeText = a11yContains("I agree to the Open Food Facts");

    private final By agreeSwitchNearText = AppiumBy.xpath(
            "//*[@class='android.widget.TextView' and contains(@content-desc,'I agree')]"
                    + "/following::android.widget.Switch[1]"
    );

    private final By anySwitch = AppiumBy.className("android.widget.Switch");
    private final By successDialog = a11yContains("Congratulations! Your account has just been created.");
    private final By okayBtn = a11y("Okay");
    private final By anyEditText = AppiumBy.className("android.widget.EditText");

    public CreateAccountScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return existsAny(Duration.ofSeconds(4), title, signUpBtn);
    }

    public CreateAccountScreen assertShown() {
        if (!isShown()) throw new AssertionError("Create account screen not shown");
        return this;
    }

    public void fillForm(String name, String email, String username, String password) {
        List<WebElement> fields = waitForAtLeastEditTexts(5, Duration.ofSeconds(15));

        typeInto(fields, 0, name);
        typeInto(fields, 1, email);
        typeInto(fields, 2, username);
        typeInto(fields, 3, password);

        fields = ensureFieldIndexAvailable(fields, 4, Duration.ofSeconds(10));
        typeInto(fields, 4, password);
    }

    private List<WebElement> waitForAtLeastEditTexts(int minCount, Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        List<WebElement> fields = driver.findElements(anyEditText);

        while (System.currentTimeMillis() < end) {
            fields = driver.findElements(anyEditText);
            if (fields.size() >= minCount) return fields;
            tryScrollForwardOnce();
            waitShort();
        }

        throw new AssertionError("Expected at least " + minCount + " EditText fields, but found " + fields.size());
    }

    private List<WebElement> ensureFieldIndexAvailable(List<WebElement> current, int index, Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        List<WebElement> fields = current;

        while (System.currentTimeMillis() < end) {
            fields = driver.findElements(anyEditText);
            if (fields.size() > index) return fields;

            tryScrollForwardOnce();
            waitShort();
        }

        throw new AssertionError("EditText with index " + index + " not found. Found only " + fields.size());
    }

    private void typeInto(List<WebElement> fields, int index, String value) {
        if (fields.size() <= index) {
            throw new AssertionError("Not enough EditText fields. Need index " + index + ", but size is " + fields.size());
        }

        WebElement el = fields.get(index);

        try {
            el.click();
        } catch (Exception ignored) {}

        try {
            el.clear();
        } catch (Exception ignored) {}

        try {
            el.sendKeys(value);
        } catch (Exception e) {
            List<WebElement> refreshed = driver.findElements(anyEditText);
            if (refreshed.size() > index) {
                try {
                    refreshed.get(index).click();
                } catch (Exception ignored) {}
                refreshed.get(index).sendKeys(value);
            } else {
                throw e;
            }
        }

        waitShort();
    }

    private void tryScrollForwardOnce() {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"
            ));
        } catch (Exception ignored) {
        }
    }

    public void enableAgree() {
        scrollToTextContains("I agree");

        if (tapIfExists(agreeSwitchNearText, Duration.ofSeconds(2))) return;
        if (tapIfExists(agreeText, Duration.ofSeconds(2))) return;

        try {
            var switches = driver.findElements(anySwitch);
            if (!switches.isEmpty()) {
                switches.get(0).click();
                return;
            }
        } catch (Exception ignored) {}

        throw new AssertionError("Agree switch not found");
    }

    public void submitAndConfirmSuccessIfPresent() {
        tap(signUpBtn);

        if (exists(successDialog, Duration.ofSeconds(8))) {
            tapIfExists(okayBtn, Duration.ofSeconds(3));
        }
    }

    public void waitClosed(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < end) {
            if (!isShown()) return;
            waitShort();
        }
    }

    public void registerRandomAccountAndSubmit() {
        assertShown();

        Faker faker = new Faker(new Locale("en"));

        String name = faker.name().fullName();

        String raw = faker.name().username().toLowerCase();
        raw = raw.replaceAll("[^a-z0-9]", "");
        if (raw.length() < 6) raw = raw + faker.number().digits(6);
        String username = "vm" + raw + faker.number().digits(3);

        String email = "vm_" + faker.number().digits(6) + "@example.com";
        String password = "Aa1!" + faker.internet().password(8, 12, true, true, true);

        fillForm(name, email, username, password);
        enableAgree();
        submitAndConfirmSuccessIfPresent();
        waitClosed(Duration.ofSeconds(10));
    }
}








