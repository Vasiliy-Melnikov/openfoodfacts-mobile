package drivers.capabilities;

import config.BrowserstackConfig;
import org.openqa.selenium.MutableCapabilities;

import java.util.HashMap;
import java.util.Map;

public class BrowserstackCapabilities {

    public static MutableCapabilities build(BrowserstackConfig cfg) {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("appium:newCommandTimeout", 120);
        caps.setCapability("appium:appWaitDuration", 20000); // 20s
        caps.setCapability("appium:androidInstallTimeout", 180000);
        caps.setCapability("appium:adbExecTimeout", 60000);
        caps.setCapability("appium:disableWindowAnimation", true);
        caps.setCapability("appium:waitForIdleTimeout", 0);
        caps.setCapability("appium:language", "en");
        caps.setCapability("appium:locale", "US");

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
