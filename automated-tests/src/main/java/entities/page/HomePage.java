package entities.page;

import common.constants.Constant;
import common.utility.Utilities;
import common.utility.WaitUtils;
import org.openqa.selenium.By;

public class HomePage extends NavbarComponent{

    // Locators

    // Methods
    @Override
    public boolean isComponentDisplayed() {
        return Utilities.isElementPresent(By.xpath("//div[contains(@class, 'home-page')]"));
    }

    public HomePage open() {
        Constant.WEBDRIVER.navigate().to(Constant.TICKET4U_URL);
        WaitUtils.waitForPageLoad();
        return new HomePage();
    }
}
