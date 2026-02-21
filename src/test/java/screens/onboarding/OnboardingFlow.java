package screens.onboarding;

import io.appium.java_client.AppiumDriver;
import screens.BaseScreen;

public class OnboardingFlow extends BaseScreen {

    private final AllowCameraUseScreen allowCamera;
    private final CameraAccessScreen cameraAccess;
    private final WelcomeScreen welcome;
    private final NextScreen next;
    private final CountryScreen country;

    public OnboardingFlow(AppiumDriver driver) {
        super(driver);
        this.allowCamera = new AllowCameraUseScreen(driver);
        this.cameraAccess = new CameraAccessScreen(driver);
        this.welcome = new WelcomeScreen(driver);
        this.next = new NextScreen(driver);
        this.country = new CountryScreen(driver);
    }

    public void passIfPresent() {
        long end = System.currentTimeMillis() + 40_000;

        while (System.currentTimeMillis() < end) {
            try {
                tapAndroidPermissionAllowIfPresent();

                if (cameraAccess.isShown()) { cameraAccess.handle(); sleep(80); continue; }
                if (allowCamera.isShown())  { allowCamera.allow();  sleep(80); continue; }
                if (welcome.isShown())      { welcome.next();       sleep(80); continue; }
                if (next.isShown())         { next.tapNext();       sleep(80); continue; }
                if (country.isShown())      { country.next();       sleep(80); continue; }

                return;
            } catch (org.openqa.selenium.WebDriverException ignored) {
                sleep(120);
            }
        }
    }
}




