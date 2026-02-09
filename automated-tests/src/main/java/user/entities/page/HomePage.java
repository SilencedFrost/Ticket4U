package user.entities.page;

import common.constants.Constant;
import common.utility.Utilities;
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
        return new HomePage();
    }
}
