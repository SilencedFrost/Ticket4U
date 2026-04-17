package common.constants;

import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class Constant {

    public static WebDriver WEBDRIVER;
    public static final String TICKET4U_URL = "https://ticket4u.uk";
    public static final Duration FIND_ELEMENT_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(60);
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");
}