package common.utility;

import common.constants.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

public class Utilities {

    // Checks
    public static boolean isElementPresent(By locator) {
        try {
            Constant.WEBDRIVER.findElement(locator);
            return true;
        } catch(NoSuchElementException e) {
            return false;
        }
    }

    // Find
    public static WebElement findElement(By locator, Duration pageLoadTimeout, Duration elementTimeout) {
        WaitUtils.waitForPageLoad(pageLoadTimeout);
        // Wait to prevent flaky scenarios
        WaitUtils.waitForElementPresent(locator, elementTimeout);
        return Constant.WEBDRIVER.findElement(locator);
    }

    public static WebElement findElement(By locator, Duration elementTimeout) {
        return findElement(locator, Constant.PAGE_LOAD_TIMEOUT, elementTimeout);
    }

    public static WebElement findElement(By locator) {
        return findElement(locator, Constant.FIND_ELEMENT_TIMEOUT);
    }

    public static List<WebElement> findElements(By locator, Duration pageLoadTimeout, Duration elementTimeout) {
        WaitUtils.waitForPageLoad(pageLoadTimeout);
        // Wait to prevent flaky scenarios
        WaitUtils.waitForElementPresent(locator, elementTimeout);
        return Constant.WEBDRIVER.findElements(locator);
    }

    public static List<WebElement> findElements(By locator, Duration elementTimeout) {
        return findElements(locator, Constant.PAGE_LOAD_TIMEOUT, elementTimeout);
    }

    public static List<WebElement> findElements(By locator) {
        return findElements(locator, Constant.FIND_ELEMENT_TIMEOUT);
    }

    // Actions
    public static void click(By locator, Duration timeout){
        WaitUtils.waitForElementVisible(locator, timeout);
        scrollToElement(locator, timeout);
        WaitUtils.waitForElementClickable(locator, timeout);
        findElement(locator).click();
    }

    public static void click(By locator) {
        click(locator, Constant.FIND_ELEMENT_TIMEOUT);
    }

    public static By scrollToElement(By locator, Duration timeout) {
        JavascriptExecutor js = (JavascriptExecutor) Constant.WEBDRIVER;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center', inline: 'nearest'});", findElement(locator, timeout));
        return locator;
    }

    public static By scrollToElement(By locator) {
        return scrollToElement(locator, Constant.FIND_ELEMENT_TIMEOUT);
    }

    public static By selectComboboxByVisibleText(By locator, String visibleText, Duration timeout) {
        WaitUtils.waitForElementVisible(locator, timeout);
        scrollToElement(locator, timeout);
        WaitUtils.waitForElementClickable(locator, timeout);
        new Select(findElement(locator)).selectByVisibleText(visibleText);
        return locator;
    }

    public static By selectComboboxByVisibleText(By locator, String visibleText) {
        return selectComboboxByVisibleText(locator, visibleText, Constant.FIND_ELEMENT_TIMEOUT);
    }
}