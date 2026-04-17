package tests;

import common.constants.data.AccountConstant;
import common.constants.data.AccountDefault;
import common.constants.data.NavbarItem;
import common.utility.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;
import entities.data.UserAccount;
import entities.page.HomePage;
import entities.page.LoginPage;
import static entities.page.LoginPage.LOGIN_MODES;

@Slf4j
public class LoginTest extends BaseTest{

    @Test
    // Verify that user can log in successfully with valid email and password
    // Verify that system does not display error message when user login with valid email and password
    public void LGI01_TC0107() {
        log.info("LGI01_TC01 - Verify that user can log in successfully with valid email and password");
        log.info("LGI01_TC07 - Verify that system does not display error message when user login with valid email and password");

        // Data
        UserAccount userAccount = new UserAccount(AccountDefault.CUSTOMER);

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter valid email and password");
        log.info("4. Press login");
        HomePage homePage = loginPage.login(userAccount).expectSuccess();

        // Assertion
        log.info("User log in successfully");
        log.info("System does not display error message");
        Assert.assertTrue(homePage.isUserLoggedIn(), "User is not logged in as expected");
    }

    @Test
    // Verify that user can log in successfully with phone number and password
    // Verify that system does not display error message when user login with valid phone number and password
    public void LGI01_TC0208() {
        log.info("LGI01_TC02 - Verify that user can log in successfully with phone number and password");
        log.info("LGI01_TC08 - Verify that system does not display error message when user login with valid phone number and password");

        UserAccount userAccount = new UserAccount(AccountDefault.CUSTOMER);

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter valid phone number and password");
        log.info("4. Press login");
        HomePage homePage = loginPage.login(userAccount, LOGIN_MODES.PHONE).expectSuccess();

        // Assertion
        log.info("User log in successfully");
        log.info("System does not display error message");
        Assert.assertTrue(homePage.isUserLoggedIn(), "User is not logged in as expected");
    }

    @Test
    // Verify that system reject user login attempt with invalid email and password
    public void LGI01_TC03() {
        log.info("LGI01_TC03 - Verify that system reject user login attempt with invalid email and password");

        // Data
        UserAccount userAccount = new UserAccount(AccountDefault.RANDOM);

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter invalid email and password");
        log.info("4. Press login");
        loginPage.login(userAccount).expectFailure();

        // Assertion
        log.info("System reject user login attempt");
        Assert.assertTrue(loginPage.isComponentDisplayed(), "System did not reject user login attempt as expected");
    }

    @Test
    // Verify that system reject user login attempt with invalid phone number and password
    public void LGI01_TC04() {
        log.info("LGI01_TC04 - Verify that system reject user login attempt with invalid phone number and password");

        // Data
        UserAccount userAccount = new UserAccount(AccountDefault.RANDOM);

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter invalid phone number and password");
        log.info("4. Press login");
        loginPage.login(userAccount, LOGIN_MODES.PHONE).expectFailure();

        // Assertion
        log.info("System reject user login attempt");
        Assert.assertTrue(loginPage.isComponentDisplayed(), "System did not reject user login attempt as expected");
    }

    @Test
    // Verify that system reject user login attempt with valid email and invalid password
    public void LGI01_TC05() {
        log.info("LGI01_TC05 - Verify that system reject user login attempt with valid email and invalid password");

        // Data
        UserAccount userAccount = new UserAccount(AccountConstant.EMAIL_CUSTOMER, RandomUtils.generateRandomPassword());

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter valid email");
        log.info("4. Enter invalid password");
        log.info("5. Press login");
        loginPage.login(userAccount).expectFailure();

        // Assertion
        log.info("System reject user login attempt");
        Assert.assertTrue(loginPage.isComponentDisplayed(), "System did not reject user login attempt as expected");
    }

    @Test
    // Verify that system reject user login attempt with valid phone number and invalid password
    public void LGI01_TC06() {
        log.info("LGI01_TC06 - Verify that system reject user login attempt with valid phone number and invalid password");

        // Data
        UserAccount userAccount = new UserAccount(AccountConstant.PHONE_DEFAULT, RandomUtils.generateRandomPassword());

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter valid phone number");
        log.info("4. Enter invalid password");
        log.info("5. Press login");
        loginPage.login(userAccount, LOGIN_MODES.PHONE).expectFailure();

        // Assertion
        log.info("System reject user login attempt");
        Assert.assertTrue(loginPage.isComponentDisplayed(), "System did not reject user login attempt as expected");
    }

    @Test
    // Verify that system display error message when user attempt login with invalid email and password
    public void LGI01_TC09() {
        log.info("LGI01_TC09 - Verify that system display error message when user attempt login with invalid email and password");

        // Data
        UserAccount userAccount = new UserAccount(AccountDefault.RANDOM);
        String expectedMsg = "Invalid login information";

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter invalid email and password");
        log.info("4. Press login");
        loginPage.login(userAccount).expectFailure();

        // Assertion
        log.info("System display error message");
        String actualMsg = loginPage.getFormErrorMsg();

        Assert.assertEquals(actualMsg, expectedMsg, "Error message is not displayed as expected");
    }

    @Test
    // Verify that system display error message when user attempt login with invalid phone number and password
    public void LGI01_TC10() {
        log.info("LGI01_TC09 - Verify that system display error message when user attempt login with invalid phone number and password");

        // Data
        UserAccount userAccount = new UserAccount(RandomUtils.generateRandomPhoneNumber(), RandomUtils.generateRandomPassword());
        String expectedMsg = "Invalid login information";

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter invalid email and password");
        log.info("4. Press login");
        loginPage.login(userAccount, LOGIN_MODES.PHONE).expectFailure();

        // Assertion
        log.info("System display error message");
        String actualMsg = loginPage.getFormErrorMsg();

        Assert.assertEquals(actualMsg, expectedMsg, "Error message is not displayed as expected");
    }

    @Test
    // Verify that system display error message when user attempt login with valid email and invalid password
    public void LGI01_TC11() {
        log.info("Verify that system display error message when user attempt login with valid email and invalid password");

        // Data
        UserAccount userAccount = new UserAccount(AccountConstant.EMAIL_CUSTOMER, RandomUtils.generateRandomPassword());
        String expectedMsg = "Invalid login information";

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter valid email");
        log.info("4. Enter invalid password");
        log.info("5. Press login");
        loginPage.login(userAccount).expectFailure();

        // Assertion
        log.info("System display error message");
        String actualMsg = loginPage.getFormErrorMsg();

        Assert.assertEquals(actualMsg, expectedMsg, "Error message is not displayed as expected");
    }

    @Test
    // Verify that system display error message when user attempt login with valid phone number and invalid password
    public void LGI01_TC12() {
        log.info("Verify that system display error message when user attempt login with valid phone number and invalid password");

        // Data
        UserAccount userAccount = new UserAccount(AccountConstant.PHONE_DEFAULT, RandomUtils.generateRandomPassword());
        String expectedMsg = "Invalid login information";

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter valid email");
        log.info("4. Enter invalid password");
        log.info("5. Press login");
        loginPage.login(userAccount, LOGIN_MODES.PHONE).expectFailure();

        // Assertion
        log.info("System display error message");
        String actualMsg = loginPage.getFormErrorMsg();

        Assert.assertEquals(actualMsg, expectedMsg, "Error message is not displayed as expected");
    }
}
