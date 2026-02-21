package drivers.browserstack;

import config.BrowserstackConfig;
import drivers.capabilities.BrowserstackCapabilities;
import io.appium.java_client.android.AndroidDriver;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.MutableCapabilities;

import java.net.URL;

public class BrowserstackDriver {

    public static AndroidDriver create() {
        try {
            BrowserstackConfig cfg = ConfigFactory.create(BrowserstackConfig.class, System.getProperties());

            MutableCapabilities caps = BrowserstackCapabilities.build(cfg);

            return new AndroidDriver(
                    new URL(cfg.url()),
                    caps
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to create BrowserStack driver", e);
        }
    }
}
