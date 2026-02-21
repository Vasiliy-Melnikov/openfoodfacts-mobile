package drivers;

import drivers.browserstack.BrowserstackDriver;
import drivers.local.Localdriver;
import io.appium.java_client.AppiumDriver;

public class MobileDriverFactory {

    public static AppiumDriver createDriver() {
        String host = System.getProperty("deviceHost", "local").toLowerCase();

        return switch (host) {
            case "local" -> Localdriver.create();
            case "browserstack" -> BrowserstackDriver.create();
            default -> throw new IllegalArgumentException("Unknown deviceHost: " + host + " (use local|browserstack)");
        };
    }
}