package common.utility;

import common.constants.Constant;

import java.util.ArrayList;

public class WindowUtils {
    private static ArrayList<String> getTabs() {
        return new ArrayList<>(Constant.WEBDRIVER.getWindowHandles());
    }

    public static void switchToLatestWindow() {
        Constant.WEBDRIVER.switchTo().window(getTabs().getLast());
    }

    public static void switchToWindowIndex(int index) {
        Constant.WEBDRIVER.switchTo().window(getTabs().get(index));
    }

    public static void switchToFirstWindow() {
        switchToWindowIndex(0);
    }

    public static void closeLatestWindow() {
        String currentHandle = Constant.WEBDRIVER.getWindowHandle();
        String latestHandle = getTabs().getLast();

        if (!latestHandle.equals(currentHandle)) {
            Constant.WEBDRIVER.switchTo().window(latestHandle);
            Constant.WEBDRIVER.close();
            Constant.WEBDRIVER.switchTo().window(currentHandle);
        } else {
            Constant.WEBDRIVER.close();
            switchToFirstWindow();
        }
    }

    public static void closeWindowIndex(int index) {
        String currentHandle = Constant.WEBDRIVER.getWindowHandle();
        String targetHandle = getTabs().get(index);

        if (!targetHandle.equals(currentHandle)) {
            Constant.WEBDRIVER.switchTo().window(targetHandle);
            Constant.WEBDRIVER.close();
            Constant.WEBDRIVER.switchTo().window(currentHandle);
        } else {
            Constant.WEBDRIVER.close();
            switchToFirstWindow();
        }
    }

    public static void closeFirstWindow() {
        String currentHandle = Constant.WEBDRIVER.getWindowHandle();
        String firstHandle = getTabs().get(0);

        if (!firstHandle.equals(currentHandle)) {
            Constant.WEBDRIVER.switchTo().window(firstHandle);
            Constant.WEBDRIVER.close();
            Constant.WEBDRIVER.switchTo().window(currentHandle);
        } else {
            Constant.WEBDRIVER.close();
            switchToWindowIndex(1); // Switch to second window if first was current
        }
    }
}
