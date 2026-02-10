package tests;

import common.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@Slf4j
public class BaseTest {

    @BeforeMethod
    public void beforeMethod() {
        log.info("Pre-condition");

        Constant.WEBDRIVER = new ChromeDriver();
        Constant.WEBDRIVER.manage().window().maximize();
    }

    @AfterMethod
    public void afterMethod() {
        log.info("Post-condition");

        Constant.WEBDRIVER.quit();
    }
}
