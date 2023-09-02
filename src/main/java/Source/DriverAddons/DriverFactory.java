package Source.DriverAddons;

import Source.CustomDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;

public class DriverFactory {

    private static boolean headless;

    // Takes a string of a browser name and returns corresponding ETypeDriver
    public static ETypeDriver stringToEtypeDriver(String browser) {
        browser = browser.toLowerCase();
        switch (browser) {
            case "firefox":
            case "mozilla":
                return ETypeDriver.FIREFOX;

            case "edge":
            case "ms edge":
            case "microsoft edge:":
                return ETypeDriver.EDGE;
        }

        return ETypeDriver.CHROME;
    }

    // Returns a driver by driverType with some configurations

    public static WebDriver GetBrowser(ETypeDriver driverType, int timeout, String[] args) {
        switch (driverType) {
            case FIREFOX -> {
                return GetFirefox(timeout, args);
            }

            case EDGE -> {
                return GetEdge(timeout, args);
            }

            default -> {
                return GetChrome(timeout, args);
            }
        }
    }

    public static WebDriver GetBrowser(ETypeDriver driverType, int timeout) {
        switch (driverType) {
            case FIREFOX -> {
                return GetFirefox(timeout);
            }

            case EDGE -> {
                return GetEdge(timeout);
            }

            default -> {
                return GetChrome(timeout);
            }
        }
    }

    public static WebDriver GetBrowser(ETypeDriver driverType) {
        int timeout = 10;
        switch (driverType) {
            case FIREFOX -> {
                return GetFirefox(timeout);
            }

            case EDGE -> {
                return GetEdge(timeout);
            }

            default -> {
                return GetChrome(timeout);
            }
        }
    }


    // PRIVATE METHODS FOR GetBrowser
    private static WebDriver GetChrome(int timeout, String[] args) {
        WebDriverManager.chromedriver().setup();
        var opts = new ChromeOptions();
        if (headless) opts.addArguments("--headless");
        opts.addArguments("--ignore-ssl-errors=yes", "--ignore-certificate-errors", "--window-size=1980,1080");
        opts.addArguments(args);

        var driver = new ChromeDriver(opts);
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

        return driver;
    }

    private static WebDriver GetChrome(int timeout) {
        WebDriverManager.chromedriver().setup();
        var opts = new ChromeOptions();
        if (headless) opts.addArguments("--headless");
        opts.addArguments("--ignore-ssl-errors=yes", "--ignore-certificate-errors", "--window-size=1980,1080");

        var driver = new ChromeDriver(opts);
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

        return driver;
    }

    private static WebDriver GetFirefox(int timeout, String[] args) {
        WebDriverManager.firefoxdriver().setup();
        var opts = new FirefoxOptions();
        if (headless) opts.addArguments("--headless");
        opts.addArguments("--ignore-ssl-errors=yes", "--ignore-certificate-errors", "--window-size=1980,1080");
        opts.addArguments(args);

        var driver = new FirefoxDriver(opts);
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

        return driver;
    }

    private static WebDriver GetFirefox(int timeout) {
        WebDriverManager.firefoxdriver().setup();
        var opts = new FirefoxOptions();
        if (headless) opts.addArguments("--headless");
        opts.addArguments("--ignore-ssl-errors=yes", "--ignore-certificate-errors", "--window-size=1980,1080");

        var driver = new FirefoxDriver(opts);
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

        return driver;
    }

    private static WebDriver GetEdge(int timeout, String[] args) {
        WebDriverManager.edgedriver().setup();
        var opts = new EdgeOptions();
        if (headless) opts.addArguments("--headless");
        opts.addArguments("--ignore-ssl-errors=yes", "--ignore-certificate-errors", "--window-size=1980,1080");
        opts.addArguments(args);

        var driver = new EdgeDriver(opts);
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

        return driver;
    }

    private static WebDriver GetEdge(int timeout) {
        WebDriverManager.edgedriver().setup();
        var opts = new EdgeOptions();
        if (headless) opts.addArguments("--headless");
        opts.addArguments("--ignore-ssl-errors=yes", "--ignore-certificate-errors", "--window-size=1980,1080");

        var driver = new EdgeDriver(opts);
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

        return driver;
    }
}
