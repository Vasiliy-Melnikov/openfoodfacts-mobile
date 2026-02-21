package tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import screens.ProductDetailsScreen;
import screens.components.BottomNavBar;

import static io.qameta.allure.SeverityLevel.*;

@Epic("Mobile")
@Feature("OpenFoodFacts")
@Story("E2E critical flows")
@Tag("android")
public class OpenFoodFactsE2ETests extends TestBase {

    @Test
    @Owner("Vasiliy Melnikov")
    @Severity(CRITICAL)
    @DisplayName("Поиск Nutella: открыть первый товар и проверить заголовок")
    void searchNutellaAndOpenFirstProduct() {
        ProductDetailsScreen product =
                new BottomNavBar(driver)
                        .openScanTab()
                        .openSearchScreen()
                        .searchAndOpenFirst("nutella", "Nutella");

        product.assertOpenedWithTitleContains("Nutella");
    }

    @Test
    @Owner("Vasiliy Melnikov")
    @Severity(NORMAL)
    @DisplayName("Навигация: переключение между вкладками Scan / Lists / Community")
    void canSwitchBetweenTabsScanCommunityLists() {
        BottomNavBar nav = new BottomNavBar(driver);

        nav.openScanTab();
        nav.openListsTab();
        nav.openCommunityTab();
    }

    @Test
    @Owner("Vasiliy Melnikov")
    @Severity(MINOR)
    @DisplayName("Community: заполнить форму Create Account и отправить")
    void canFillCreateAccountFormAndSubmit() {
        new BottomNavBar(driver)
                .openCommunityTab()
                .openCreateAccountScreen()
                .registerRandomAccountAndSubmit();
    }
}


