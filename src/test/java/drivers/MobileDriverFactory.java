package drivers;

import drivers.browserstack.BrowserstackDriver;
import drivers.real.RealDeviceDriver;
import drivers.emulation.EmulatorDriver;
import io.appium.java_client.AppiumDriver;

public class MobileDriverFactory {

    public static AppiumDriver createDriver() {
        String host = System.getProperty("deviceHost", "browserstack").toLowerCase();

        return switch (host) {
            case "emulation" -> EmulatorDriver.create();
            case "real" -> RealDeviceDriver.create();
            case "browserstack" -> BrowserstackDriver.create();
            default -> throw new IllegalArgumentException("Unknown deviceHost: " + host);
        };
    }
}