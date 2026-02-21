package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/browserstack.properties"
})
public interface BrowserstackConfig extends Config {

    @Key("bs.user")
    @DefaultValue("")
    String user();

    @Key("bs.key")
    @DefaultValue("")
    String key();

    @Key("bs.url")
    @DefaultValue("https://hub-cloud.browserstack.com/wd/hub")
    String url();

    @Key("bs.app")
    String app();

    @Key("bs.project")
    @DefaultValue("Mobile")
    String project();

    @Key("bs.build")
    @DefaultValue("local-build")
    String build();

    @Key("bs.name")
    @DefaultValue("Mobile tests")
    String name();

    @Key("bs.device")
    @DefaultValue("Google Pixel 7")
    String device();

    @Key("bs.osVersion")
    @DefaultValue("13.0")
    String osVersion();

    @Key("bs.platformName")
    @DefaultValue("Android")
    String platformName();

    @Key("bs.automationName")
    @DefaultValue("UiAutomator2")
    String automationName();
}
