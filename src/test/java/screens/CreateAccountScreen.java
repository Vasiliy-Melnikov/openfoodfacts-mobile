package screens;

import com.github.javafaker.Faker;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class CreateAccountScreen extends BaseScreen {

    private final By title = a11y("Create account");
    private final By signUpBtn = a11y("Sign Up");
    private final By editTexts = AppiumBy.className("android.widget.EditText");

    private final By agreeText = a11yContains("I agree to the Open Food Facts");
    private final By agreeRowByDesc = a11yContains("I agree");
    private final By agreeRowByText = byTextContains("I agree");

    private final By agreeSwitchNearText = AppiumBy.xpath(
            "//*[@class='android.widget.TextView' and contains(@content-desc,'I agree')]" +
                    "/following::android.widget.Switch[1]"
    );

    private final By anySwitch = AppiumBy.className("android.widget.Switch");
    private final By successDialog = a11yContains("Congratulations! Your account has just been created.");
    private final By okayBtn = a11y("Okay");

    public CreateAccountScreen(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return existsAny(Duration.ofSeconds(4), title, signUpBtn);
    }

    @Step("Проверить, что открыт экран Create Account")
    public CreateAccountScreen assertShown() {
        if (!isShown()) throw new AssertionError("Create account screen not shown");
        return this;
    }

    @Step("Заполнить форму регистрации")
    public void fillForm(String name, String email, String username, String password) {
        List<WebElement> fields = waitOf(Duration.ofSeconds(15)).until(d -> {
            List<WebElement> els = d.findElements(editTexts);
            return els.size() >= 5 ? els : null;
        });

        typeSlow(fields.get(0), name);
        typeSlow(fields.get(1), email);
        typeSlow(fields.get(2), username);
        typeSlow(fields.get(3), password);
        typeSlow(fields.get(4), password);
    }

    @Step("Принять соглашение")
    public void enableAgree() {
        scrollToTextContains("I agree");
        if (tapIfExists(agreeSwitchNearText, Duration.ofSeconds(2))) return;
        if (tapIfExists(agreeText, Duration.ofSeconds(2))) return;
        if (tapIfExists(agreeRowByDesc, Duration.ofSeconds(2))) return;
        if (tapIfExists(agreeRowByText, Duration.ofSeconds(2))) return;
        try {
            var switches = driver.findElements(anySwitch);
            if (!switches.isEmpty()) {
                switches.get(0).click();
                return;
            }
        } catch (Exception ignored) {}

        throw new AssertionError("Agree switch not found");
    }

    @Step("Нажать Sign Up и подтвердить успех, если появится диалог")
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

    @Step("Зарегистрировать случайный аккаунт")
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








