package drivers.capabilities;

import config.BrowserstackConfig;
import org.openqa.selenium.MutableCapabilities;

import java.util.HashMap;
import java.util.Map;

public class BrowserstackCapabilities {

    public static MutableCapabilities build(BrowserstackConfig cfg) {
        MutableCapabilities caps = new MutableCapabilities();

        caps.setCapability("platformName", cfg.platformName());
        caps.setCapability("appium:automationName", cfg.automationName());
        caps.setCapability("appium:app", cfg.app());
        caps.setCapability("appium:autoGrantPermissions", true);
        caps.setCapability("appium:noReset", false);

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", cfg.user());
        bstackOptions.put("accessKey", cfg.key());
        bstackOptions.put("projectName", cfg.project());
        bstackOptions.put("buildName", cfg.build());
        bstackOptions.put("sessionName", cfg.name());

        bstackOptions.put("deviceName", cfg.device());
        bstackOptions.put("osVersion", cfg.osVersion());

        bstackOptions.put("debug", true);
        bstackOptions.put("networkLogs", true);

        caps.setCapability("bstack:options", bstackOptions);
        return caps;
    }
}
