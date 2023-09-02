package Source;

import Utils.Reporter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CustomDriver {
    public WebDriver driver;
    public JavascriptExecutor js;
    private Reporter log;

    public CustomDriver(WebDriver driver, Reporter log) {
        this.driver = driver;
        this.log = log;
        js = (JavascriptExecutor) driver;
    }

    public void quit() {
        log.info("driver quit");
        driver.quit();
    }

    public WebDriverWait wait(int timeout) {
        return new WebDriverWait(driver, Duration.ofMillis(timeout));
    }

}
