package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import screens.ProductDetailsScreen;
import screens.components.BottomNavBar;

import static io.qameta.allure.Allure.step;

@Tag("android")
public class OpenFoodFactsE2ETests extends TestBase {

    @Test
    void searchNutellaAndOpenFirstProduct() {
        ProductDetailsScreen product = step("Open Search and find Nutella", () ->
                new BottomNavBar(driver)
                        .openScanTab()
                        .openSearchScreen()
                        .searchAndOpenFirst("nutella", "Nutella")
        );

        step("Verify product details title contains 'Nutella'", () ->
                product.assertOpenedWithTitleContains("Nutella")
        );
    }

    @Test
    void canSwitchBetweenTabsScanCommunityLists() {
        BottomNavBar nav = new BottomNavBar(driver);

        step("Open Scan tab", nav::openScanTab);
        step("Open Lists tab", nav::openListsTab);
        step("Open Community tab", nav::openCommunityTab);
    }

    @Test
    void canFillCreateAccountFormAndSubmit() {
        step("Open Create account and register random user", () ->
                new BottomNavBar(driver)
                        .openCommunityTab()
                        .openCreateAccountScreen()
                        .registerRandomAccountAndSubmit()
        );
    }
}


