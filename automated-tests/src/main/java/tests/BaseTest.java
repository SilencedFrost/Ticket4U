package tests;

import common.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.List;

@Slf4j
public class BaseTest {

    @Parameters("browser")
    @BeforeMethod
    public void beforeMethod(@Optional("chrome") String browser) {
        log.info("Pre-condition");

        List<String> arguments = List.of(
                "--start-maximized"
        );

        Constant.WEBDRIVER = switch(browser) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments(arguments);
                yield new ChromeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments(arguments);
                yield new FirefoxDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    @AfterMethod
    public void afterMethod() {
        log.info("Post-condition");

        Constant.WEBDRIVER.quit();
    }
}
