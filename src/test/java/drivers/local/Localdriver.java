package drivers.local;

import config.LocalConfig;
import helpers.ApkInstaller;
import io.appium.java_client.android.AndroidDriver;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.MutableCapabilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Localdriver {

    public static AndroidDriver create() {
        try {
            LocalConfig cfg = ConfigFactory.create(LocalConfig.class, System.getProperties());

            String udid = System.getProperty("udid");
            if (udid == null || udid.isBlank()) udid = cfg.udid();

            if (cfg.reinstallApp()) {
                ApkInstaller.reinstall(udid, cfg.appPackage(), cfg.app());
            }

            String launchableActivity = resolveLaunchableActivity(udid, cfg.appPackage());
            String appActivity = normalizeActivity(cfg.appActivity());
            if (appActivity == null) appActivity = launchableActivity;

            MutableCapabilities caps = new MutableCapabilities();
            caps.setCapability("platformName", cfg.platformName());
            caps.setCapability("appium:automationName", cfg.automationName());

            caps.setCapability("appium:deviceName", cfg.deviceName());
            caps.setCapability("appium:udid", udid);
            caps.setCapability("appium:platformVersion", cfg.platformVersion());

            caps.setCapability("appium:appPackage", cfg.appPackage());
            caps.setCapability("appium:appActivity", appActivity);
            caps.setCapability("appium:appWaitPackage", cfg.appPackage());
            caps.setCapability("appium:appWaitActivity", "*");
            caps.setCapability("appium:appWaitDuration", 30000);

            caps.setCapability("appium:noReset", true);
            caps.setCapability("appium:fullReset", false);
            caps.setCapability("appium:disableWindowAnimation", true);

            caps.setCapability("appium:autoGrantPermissions", true);
            caps.setCapability("appium:newCommandTimeout", 60);
            caps.setCapability("appium:skipLogcatCapture", true);
            caps.setCapability("appium:waitForIdleTimeout", 0);

            return new AndroidDriver(new URL(cfg.appiumUrl()), caps);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create local Android driver", e);
        }
    }

    private static String resolveLaunchableActivity(String udid, String appPackage) {
        String out = adbOutLastLine(udid,
                "shell", "cmd", "package", "resolve-activity", "--brief",
                "-c", "android.intent.category.LAUNCHER",
                appPackage
        );

        if (out == null || out.isBlank() || !out.contains("/")) {
            return appPackage + ".MainActivity";
        }

        String activity = out.substring(out.indexOf("/") + 1).trim();
        if (activity.startsWith(".")) return appPackage + activity;
        if (activity.contains(".")) return activity;
        return appPackage + "." + activity;
    }

    private static String normalizeActivity(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;
        if (s.startsWith(".")) s = s.substring(1);
        return s;
    }

    private static String adbOutLastLine(String udid, String... args) {
        try {
            String[] cmd = new String[args.length + 3];
            cmd[0] = "adb";
            cmd[1] = "-s";
            cmd[2] = udid;
            System.arraycopy(args, 0, cmd, 3, args.length);

            Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();

            String last = null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isBlank()) last = line.trim();
                }
            }
            p.waitFor();
            return last;
        } catch (Exception e) {
            return null;
        }
    }
}