package common.utility;

import common.constants.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class WaitUtils {

    // Waits
    public static WebElement waitForElementVisible(By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElementVisible(By locator) {
        return waitForElementVisible(locator, Constant.FIND_ELEMENT_TIMEOUT);
    }

    public static WebElement waitForElementPresent(By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, timeout);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));

    }

    public static WebElement waitForElementPresent(By locator) {
        return waitForElementPresent(locator, Constant.FIND_ELEMENT_TIMEOUT);
    }

    public static WebElement waitForElementClickable(By locator, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElementClickable(By locator) {
        return waitForElementClickable(locator, Constant.FIND_ELEMENT_TIMEOUT);
    }

    public static void waitForElementStale(WebElement webElement, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, timeout);
        wait.until(ExpectedConditions.stalenessOf(webElement));
    }

    public static void waitForElementStale(WebElement webElement) {
        waitForElementStale(webElement, Constant.FIND_ELEMENT_TIMEOUT);
    }

    public static void waitForPageLoad(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, timeout);
        wait.until(webDriver -> Objects.equals(((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState"), "complete"));
    }

    public static void waitForPageLoad() {
        waitForPageLoad(Constant.PAGE_LOAD_TIMEOUT);
    }

    public static void waitForAlertPresent(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, timeout);
        wait.until(ExpectedConditions.alertIsPresent());
    }

    public static void waitForAlertPresent() {
        waitForAlertPresent(Constant.FIND_ELEMENT_TIMEOUT);
    }
}
