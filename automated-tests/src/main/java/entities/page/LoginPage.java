package entities.page;

import common.constants.Constant;
import common.utility.Utilities;
import common.utility.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import entities.data.UserAccount;

public class LoginPage extends CorePageEntity {

    // Locators
    private final By txtEmail = By.xpath("//div[contains(@class, 'border-start')]//input[@id='email']");
    private final By txtPassword = By.xpath("//div[contains(@class, 'border-start')]//input[@id='password']");

    private final By btnLogin = By.xpath("//div[contains(@class, 'border-start')]//button[contains(@class, 'btn-primary')]");

    private final By spinnerLoginButton = By.xpath("//div[contains(@class, 'border-start')]//button[contains(@class, 'btn-primary')]/span[contains(@class, 'spinner-border')]");

    // Methods
    @Override
    public boolean isComponentDisplayed() {
        return Utilities.isElementPresent(By.xpath("//div[@class='form-width'][./h3[.='Login:']]"));
    }

    public LoginPage login(String username, String password) {
        Utilities.findElement(txtEmail).sendKeys(username);
        WebElement txtPasswordElement = Utilities.findElement(txtPassword);
        txtPasswordElement.sendKeys(password);

        Utilities.click(btnLogin);

        WaitUtils.waitForElementStale(WaitUtils.waitForElementVisible(spinnerLoginButton));

        return this;
    }

    public LoginPage login(UserAccount userAccount) {
        return this.login(userAccount.getEmail(), userAccount.getPassword());
    }

    public HomePage expectSuccess() {
        return new HomePage();
    }

    public LoginPage expectFailure() {
        return this;
    }
}
