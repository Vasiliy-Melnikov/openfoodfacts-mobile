package tests;

import com.codeborne.selenide.Configuration;
import helpers.AllureAttachments;
import helpers.ApkInstaller;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.*;
import screens.ScanScreen;
import screens.components.BottomNavBar;
import screens.onboarding.OnboardingFlow;

import java.net.URL;
import java.time.Duration;

@Tag("android")
public class TestBase {

    protected AppiumDriver driver;
    protected static final String APP_PACKAGE = "org.openfoodfacts.scanner";

    @BeforeEach
    void setUp() {
        Configuration.browserSize = null;

        String host = System.getProperty("deviceHost", "emulation").toLowerCase();

        if (!host.equals("browserstack")) {
            String udid = System.getProperty("udid", "emulator-5554");
            String apkAbsPath = resolveApkAbsolutePath();
            ApkInstaller.reinstall(udid, APP_PACKAGE, apkAbsPath);
        }

        driver = drivers.MobileDriverFactory.createDriver();

        new OnboardingFlow(driver).passIfPresent();
        ensureAppReady(Duration.ofSeconds(60));
    }

    @AfterEach
    void tearDown() {
        try { AllureAttachments.screenshot(driver); } catch (Exception ignored) {}
        try { AllureAttachments.pageSource(driver); } catch (Exception ignored) {}
        try { driver.quit(); } catch (Exception ignored) {}
    }

    private String resolveApkAbsolutePath() {
        try {
            URL url = getClass().getClassLoader().getResource("apps/openfoodfacts.apk");
            if (url == null) throw new IllegalStateException("APK not found in resources: apps/openfoodfacts.apk");
            return new java.io.File(url.toURI()).getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve apk path from resources", e);
        }
    }

    private void ensureAppReady(Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();

        while (System.currentTimeMillis() < end) {
            try {
                new OnboardingFlow(driver).passIfPresent();

                BottomNavBar nav = new BottomNavBar(driver);
                if (!nav.isShown()) { sleep(300); continue; }

                nav.openScan();

                ScanScreen scan = new ScanScreen(driver);
                if (scan.isShown()) return;

            } catch (Exception ignored) {}

            sleep(300);
        }

        throw new AssertionError("App not ready within " + timeout.toSeconds() + "s (Scan screen not shown)");
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}












