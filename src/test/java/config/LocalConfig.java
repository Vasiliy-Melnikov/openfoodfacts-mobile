package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/local.properties"
})
public interface LocalConfig extends Config {

    @Key("appium.url")
    @DefaultValue("http://127.0.0.1:4723/wd/hub")
    String appiumUrl();

    @Key("platformName")
    @DefaultValue("Android")
    String platformName();

    @Key("automationName")
    @DefaultValue("UiAutomator2")
    String automationName();

    @Key("app")
    @DefaultValue("apps/openfoodfacts.apk")
    String app();

    @Key("appPackage")
    @DefaultValue("org.openfoodfacts.scanner")
    String appPackage();

    @Key("appActivity")
    @DefaultValue("")
    String appActivity();

    @Key("udid")
    @DefaultValue("emulator-5554")
    String udid();

    @Key("deviceName")
    @DefaultValue("emulator-5554")
    String deviceName();

    @Key("platformVersion")
    @DefaultValue("14")
    String platformVersion();

    @Key("reinstallApp")
    @DefaultValue("true")
    boolean reinstallApp();
}

