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

@Slf4j
public class LoginTest extends BaseTest{

    @Test
    public void LGI01_TC01_LGI01_TC07() {
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

    // @Test TODO: implement phone number login feature
    public void LGI01_TC02_LGI01_TC08() {
        log.info("LGI01_TC02 - Verify that user can log in successfully with phone number and password");
        log.info("LGI01_TC08 - Verify that system does not display error message when user login with valid phone number and password");

        // Data TODO: add user data
        UserAccount userAccount = new UserAccount(null);

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter valid phone number and password");
        log.info("4. Press login");
        HomePage homePage = loginPage.login(userAccount).expectSuccess();

        // Assertion
        log.info("User log in successfully");
        log.info("System does not display error message");
        Assert.assertTrue(homePage.isUserLoggedIn(), "User is not logged in as expected");
    }

    @Test
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

    // @Test TODO: implement phone number login feature
    public void LGI01_TC04() {
        log.info("LGI01_TC04 - Verify that system reject user login attempt with invalid phone number and password");

        // Data TODO: add user data
        UserAccount userAccount = new UserAccount(null);

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter invalid phone number and password");
        log.info("4. Press login");
        loginPage.login(userAccount).expectFailure();

        // Assertion
        log.info("System reject user login attempt");
        Assert.assertTrue(loginPage.isComponentDisplayed(), "System did not reject user login attempt as expected");
    }

    @Test
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

    // @Test TODO: implement phone number login feature
    @Test
    public void LGI01_TC06() {
        log.info("LGI01_TC06 - Verify that system reject user login attempt with valid phone number and invalid password");

        // Data TODO: add user data
        UserAccount userAccount = new UserAccount(null);

        // Actions
        log.info("1. Navigate to Ticket4U website");
        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) new HomePage().open().gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter valid phone number");
        log.info("4. Enter invalid password");
        log.info("5. Press login");
        loginPage.login(userAccount).expectFailure();

        // Assertion
        log.info("System reject user login attempt");
        Assert.assertTrue(loginPage.isComponentDisplayed(), "System did not reject user login attempt as expected");
    }


}
