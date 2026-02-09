package user.tests;

import common.constants.data.AccountDefault;
import common.constants.data.NavbarItem;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;
import user.entities.data.UserAccount;
import user.entities.page.HomePage;
import user.entities.page.LoginPage;

@Slf4j
public class LoginTest extends BaseTest{

    @Test
    public void LGI01_TC01() {
        log.info("LGI01_TC01 - Verify that user can log in successfully with valid email and password");

        // Data
        UserAccount userAccount = new UserAccount(AccountDefault.CUSTOMER);

        log.info("1. Navigate to Ticket4U website");
        HomePage homePage = new HomePage().open();

        log.info("2. Click login button on navbar");
        LoginPage loginPage = (LoginPage) homePage.gotoPage(NavbarItem.LOGIN);

        log.info("3. Enter valid email and password");
        log.info("4. Press login");
        homePage = (HomePage) loginPage.login(userAccount);

        // Assertion
        log.info("User log in successfully");
        Assert.assertTrue(homePage.isUserLoggedIn(), "User is not logged in as expected");
    }
}
