package tests;

import com.codeborne.selenide.Selenide;
import helpers.AllureAttachments;
import helpers.BrowserstackApi;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.*;
import screens.ScanScreen;
import screens.components.BottomNavBar;
import screens.onboarding.OnboardingFlow;

import java.time.Duration;

@Tag("android")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {

    protected AppiumDriver driver;

    private boolean isBrowserStack() {
        String hostRaw = System.getProperty("deviceHost");
        String host = (hostRaw == null || hostRaw.isBlank()) ? "local" : hostRaw.toLowerCase();
        return host.equals("browserstack");
    }

    @BeforeEach
    void setUp() {
        if (isBrowserStack()) {
            if (driver == null) {
                driver = drivers.MobileDriverFactory.createDriver();
            }
        } else {
            driver = drivers.MobileDriverFactory.createDriver();
        }

        new OnboardingFlow(driver).passIfPresent();

        Duration readyTimeout = isBrowserStack()
                ? Duration.ofSeconds(120)
                : Duration.ofSeconds(60);

        ensureAppReady(readyTimeout);
    }

    @AfterEach
    void tearDown() {
        try { AllureAttachments.screenshot(driver); } catch (Exception ignored) {}
        try { AllureAttachments.pageSource(driver); } catch (Exception ignored) {}
        if (!isBrowserStack()) {
            try { driver.quit(); } catch (Exception ignored) {}
        }

        try { Selenide.closeWebDriver(); } catch (Exception ignored) {}
    }

    @AfterAll
    void afterAll() {
        if (!isBrowserStack()) return;

        try {
            String sessionId = getSessionId();
            if (!sessionId.isBlank()) {
                new BrowserstackApi().attachVideo(sessionId);
            }
        } catch (Exception ignored) {}

        try { driver.quit(); } catch (Exception ignored) {}
    }

    private String getSessionId() {
        try {
            var sid = driver.getSessionId();
            return sid == null ? "" : sid.toString();
        } catch (Exception ignored) {
            return "";
        }
    }

    private void ensureAppReady(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();

        BottomNavBar nav = new BottomNavBar(driver);
        ScanScreen scan = new ScanScreen(driver);

        while (System.currentTimeMillis() < end) {
            try {
                new OnboardingFlow(driver).passIfPresent();
                tapAndroidPermissionAllowIfPresent();
                if (scan.isShown()) return;

                if (!nav.isShown()) { sleep(400); continue; }
                nav.openScan();
                scan.waitShown(Duration.ofSeconds(20));
                return;

            } catch (Exception ignored) {
                sleep(400);
            }
        }

        throw new AssertionError("App not ready within " + timeout.toSeconds() + "s (Scan screen not shown)");
    }

    private boolean tapAndroidPermissionAllowIfPresent() {
        try {
            var allow1 = io.appium.java_client.AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button");
            var allow2 = io.appium.java_client.AppiumBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");
            var allow3 = io.appium.java_client.AppiumBy.id("com.android.packageinstaller:id/permission_allow_button");

            var els = driver.findElements(allow2);
            if (!els.isEmpty()) { els.get(0).click(); return true; }

            els = driver.findElements(allow1);
            if (!els.isEmpty()) { els.get(0).click(); return true; }

            els = driver.findElements(allow3);
            if (!els.isEmpty()) { els.get(0).click(); return true; }

        } catch (Exception ignored) {}
        return false;
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}









