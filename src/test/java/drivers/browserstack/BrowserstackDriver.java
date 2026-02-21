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

            requireNotBlank(cfg.url(), "bs.url");
            requireNotBlank(resolveUser(cfg), "bs.user (or env BROWSERSTACK_USER/BROWSERSTACK_USERNAME)");
            requireNotBlank(resolveKey(cfg), "bs.key (or env BROWSERSTACK_KEY/BROWSERSTACK_ACCESS_KEY)");
            requireNotBlank(cfg.app(), "bs.app");

            MutableCapabilities caps = BrowserstackCapabilities.build(cfg);

            return new AndroidDriver(new URL(cfg.url()), caps);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create BrowserStack driver", e);
        }
    }

    private static String resolveUser(BrowserstackConfig cfg) {
        if (cfg.user() != null && !cfg.user().isBlank()) return cfg.user();
        String u = System.getenv("BROWSERSTACK_USERNAME");
        if (u != null && !u.isBlank()) return u;
        return System.getenv("BROWSERSTACK_USER");
    }

    private static String resolveKey(BrowserstackConfig cfg) {
        if (cfg.key() != null && !cfg.key().isBlank()) return cfg.key();
        String k = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (k != null && !k.isBlank()) return k;
        return System.getenv("BROWSERSTACK_KEY");
    }

    private static void requireNotBlank(String v, String name) {
        if (v == null || v.isBlank()) throw new IllegalStateException("Required property is empty: " + name);
    }
}
