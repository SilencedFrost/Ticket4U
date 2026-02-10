package user.entities.page;

import common.utility.Utilities;
import common.utility.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import user.entities.data.UserAccount;

public class LoginPage extends CorePageEntity {

    // Locators
    private final By txtEmail = By.xpath("//div[contains(@class, 'border-start')]//input[@id='email']");
    private final By txtPassword = By.xpath("//div[contains(@class, 'border-start')]//input[@id='password']");

    private final By btnLogin = By.xpath("//div[contains(@class, 'border-start')]//button[contains(@class, 'btn-primary')]");

    // Methods
    @Override
    public boolean isComponentDisplayed() {
        return Utilities.isElementPresent(By.xpath("//div[@class='form-width'][./h3[.='Login:']]"));
    }

    public CorePageEntity login(String username, String password) {
        Utilities.findElement(txtEmail).sendKeys(username);
        WebElement txtPasswordElement = Utilities.findElement(txtPassword);
        txtPasswordElement.sendKeys(password);

        Utilities.click(btnLogin);

        WaitUtils.waitForElementStale(txtPasswordElement);

        if(this.isComponentDisplayed()) return this;
        return new HomePage();
    }

    public CorePageEntity login(UserAccount userAccount) {
        return this.login(userAccount.getEmail(), userAccount.getPassword());
    }
}
