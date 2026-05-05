package entities.page;

import common.constants.Constant;
import common.utility.Utilities;
import common.utility.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import entities.data.UserAccount;

public class LoginPage extends CorePageEntity {

    public static class LOGIN_MODES {
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
    }

    // Locators
    private final By txtEmail = By.xpath("//div[@id='fullscreen-layout']//input[@id='identifier']");
    private final By txtPassword = By.xpath("//div[@id='fullscreen-layout']//input[@id='password']");

    private final By btnLogin = By.xpath("//div[@id='fullscreen-layout']//button[@id='submit-btn']");

    private final By spinnerLoginButton = By.xpath("//div[@id='fullscreen-layout']//button[@id='submit-btn']/span[contains(@class, 'spinner-border')]");

    private final By lblFormErrorMsg = By.xpath("//div[@id='fullscreen-layout']//div[@id='error-generic']");

    // Methods
    @Override
    public boolean isComponentDisplayed() {
        return Utilities.isElementPresent(By.xpath("//div[@class='form-width'][./h3[.='Login']]"));
    }

    public String getFormErrorMsg() {
        return Utilities.findElement(lblFormErrorMsg).getText();
    }

    public LoginPage login(String username, String password) {
        Utilities.findElement(txtEmail).sendKeys(username);
        WebElement txtPasswordElement = Utilities.findElement(txtPassword);
        txtPasswordElement.sendKeys(password);

        Utilities.click(btnLogin);

        WaitUtils.waitForElementStale(WaitUtils.waitForElementVisible(spinnerLoginButton));

        return this;
    }

    public LoginPage login(UserAccount userAccount, String mode) {
        String identifier = mode.equals("email") ? userAccount.getEmail() : mode.equals("phone") ? userAccount.getPhoneNumber() : "";
        return this.login(identifier, userAccount.getPassword());
    }

    public LoginPage login(UserAccount userAccount) {
        return this.login(userAccount, LOGIN_MODES.EMAIL);
    }

    public HomePage expectSuccess() {
        return new HomePage();
    }

    public LoginPage expectFailure() {
        return this;
    }
}
