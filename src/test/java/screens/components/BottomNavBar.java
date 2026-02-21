package screens.components;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import screens.BaseScreen;
import screens.CommunityScreen;
import screens.ScanScreen;

import java.time.Duration;

public class BottomNavBar extends BaseScreen {

    private final By scanTab = AppiumBy.accessibilityId("Scan");
    private final By communityTab = AppiumBy.accessibilityId("Community");
    private final By listsTab = AppiumBy.accessibilityId("Lists");

    public BottomNavBar(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return existsAny(Duration.ofSeconds(3), scanTab, communityTab, listsTab);
    }

    public BottomNavBar assertShown() {
        if (!isShown()) throw new AssertionError("Bottom nav bar not shown");
        return this;
    }

    public void openScan() {
        if (!tapIfExists(scanTab, Duration.ofSeconds(5)))
            throw new AssertionError("Cannot open Scan tab (content-desc=Scan not found)");
    }

    public void openCommunity() {
        if (!tapIfExists(communityTab, Duration.ofSeconds(5)))
            throw new AssertionError("Cannot open Community tab (content-desc=Community not found)");
    }

    public void openLists() {
        if (!tapIfExists(listsTab, Duration.ofSeconds(5)))
            throw new AssertionError("Cannot open Lists tab (content-desc=Lists not found)");
    }

    public ScanScreen openScanTab() {
        assertShown();
        openScan();
        return new ScanScreen(driver).assertShown();
    }

    public CommunityScreen openCommunityTab() {
        assertShown();
        openCommunity();
        return new CommunityScreen(driver).assertShown();
    }

    public BottomNavBar openListsTab() {
        assertShown();
        openLists();
        return assertShown();
    }

    public void assertScanSelected() {
        openScanTab();
    }

    public void assertCommunitySelected() {
        openCommunityTab();
    }

    public void assertListsSelected() {
        openListsTab();
    }

    public void assertSelected(String tab) {
        String t = tab == null ? "" : tab.trim().toLowerCase();

        if (t.equals("scan")) { assertScanSelected(); return; }
        if (t.equals("community")) { assertCommunitySelected(); return; }
        if (t.equals("lists")) { assertListsSelected(); return; }

        throw new IllegalArgumentException("Unknown tab: " + tab);
    }
}






