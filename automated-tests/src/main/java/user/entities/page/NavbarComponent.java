package user.entities.page;

import common.constants.data.NavbarItem;
import common.utility.Utilities;
import org.openqa.selenium.By;

public class NavbarComponent extends CorePageEntity {

    // Locators
    private final By btnAccountDropdown = By.xpath("//i[contains(@class, 'bi-person-circle')]");
    private final By btnTicket = By.xpath("//div[contains(@class, 'text-clickable')][./i[contains(@class, 'bi-ticket')]]");

    private static final String dropdownMenuItemXpath = "//div[@class='menu-item-right']//a[contains(@href, '%s')]";

    // Methods
    @Override
    public boolean isComponentDisplayed() {
        return Utilities.isElementPresent(By.xpath("//nav"));
    }

    public CorePageEntity gotoPage(NavbarItem navbarItem) {
        return switch (navbarItem) {
            case LOGIN -> {
                Utilities.click(btnAccountDropdown);
                Utilities.click(By.xpath(String.format(dropdownMenuItemXpath, navbarItem.getHref())));
                yield new LoginPage();
            }
        };
    }

    public boolean isUserLoggedIn() {
        return Utilities.isElementPresent(btnTicket);
    }
}
