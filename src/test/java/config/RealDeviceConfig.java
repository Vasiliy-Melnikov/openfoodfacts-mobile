package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "classpath:config/real.properties"
})
public interface RealDeviceConfig extends Config {

    @Key("appium.url")
    @DefaultValue("http://127.0.0.1:4723/wd/hub")
    String appiumUrl();

    @Key("platformName")
    @DefaultValue("Android")
    String platformName();

    @Key("automationName")
    @DefaultValue("UiAutomator2")
    String automationName();

    @Key("udid")
    @DefaultValue("8d36d42b")
    String udid();

    @Key("app")
    @DefaultValue("apps/openfoodfacts.apk")
    String app();

    @Key("appPackage")
    @DefaultValue("org.openfoodfacts.scanner")
    String appPackage();

    @Key("appActivity")
    @DefaultValue("")
    String appActivity();

    @Key("reinstallApp")
    @DefaultValue("true")
    boolean reinstallApp();
}

